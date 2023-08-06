package br.com.market.place.domain.customer.repository;

import br.com.market.place.domain.customer.entity.Customer;
import br.com.market.place.domain.customer.entity.Legal;
import br.com.market.place.factory.CustomerEntityMockFactory;
import br.com.market.place.domain.customer.value.*;
import br.com.market.place.domain.payment.entity.CredCard;
import br.com.market.place.domain.payment.repository.PaymentRepository;
import br.com.market.place.domain.payment.value.CardPan;
import br.com.market.place.domain.shared.value.Address;
import br.com.market.place.domain.shared.value.Currency;
import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.hamcrest.Matchers;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static br.com.market.place.domain.customer.constant.DocumentType.CNPJ;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest()
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LegalRepositoryTest {
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private PaymentRepository paymentRepository;

    private CustomerEntityMockFactory factory;

    @BeforeEach
    void setUp() {
        factory = new CustomerEntityMockFactory();
    }


    @Test
    void shouldReturnLegalDataWhenSaveWithSuccess() {
        final var response = repository.saveAndFlush(factory.makeLegalFactory().now());

        assertThat(response.getId(), Matchers.notNullValue());
        assertThat(response.getName(), Matchers.is(new Name("Josefa e Nicole Pizzaria Delivery LTDA")));
        assertThat(response.getFantasyName(), Matchers.is(new Name("Josefa e Nicole")));
        assertThat(response.getDocument(), Matchers.is(new Document("33747249000114", CNPJ)));
        assertThat(response.getMunicipalRegistration(), Matchers.is("807337772144"));
        assertThat(response.getStateRegistration(), Matchers.is("807337772144"));
        assertThat(response.getEmail(), Matchers.is(new Email("financeiro@exemple.com.br")));
        assertThat(response.getTelephone(), Matchers.is(new Telephone("11999982343")));
        assertThat(response.getAddress(), Matchers.is(new Address("London", "Baker Street", "221", "B", "37540232")));
        assertThat(response.getCreateAt().getDate(), LocalDateTimeMatchers.before(LocalDateTime.now()));
        assertThat(response.isLegalPerson(), Matchers.is(true));
        assertThat(response.getUpdateAt(), Matchers.nullValue());
        assertThat(response.getPayments(), Matchers.nullValue());
    }

    @Test
    void shouldReturnUpdateAtWithDateWhenUpdateLegal() {
        var legal = factory.makeLegalFactory();
        repository.saveAndFlush(legal.now());
        legal.withEmail(new Email("any@any.com"));

        repository.saveAndFlush(legal.now());
        var response = repository.findById(legal.now().getId());

        assertThat(response.orElseThrow().getUpdateAt(), Matchers.notNullValue());
    }


    @Test
    void shouldReturnLegalDataWhenFoundByLegalDocumentSavedInTheDatabase() {
        Document document = new Document("33747249000114", CNPJ);
        repository.saveAndFlush(factory.makeLegalFactory().now());

        Optional<Customer> response = repository.findCustomerByDocument(document);
        Optional<Customer> responseNotFound = repository.findCustomerByDocument(new Document("33747249000115", CNPJ));

        assertThat(response.isPresent(), Matchers.is(true));
        assertThat(responseNotFound.isPresent(), Matchers.is(false));
        assertThat(response.get().getDocument(), Matchers.is(document));
    }

    @Test
    void shouldReturnLegalDataWhenFoundByLegalEmailSavedInTheDatabase() {
        Email email = new Email("financeiro@exemple.com.br");
        repository.saveAndFlush(factory.makeLegalFactory().now());

        Optional<Customer> response = repository.findCustomerByEmail(email);
        Optional<Customer> responseNotFound = repository.findCustomerByEmail(new Email("any@not-found.com.br"));
        Legal legal = (Legal) response.orElseThrow();

        assertThat(responseNotFound.isPresent(), Matchers.is(false));
        assertThat(legal.getEmail(), Matchers.is(email));
        assertThat(legal.getFantasyName(), Matchers.is(new Name("Josefa e Nicole")));
    }

    @Test
    void shouldReturnPaymentWhenFoundIt() {
        Legal legal = factory.makeLegalFactory().now();
        Address address = Address.Builder.build().withCity("London").
                withStreet("Baker Street").withNumber("221").
                withComponent("B").withZipCode("37540232").now();

        CredCard credCard = CredCard.Builder.build().
                withStatusPending().
                withCardPan(new CardPan("2720339563597456")).
                withAmount(new Currency("10.0", "BRL")).
                withCustomer(legal).
                withAddress(address).now();

        paymentRepository.saveAndFlush(credCard);

        var response = repository.findCustomerByEmail(legal.getEmail());
        assertThat(response.orElseThrow().getPayments(), Matchers.not(Matchers.empty()));
    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenDocumentIsDuplicated() {
        repository.saveAndFlush(factory.makeLegalFactory().now());

        var exception = assertThrows(DataIntegrityViolationException.class,
                () -> repository.saveAndFlush(factory.makeLegalFactory().now()));
        var cause = (ConstraintViolationException) exception.getCause();

        assertThat(cause.getErrorMessage(), Matchers.containsString("33747249000114"));
    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenEmailIsDuplicated() {
        repository.saveAndFlush(factory.makeLegalFactory().withCNPJ("33.747.249/0001-16").now());

        var exception = assertThrows(DataIntegrityViolationException.class, () ->
                repository.saveAndFlush(factory.makeLegalFactory().now()));
        var cause = (ConstraintViolationException) exception.getCause();

        assertThat(cause.getErrorMessage(), Matchers.containsString("financeiro@exemple.com.br"));
    }


}