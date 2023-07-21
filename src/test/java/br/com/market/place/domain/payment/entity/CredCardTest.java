package br.com.market.place.domain.payment.entity;

import br.com.market.place.domain.customer.entity.Legal;
import br.com.market.place.domain.customer.value.Email;
import br.com.market.place.domain.customer.value.Name;
import br.com.market.place.domain.payment.constant.PaymentStatus;
import br.com.market.place.domain.payment.service.CancelPaymentService;
import br.com.market.place.domain.payment.service.RunPaymentService;
import br.com.market.place.domain.payment.value.CardPan;
import br.com.market.place.domain.shared.exception.PaymentException;
import br.com.market.place.domain.shared.value.Address;
import br.com.market.place.domain.shared.value.Currency;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

import static br.com.market.place.domain.payment.constant.PaymentStatus.*;
import static br.com.market.place.domain.shared.constant.CurrencyType.BRL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CredCardTest {
    private Address address;
    private CredCard credCard;

    @BeforeEach
    void setUp() {
        Mockito.clearAllCaches();

        address = Address.Builder.build().withCity("London").withStreet("Baker Street").withNumber("221").withComponent("B").withZipCode("37540232").now();
        credCard = CredCard.Builder.build()
                .withStatusPending()
                .withCardPan(new CardPan("2720339563597456"))
                .withAmount(new Currency("10.0", BRL))
                .withCustomer(Legal.Builder.build().withEmail(new Email("any@email.com")).now())
                .withAddress(address)
                .now();

    }

    @Test
    void shouldReturnCredCardPaymentWithStatusPending() {
        assertThat(credCard.getId(), Matchers.nullValue());
        assertThat(credCard.getCardPan(), Matchers.is(new CardPan("2720339563597456")));
        assertThat(credCard.getStatus(), Matchers.is(PaymentStatus.PENDING));
        assertThat(credCard.getAmount(), Matchers.is(new Currency("10.0", BRL)));
        assertThat(credCard.getAddress(), Matchers.is(address));
        assertThat(credCard.getCreateAt(), Matchers.nullValue());
        assertThat(credCard.getUpdateAt(), Matchers.nullValue());
        assertThat(credCard.getCustomer().getEmail(),Matchers.is(new Email("any@email.com")));
    }

    @Test
    void shouldUpdatePaymentStatusToSuccessWhenCallRunPaymentService() {
        RunPaymentService runPaymentService = Mockito.mock(RunPaymentService.class);

        Mockito.when(runPaymentService.executePayment()).thenReturn(SUCCESS);
        credCard.pay(runPaymentService);

        assertThat(credCard.getStatus(), Matchers.is(SUCCESS));
        Mockito.verify(runPaymentService).executePayment();
    }

    @ParameterizedTest
    @EnumSource(names = {"PENDING", "SUCCESS"})
    void shouldUpdatePaymentStatusToCancelWhenCallCancelPaymentService(PaymentStatus status) {
        CancelPaymentService cancelPaymentService = Mockito.mock(CancelPaymentService.class);
        RunPaymentService runPaymentService = Mockito.mock(RunPaymentService.class);
        Mockito.when(runPaymentService.executePayment()).thenReturn(status);

        credCard.pay(runPaymentService);

        Mockito.when(cancelPaymentService.cancelPayment()).thenReturn(SUCCESS);
        credCard.cancelPayment(cancelPaymentService);

        assertThat(credCard.getStatus(), Matchers.is(CANCELED));
        Mockito.verify(cancelPaymentService).cancelPayment();
    }

    @ParameterizedTest
    @EnumSource(names = {"CANCELED", "FAIL", "EXPIRED"})
    void shouldThrowsPaymentExceptionWhenPaymentCannotBeUpdate(PaymentStatus status) {
        CancelPaymentService cancelPaymentService = Mockito.mock(CancelPaymentService.class);
        RunPaymentService runPaymentService = Mockito.mock(RunPaymentService.class);
        Mockito.when(runPaymentService.executePayment()).thenReturn(status);

        credCard.pay(runPaymentService);
        var exception = assertThrows(PaymentException.class, () -> credCard.cancelPayment(cancelPaymentService));

        assertThat(exception.getMessage(), Matchers.containsString("Cannot cancel payment with status"));
        assertThat(exception.getMessage(), Matchers.containsString(status.name()));

        Mockito.verify(cancelPaymentService, Mockito.never()).cancelPayment();
    }

    @ParameterizedTest
    @EnumSource(names = {"PENDING", "FAIL", "CANCELED"})
    void shouldUpdatePaymentStatusToCancelJustWhenCallCancelPaymentServiceReturnSuccess(PaymentStatus status) {
        CancelPaymentService cancelPaymentService = Mockito.mock(CancelPaymentService.class);

        Mockito.when(cancelPaymentService.cancelPayment()).thenReturn(status);
        credCard.cancelPayment(cancelPaymentService);

        assertThat(credCard.getStatus(), Matchers.is(PENDING));
    }


}