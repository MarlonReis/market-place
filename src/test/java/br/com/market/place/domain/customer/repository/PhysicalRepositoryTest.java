package br.com.market.place.domain.customer.repository;

import br.com.market.place.domain.customer.entity.Customer;
import br.com.market.place.domain.customer.entity.Physical;
import br.com.market.place.factory.CustomerEntityMockFactory;
import br.com.market.place.domain.customer.value.*;
import br.com.market.place.domain.shared.value.Address;
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

import static br.com.market.place.domain.customer.constant.DocumentType.CPF;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest()
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PhysicalRepositoryTest {
    @Autowired
    private CustomerRepository repository;

    private CustomerEntityMockFactory factory;

    @BeforeEach
    void setUp(){
        factory = new CustomerEntityMockFactory();
    }


    @Test
    void shouldReturnPhysicalDataWhenSaveWithSuccess() {
        Physical physical = factory.makePhysicalFactory().now();
        var response = repository.saveAndFlush(physical);

        assertThat(response.getId(), Matchers.notNullValue());
        assertThat(response.getName(), Matchers.is(new Name("Benedito Caio Araújo")));
        assertThat(response.getBirthDate(), Matchers.is(new BirthDate("23/02/2001")));
        assertThat(response.getDocument(), Matchers.is(new Document("536.271.871-13", CPF)));
        assertThat(response.getEmail(), Matchers.is(new Email("benedito-araujo91@gmnail.com")));
        assertThat(response.getTelephone(), Matchers.is(new Telephone("11999982343")));
        assertThat(response.getAddress(), Matchers.is(new Address("London", "Baker Street", "221", "B", "37540232")));
        assertThat(response.getCreateAt().getDate(), LocalDateTimeMatchers.before(LocalDateTime.now()));
        assertThat(response.isLegalPerson(), Matchers.is(false));
        assertThat(response.getUpdateAt(), Matchers.nullValue());
    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenDocumentIsDuplicated() {
        repository.saveAndFlush(factory.makePhysicalFactory().now());

        var exception = assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(factory.makePhysicalFactory().now()));
        var cause = (ConstraintViolationException) exception.getCause();

        assertThat(cause.getErrorMessage(), Matchers.containsString("53627187113"));

    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenEmailIsDuplicated() {
        repository.saveAndFlush(factory.makePhysicalFactory().withDocument("536.271.871-16", CPF).now());

        var exception = assertThrows(DataIntegrityViolationException.class,
                () -> repository.saveAndFlush(factory.makePhysicalFactory().now()));
        var cause = (ConstraintViolationException) exception.getCause();

        assertThat(cause.getErrorMessage(), Matchers.containsString("benedito-araujo91@gmnail.com"));
    }

    @Test
    void shouldReturnPhysicalDataWhenFoundByDocument() {
        repository.saveAndFlush(factory.makePhysicalFactory().now());
        Optional<Customer> response = repository.findCustomerByDocument(new Document("536.271.871-13", CPF));
        Optional<Customer> responseNotFound = repository.findCustomerByDocument(new Document("536.271.871-15", CPF));

        Physical physical = (Physical) response.orElseThrow();

        assertThat(responseNotFound.isEmpty(), Matchers.is(true));
        assertThat(physical.getDocument(), Matchers.is(new Document("536.271.871-13", CPF)));
        assertThat(physical.getBirthDate(), Matchers.is(new BirthDate("23/02/2001")));
    }




}