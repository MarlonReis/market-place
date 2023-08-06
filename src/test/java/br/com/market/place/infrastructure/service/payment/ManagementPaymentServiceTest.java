package br.com.market.place.infrastructure.service.payment;

import br.com.market.place.domain.customer.entity.Legal;
import br.com.market.place.domain.shared.exception.PaymentException;
import br.com.market.place.factory.CustomerEntityMockFactory;
import br.com.market.place.factory.PaymentEntityMockFactory;
import br.com.market.place.domain.customer.repository.CustomerRepository;
import br.com.market.place.domain.payment.constant.PaymentStatus;
import br.com.market.place.domain.payment.entity.Payment;
import br.com.market.place.domain.payment.repository.PaymentRepository;
import br.com.market.place.domain.payment.service.PaymentService;
import br.com.market.place.domain.payment.value.PaymentId;
import br.com.market.place.domain.shared.exception.NotFoundException;
import br.com.market.place.infrastructure.service.payment.CancelPaymentExternalService;
import br.com.market.place.infrastructure.service.payment.ManagementPaymentService;
import br.com.market.place.infrastructure.service.payment.RunPaymentExternalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest()
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Import({
        CancelPaymentExternalService.class,
        RunPaymentExternalService.class,
        ManagementPaymentService.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ManagementPaymentServiceTest {
    @Autowired
    private PaymentService paymentService;
    @SpyBean
    private PaymentRepository paymentRepository;
    @SpyBean
    private CustomerRepository customerRepository;

    private PaymentId paymentId;

    @BeforeEach
    void setUp() {
        var paymentFactory = new PaymentEntityMockFactory();
        var customerFactory = new CustomerEntityMockFactory();

        Legal customer = customerFactory.makeLegalFactory().now();
        var credCard = paymentFactory.credCardFactory(customer);


        customerRepository.saveAndFlush(customer);
        paymentRepository.save(credCard);

        paymentId = credCard.getId();
    }

    @Test
    void shouldRunPaymentWhenFoundById() {
        paymentService.pay(paymentId);

        ArgumentCaptor<Payment> argument = ArgumentCaptor.forClass(Payment.class);
        Mockito.verify(paymentRepository).saveAndFlush(argument.capture());

        Payment payment = argument.getValue();
        assertThat(payment.getId(), Matchers.notNullValue());
        assertThat(payment.getStatus(), Matchers.is(PaymentStatus.PAID_OUT));
    }

    @Test
    void shouldThrowsNotFoundExceptionWhenNotFoundPaymentById() {
        var exception = assertThrows(NotFoundException.class, () -> paymentService.pay(new PaymentId()));
        assertThat(exception.getMessage(), Matchers.is("Cannot be found payment by id"));
    }

    @Test
    void shouldCancelPaymentWhenFoundById() {
        paymentService.cancel(paymentId);

        ArgumentCaptor<Payment> argument = ArgumentCaptor.forClass(Payment.class);
        Mockito.verify(paymentRepository).saveAndFlush(argument.capture());

        Payment payment = argument.getValue();
        assertThat(payment.getId(), Matchers.notNullValue());
        assertThat(payment.getStatus(), Matchers.is(PaymentStatus.CANCELED));
    }

    @Test
    void shouldThrowPaymentExceptionWhenPayThrowException() {
        Mockito.doThrow(RuntimeException.class).when(paymentRepository).saveAndFlush(ArgumentMatchers.any());
        var exception = assertThrows(PaymentException.class, () -> paymentService.pay(paymentId));
        assertThat(exception.getMessage(),Matchers.is("Cannot be run payment!"));
    }

    @Test
    void shouldThrowPaymentExceptionWhenCancelThrowException() {
        Mockito.doThrow(RuntimeException.class).when(paymentRepository).saveAndFlush(ArgumentMatchers.any());
        var exception = assertThrows(PaymentException.class, () -> paymentService.cancel(paymentId));
        assertThat(exception.getMessage(),Matchers.is("Cannot be cancel the payment!"));
    }

}