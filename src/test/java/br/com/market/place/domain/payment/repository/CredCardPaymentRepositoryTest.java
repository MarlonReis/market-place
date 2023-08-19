package br.com.market.place.domain.payment.repository;

import br.com.market.place.domain.customer.entity.Physical;
import br.com.market.place.factory.CustomerEntityMockFactory;
import br.com.market.place.factory.PaymentEntityMockFactory;
import br.com.market.place.domain.payment.constant.PaymentStatus;
import br.com.market.place.domain.payment.entity.CreditCard;
import br.com.market.place.domain.payment.service.RunPaymentService;
import br.com.market.place.domain.payment.value.CardPan;
import br.com.market.place.domain.shared.value.Address;
import br.com.market.place.domain.shared.value.Currency;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.hamcrest.MatcherAssert.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CredCardPaymentRepositoryTest {
    @Autowired
    private PaymentRepository repository;
    private Address address;
    private CreditCard credCard;
    private Physical physical;

    @BeforeEach
    void setUp() {
        physical = new CustomerEntityMockFactory().makePhysicalFactory().now();

        address = physical.getAddress();
        credCard = new PaymentEntityMockFactory().credCardFactory(physical);

    }

    @Test
    void shouldSaveCredCardPaymentWithSuccess() {
        var response = repository.saveAndFlush(credCard);

        assertThat(response.getId(), Matchers.notNullValue());
        assertThat(response.getAddress(), Matchers.is(address));
        assertThat(response.getStatus(), Matchers.is(PaymentStatus.PENDING));
        assertThat(response.getAmount(), Matchers.is(new Currency("10.0", "BRL")));
        assertThat(response.getCardPan(), Matchers.is(new CardPan("2720339563597456")));
        assertThat(response.getCreateAt(), Matchers.notNullValue());
        assertThat(response.getUpdateAt(), Matchers.nullValue());
    }

    @Test
    void shouldUpdateToSuccessPaymentWhenWithSuccess() {
        RunPaymentService paymentService = Mockito.mock(RunPaymentService.class);
        Mockito.when(paymentService.executePayment(ArgumentMatchers.any())).thenReturn(PaymentStatus.PAID_OUT);
        repository.saveAndFlush(credCard);

        credCard.pay(paymentService);
        repository.saveAndFlush(credCard);

        var response = repository.findById(credCard.getId()).orElseThrow();

        assertThat(response.getStatus(), Matchers.is(PaymentStatus.PAID_OUT));
        assertThat(response.getCreateAt(), Matchers.notNullValue());
        assertThat(response.getUpdateAt(), Matchers.notNullValue());
    }

}