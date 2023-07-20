package br.com.market.place.domain.customer.repository;

import br.com.market.place.domain.customer.entity.Physical;
import br.com.market.place.domain.customer.value.*;
import br.com.market.place.domain.shared.value.Address;
import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.hamcrest.Matchers;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static br.com.market.place.domain.customer.value.DocumentType.CNPJ;
import static br.com.market.place.domain.customer.value.DocumentType.CPF;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest()
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PhysicalRepositoryTest {
    @Autowired
    private PhysicalRepository repository;

    @Test
    void shouldReturnPhysicalDataWhenSaveWithSuccess() {
        Physical physical = createPhysicalFactory().now();
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
        repository.saveAndFlush(createPhysicalFactory().now());

        var exception = assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(createPhysicalFactory().now()));
        var cause = (ConstraintViolationException) exception.getCause();

        assertThat(cause.getErrorMessage(), Matchers.containsString("53627187113"));
        assertThat(cause.getErrorCode(), Matchers.is(23505));
    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenEmailIsDuplicated() {
        repository.saveAndFlush(createPhysicalFactory().withDocument("536.271.871-16", CPF).now());

        var exception = assertThrows(DataIntegrityViolationException.class,
                () -> repository.saveAndFlush(createPhysicalFactory().now()));
        var cause = (ConstraintViolationException) exception.getCause();

        assertThat(cause.getErrorMessage(), Matchers.containsString("benedito-araujo91@gmnail.com"));
        assertThat(cause.getErrorCode(), Matchers.is(23505));
    }

    @Test
    void shouldReturnPhysicalDataWhenFoundByDocument() {
        Physical physical = createPhysicalFactory().now();
        repository.saveAndFlush(physical);
        Optional<Physical> response = repository.findPhysicalByDocument(new Document("536.271.871-13", CPF));
        Optional<Physical> responseNotFound = repository.findPhysicalByDocument(new Document("536.271.871-15", CPF));

        assertThat(response.isPresent(), Matchers.is(true));
        assertThat(responseNotFound.isEmpty(), Matchers.is(true));
        assertThat(response.orElseThrow().getDocument(), Matchers.is(new Document("536.271.871-13", CPF)));
    }


    private Physical.Builder createPhysicalFactory() {
        return Physical.Builder.build()
                .withName(new Name("Benedito Caio Araújo"))
                .withDocument("536.271.871-13", CPF)
                .withEmail(new Email("benedito-araujo91@gmnail.com"))
                .withBirthDate(new BirthDate("23/02/2001"))
                .withTelephone(new Telephone("11999982343"))
                .withAddress(Address.Builder.build().withCity("London")
                        .withStreet("Baker Street").withNumber("221")
                        .withComponent("B").withZipCode("37540232").now());
    }

}