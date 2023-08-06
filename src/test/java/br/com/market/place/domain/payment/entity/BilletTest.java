package br.com.market.place.domain.payment.entity;

import br.com.market.place.factory.CustomerEntityMockFactory;
import br.com.market.place.factory.PaymentEntityMockFactory;
import br.com.market.place.domain.payment.constant.PaymentStatus;
import br.com.market.place.domain.payment.service.CancelPaymentService;
import br.com.market.place.domain.payment.service.RunPaymentService;
import br.com.market.place.domain.shared.exception.PaymentException;
import br.com.market.place.domain.shared.value.Address;
import br.com.market.place.domain.shared.value.Currency;
import br.com.market.place.domain.shared.value.DueDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;

class BilletTest {
    private Address address;
    private Billet billet;

    @BeforeEach
    void setUp() {
        var customer = new CustomerEntityMockFactory().makePhysicalFactory().now();
        address = Address.Builder.build().withCity("London").withStreet("Baker Street").withNumber("221").withComponent("B").withZipCode("37540232").now();
        billet = new PaymentEntityMockFactory().billetFactory(customer);
    }

    @Test
    void shouldReturnBilling() {
        assertThat(billet.getId(), Matchers.nullValue());
        assertThat(billet.getCreateAt(), Matchers.nullValue());
        assertThat(billet.getUpdateAt(), Matchers.nullValue());
        assertThat(billet.getDueDate(), Matchers.is(new DueDate(3)));
        assertThat(billet.getAmount(), Matchers.is(new Currency("10.0", "BRL")));
        assertThat(billet.getStatus(), Matchers.is(PaymentStatus.PENDING));
        assertThat(billet.getCustomer(), Matchers.notNullValue());
        assertThat(billet.getAddress(), Matchers.is(address));
        assertThat(billet.getPayLine(), Matchers.is("09580928590923405023450230450234509234508080800"));
    }

    @Test
    void shouldCallRunPaymentServiceWhenPayTheBillet() {
        RunPaymentService service = Mockito.mock(RunPaymentService.class);
        Mockito.when(service.executePayment(ArgumentMatchers.any())).thenReturn(PaymentStatus.PAID_OUT);

        billet.pay(service);

        assertThat(billet.getStatus(), Matchers.is(PaymentStatus.PAID_OUT));
        ArgumentCaptor<Payment> argument = ArgumentCaptor.forClass(Payment.class);
        Mockito.verify(service).executePayment(argument.capture());
    }

    @Test
    void shouldThrowPaymentExceptionWhenTryPayWithPaymentWithStatusIsDifferentOfPending() {
        RunPaymentService service = Mockito.mock(RunPaymentService.class);
        Payment payment = Billet.Builder.build().now();
        payment.setStatus(PaymentStatus.CANCELED);

        var exception = assertThrows(PaymentException.class, () -> payment.pay(service));
        assertThat(exception.getMessage(),Matchers.is("Cannot run billet payment with status different of pending!"));
    }

    @ParameterizedTest
    @EnumSource(names = {"CANCELED","FAIL","EXPIRED","REVERSED"})
    void shouldThrowPaymentExceptionWhenTryCancelPaymentWithStatusIsDifferentOfPendingAndPaidOut(PaymentStatus status) {
        CancelPaymentService service = Mockito.mock(CancelPaymentService.class);
        Payment payment = Billet.Builder.build().now();
        payment.setStatus(status);

        var exception = assertThrows(PaymentException.class, () -> payment.cancelPayment(service));
        assertThat(exception.getMessage(),Matchers.is(String.format("Cannot cancel payment with status %s!", status.name())));
    }

    @Test
    void shouldCallCancelPaymentServiceWhenCancelTheBilletPayment() {
        CancelPaymentService service = Mockito.mock(CancelPaymentService.class);
        Mockito.when(service.cancelPayment(ArgumentMatchers.any())).thenReturn(PaymentStatus.CANCELED);

        billet.cancelPayment(service);

        assertThat(billet.getStatus(), Matchers.is(PaymentStatus.CANCELED));
        ArgumentCaptor<Payment> argument = ArgumentCaptor.forClass(Payment.class);
        Mockito.verify(service).cancelPayment(argument.capture());
    }

    @ParameterizedTest
    @EnumSource(names = {"CANCELED", "FAIL", "EXPIRED"})
    void shouldPaymentExceptionWhenBilletNotHaveStatus(PaymentStatus status) {
        RunPaymentService pay = Mockito.mock(RunPaymentService.class);
        CancelPaymentService service = Mockito.mock(CancelPaymentService.class);

        Mockito.when(pay.executePayment(ArgumentMatchers.any())).thenReturn(status);
        Mockito.when(service.cancelPayment(ArgumentMatchers.any())).thenReturn(PaymentStatus.CANCELED);

        billet.pay(pay);

        var exception = assertThrows(PaymentException.class, () -> billet.cancelPayment(service));
        assertThat(exception.getMessage(), Matchers.containsString("Cannot cancel payment with status"));
        assertThat(exception.getMessage(), Matchers.containsString(status.name()));
    }

    @Test
    void shouldReturnFalseWhenBilletIsNotEquals() {
        UUID mockId = UUID.randomUUID();
        Billet billetStub = Billet.Builder.build().now();

        try (MockedStatic<UUID> mocked = Mockito.mockStatic(UUID.class)) {
            mocked.when(UUID::randomUUID).thenReturn(mockId);

            billetStub.prePersist();
            billet.prePersist();

            assertTrue(billet.equals(billetStub));
        }

        assertFalse(billet.equals(Billet.Builder.build().now()));
        assertFalse(billet.equals(new Object()));
        assertFalse(billet.equals(null));

    }

    @Test
    void shouldDefineValueAfterCallPrePersistAndPreUpdate() {
        Payment payment = Billet.Builder.build().now();

        payment.prePersist();
        payment.preUpdate();

        assertThat(payment.getId(), Matchers.notNullValue());
        assertThat(payment.getCreateAt(), Matchers.notNullValue());
        assertThat(payment.getUpdateAt(), Matchers.notNullValue());
    }

}