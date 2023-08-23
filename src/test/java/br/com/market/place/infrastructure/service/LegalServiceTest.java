package br.com.market.place.infrastructure.service;

import br.com.market.place.domain.shared.boundary.AddressInputBoundary;
import br.com.market.place.domain.customer.boundary.legal.CreateLegalInputBoundary;
import br.com.market.place.domain.customer.boundary.legal.UpdateLegalInputBoundary;
import br.com.market.place.domain.customer.entity.Legal;
import br.com.market.place.domain.customer.repository.CustomerRepository;
import br.com.market.place.domain.customer.service.LegalCustomerService;
import br.com.market.place.domain.customer.value.Email;
import br.com.market.place.domain.customer.value.Telephone;
import br.com.market.place.domain.shared.exception.CreateException;
import br.com.market.place.domain.shared.exception.DomainException;
import br.com.market.place.domain.shared.exception.NotFoundException;
import br.com.market.place.infrastructure.service.customer.LegalService;
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
@Import(LegalService.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LegalServiceTest {
    @Autowired
    private LegalCustomerService service;

    @SpyBean
    private CustomerRepository repository;

    private CreateLegalInputBoundary inputData;

    @BeforeEach
    void setUp() {
        final var address = new AddressInputBoundary("Santa Rita do Sapucai", "Joaquim Teles de Souza", "80", "APT 102", "37540000");
        inputData = new CreateLegalInputBoundary(
                "Any Name", "35999902324", "any@email.com", "37.680.915/0001-03",
                "Any Fantasy Name", "980808080", "9898798798",
                address
        );
    }

    @Test
    void shouldCreateLegalCustomerWithSuccess() {
        ArgumentCaptor<Legal> argumentCaptor = ArgumentCaptor.forClass(Legal.class);

        service.create(inputData);

        Mockito.verify(repository).saveAndFlush(argumentCaptor.capture());
        Legal legal = argumentCaptor.getValue();

        assertThat(legal.getId(), Matchers.notNullValue());
        assertThat(legal.getCreateAt(), Matchers.notNullValue());
        assertThat(legal.getUpdateAt(), Matchers.nullValue());
    }

    @Test
    void shouldThrowCreateExceptionWhenDocumentOrEmailAreDuplicated() {
        service.create(inputData);
        DomainException exception = assertThrows(CreateException.class, () -> service.create(inputData));
        assertThat(exception.getMessage(), Matchers.is("E-mail or document needs to be unique!"));
    }

    @Test
    void shouldUpdateLegalDataWithSuccess() {
        var address = new AddressInputBoundary("Any City", "Any Street", "80", "APT 102", "37540000");
        service.create(inputData);

        ArgumentCaptor<Legal> argumentCaptor = ArgumentCaptor.forClass(Legal.class);
        var data = new UpdateLegalInputBoundary("3499003234", "37.680.915/0001-03", "new-email@faka.com", address);
        service.update(data);

        Mockito.verify(repository, Mockito.atLeastOnce()).saveAndFlush(argumentCaptor.capture());
        Legal legal = argumentCaptor.getValue();

        assertThat(legal.getId(), Matchers.notNullValue());
        assertThat(legal.getCreateAt(), Matchers.notNullValue());
        assertThat(legal.getUpdateAt(), Matchers.notNullValue());
        assertThat(legal.getEmail(), Matchers.is(new Email("new-email@faka.com")));
        assertThat(legal.getTelephone(), Matchers.is(new Telephone("3499003234")));
        assertThat(legal.getAddress().equals(address.toEntity()), Matchers.is(true));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCustomerNotFoundByDocument() {
        var address = new AddressInputBoundary("Any City", "Any Street", "80", "APT 102", "37540000");
        var data = new UpdateLegalInputBoundary("3499003234", "37.680.915/0001-03", "new-email@faka.com", address);

        var exception = assertThrows(NotFoundException.class, () -> service.update(data));
        assertThat(exception.getMessage(), Matchers.is("Customer not found by CNPJ!"));

        Mockito.verify(repository, Mockito.never()).saveAndFlush(ArgumentMatchers.any());
    }

    @Test
    void shouldReturnLegalCustomerWhenFoundByDocument() {
        service.create(inputData);
        var response = service.findLegalCustomerByCNPJ("37.680.915/0001-03");

        assertThat(response.customerId(), Matchers.notNullValue());
        assertThat(response.address(), Matchers.notNullValue());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNotFoundByDocument() {
        var exception = assertThrows(NotFoundException.class, () -> service.findLegalCustomerByCNPJ("37.680.915/0001-03"));
        assertThat(exception.getMessage(), Matchers.is("Customer not found!"));
    }

    @Test
    void shouldReturnLegalCustomerWhenFoundByEmail() {
        service.create(inputData);
        var response = service.findLegalCustomerByEmail("any@email.com");

        assertThat(response.customerId(), Matchers.notNullValue());
        assertThat(response.address(), Matchers.notNullValue());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNotFoundByEmail() {
        var exception = assertThrows(NotFoundException.class, () -> service.findLegalCustomerByEmail("any@email.com"));
        assertThat(exception.getMessage(), Matchers.is("Customer not found!"));
    }

}