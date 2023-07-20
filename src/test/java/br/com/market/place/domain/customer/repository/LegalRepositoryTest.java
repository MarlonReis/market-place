package br.com.market.place.domain.customer.repository;

import br.com.market.place.domain.customer.entity.Legal;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest()
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LegalRepositoryTest {
    @Autowired
    private LegalRepository repository;


    @Test
    void shouldReturnLegalDataWhenSaveWithSuccess() {
        final var response = repository.saveAndFlush(legalFactory().now());

        assertThat(response.getId(), Matchers.notNullValue());
        assertThat(response.getName(), Matchers.is(new Name("Josefa e Nicole Pizzaria Delivery LTDA")));
        assertThat(response.getFantasyName(), Matchers.is(new Name("Josefa e Nicole")));
        assertThat(response.getDocument(), Matchers.is(new Document("33747249000114", CNPJ)));
        assertThat(response.getMunicipalRegistration(), Matchers.is("807337772144"));
        assertThat(response.getStateRegistration(), Matchers.is("807337772144"));
        assertThat(response.getEmail(), Matchers.is(new Email("financeiro@josefaenicolepizzariadeliveryltda.com.br")));
        assertThat(response.getTelephone(), Matchers.is(new Telephone("11999982343")));
        assertThat(response.getAddress(), Matchers.is(new Address("London", "Baker Street", "221", "B", "37540232")));
        assertThat(response.getCreateAt().getDate(), LocalDateTimeMatchers.before(LocalDateTime.now()));
        assertThat(response.isLegalPerson(), Matchers.is(true));
        assertThat(response.getUpdateAt(), Matchers.nullValue());
    }

    @Test
    void shouldReturnUpdateAtWithDateWhenUpdateLegal() {
        var legal = legalFactory();
        repository.saveAndFlush(legal.now());
        legal.withEmail(new Email("any@any.com"));

        repository.saveAndFlush(legal.now());
        var response = repository.findById(legal.now().getId());

        assertThat(response.orElseThrow().getUpdateAt(), Matchers.notNullValue());
    }


    @Test
    void shouldReturnLegalDataWhenFoundByLegalDocumentSavedInTheDatabase() {
        Document document = new Document("33747249000114", CNPJ);
        repository.saveAndFlush(legalFactory().now());

        Optional<Legal> response = repository.findByDocument(document);
        Optional<Legal> responseNotFound = repository.findByDocument(new Document("33747249000115", CNPJ));

        assertThat(response.isPresent(), Matchers.is(true));
        assertThat(responseNotFound.isPresent(), Matchers.is(false));
        assertThat(response.get().getDocument(), Matchers.is(document));
    }

    @Test
    void shouldReturnLegalDataWhenFoundByLegalEmailSavedInTheDatabase() {
        Email email = new Email("financeiro@josefaenicolepizzariadeliveryltda.com.br");
        repository.saveAndFlush(legalFactory().now());

        Optional<Legal> response = repository.findByEmail(email);
        Optional<Legal> responseNotFound = repository.findByEmail(new Email("any@not-found.com.br"));

        assertThat(response.isPresent(), Matchers.is(true));
        assertThat(responseNotFound.isPresent(), Matchers.is(false));
        assertThat(response.get().getEmail(), Matchers.is(email));
    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenDocumentIsDuplicated() {
        repository.saveAndFlush(legalFactory().now());

        var exception = assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(legalFactory().now()));
        var cause = (ConstraintViolationException) exception.getCause();

        assertThat(cause.getErrorMessage(), Matchers.containsString("33747249000114"));
        assertThat(cause.getErrorCode(), Matchers.is(23505));
    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenEmailIsDuplicated() {
        repository.saveAndFlush(legalFactory().withCNPJ("33.747.249/0001-16").now());

        var exception = assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(legalFactory().now()));
        var cause = (ConstraintViolationException) exception.getCause();

        assertThat(cause.getErrorMessage(), Matchers.containsString("financeiro@josefaenicolepizzariadeliveryltda.com.br"));
        assertThat(cause.getErrorCode(), Matchers.is(23505));
    }

    private Legal.Builder legalFactory() {
        return Legal.Builder.build()
                .withName(new Name("Josefa e Nicole Pizzaria Delivery LTDA"))
                .withFantasyName(new Name("Josefa e Nicole"))
                .withCNPJ("33.747.249/0001-14")
                .withMunicipalRegistration("807337772144")
                .withStateRegistration("807337772144")
                .withEmail(new Email("financeiro@josefaenicolepizzariadeliveryltda.com.br"))
                .withTelephone(new Telephone("11999982343"))
                .withAddress(Address.Builder.build().withCity("London")
                        .withStreet("Baker Street").withNumber("221")
                        .withComponent("B").withZipCode("37540232").now());
    }

}