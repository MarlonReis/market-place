package br.com.market.place.domain.payment.repository;

import br.com.market.place.domain.customer.entity.Physical;
import br.com.market.place.domain.customer.value.BirthDate;
import br.com.market.place.domain.customer.value.Email;
import br.com.market.place.domain.customer.value.Name;
import br.com.market.place.domain.customer.value.Telephone;
import br.com.market.place.domain.payment.constant.PaymentStatus;
import br.com.market.place.domain.payment.entity.CredCard;
import br.com.market.place.domain.payment.service.RunPaymentService;
import br.com.market.place.domain.payment.value.CardPan;
import br.com.market.place.domain.shared.value.Address;
import br.com.market.place.domain.shared.value.Currency;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static br.com.market.place.domain.customer.constant.DocumentType.CPF;
import static br.com.market.place.domain.shared.constant.CurrencyType.BRL;
import static org.hamcrest.MatcherAssert.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CredCardPaymentRepositoryTest {
    @Autowired
    private PaymentRepository repository;
    private Address address;
    private CredCard credCard;
    private Physical physical;

    @BeforeEach
    void setUp() {
        physical = Physical.Builder.build()
                .withName(new Name("Benedito Caio Araújo"))
                .withDocument("536.271.871-13", CPF)
                .withEmail(new Email("benedito-araujo91@gmnail.com"))
                .withBirthDate(new BirthDate("23/02/2001"))
                .withTelephone(new Telephone("11999982343"))
                .withAddress(Address.Builder.build().withCity("London").withStreet("Baker Street").withNumber("221").withComponent("B").withZipCode("37540232").now())
                .now();

        address = Address.Builder.build().withCity("London").withStreet("Baker Street"). withNumber("221").withComponent("B").withZipCode("37540232").now();

        credCard = CredCard.Builder.build().
                withStatusPending().
                withCardPan(new CardPan("2720339563597456")).
                withAmount(new Currency("10.0", BRL)).
                withCustomer(physical).
                withAddress(address).now();

    }

    @Test
    void shouldSaveCredCardPaymentWithSuccess() {
        var response = repository.saveAndFlush(credCard);

        assertThat(response.getId(), Matchers.notNullValue());
        assertThat(response.getAddress(), Matchers.is(address));
        assertThat(response.getStatus(), Matchers.is(PaymentStatus.PENDING));
        assertThat(response.getAmount(), Matchers.is(new Currency("10.0", BRL)));
        assertThat(response.getCardPan(), Matchers.is(new CardPan("2720339563597456")));
        assertThat(response.getCreateAt(), Matchers.notNullValue());
        assertThat(response.getUpdateAt(), Matchers.nullValue());
    }

    @Test
    void shouldUpdateToSuccessPaymentWhenWithSuccess() {
        RunPaymentService paymentService = Mockito.mock(RunPaymentService.class);
        Mockito.when(paymentService.executePayment()).thenReturn(PaymentStatus.SUCCESS);
        repository.saveAndFlush(credCard);

        credCard.pay(paymentService);
        repository.saveAndFlush(credCard);

        var response = repository.findById(credCard.getId()).orElseThrow();

        assertThat(response.getStatus(), Matchers.is(PaymentStatus.SUCCESS));
        assertThat(response.getCreateAt(), Matchers.notNullValue());
        assertThat(response.getUpdateAt(), Matchers.notNullValue());
    }

}