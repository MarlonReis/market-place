package br.com.market.place.infrastructure.service;

import br.com.market.place.domain.shared.boundary.AddressInputBoundary;
import br.com.market.place.domain.customer.boundary.physical.CreatePhysicalInputBoundary;
import br.com.market.place.domain.customer.boundary.physical.UpdatePhysicalInputBoundary;
import br.com.market.place.domain.customer.entity.Physical;
import br.com.market.place.domain.customer.repository.CustomerRepository;
import br.com.market.place.domain.customer.service.PhysicalCustomerService;
import br.com.market.place.domain.customer.value.BirthDate;
import br.com.market.place.domain.customer.value.Email;
import br.com.market.place.domain.shared.exception.CreateException;
import br.com.market.place.domain.shared.exception.InvalidDataException;
import br.com.market.place.domain.shared.exception.NotFoundException;
import br.com.market.place.domain.shared.exception.UpdateException;
import br.com.market.place.infrastructure.service.customer.PhysicalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest()
@ActiveProfiles("test")
@Import(PhysicalService.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PhysicalServiceTest {

    @SpyBean
    private CustomerRepository repository;

    @Autowired
    private PhysicalCustomerService service;

    private CreatePhysicalInputBoundary inputBoundary;

    private AddressInputBoundary addressBoundary;

    @BeforeEach
    void setUp() {
        addressBoundary = new AddressInputBoundary("Santa Rita do Sapucai", "Joaquim Teles de Souza", "80", "APT 102", "37540000");
        inputBoundary = new CreatePhysicalInputBoundary(
                "Any Name", "any@email.com", "35999902345",
                "87621295031", "CPF", "31/12/1991",
                addressBoundary
        );
    }

    @Test
    void shouldCreatePhysicalCustomer() {
        ArgumentCaptor<Physical> argumentCaptor = ArgumentCaptor.forClass(Physical.class);
        service.create(inputBoundary);

        Mockito.verify(repository).saveAndFlush(argumentCaptor.capture());
        Physical physical = argumentCaptor.getValue();

        assertThat(physical.getId(), Matchers.notNullValue());
        assertThat(physical.getBirthDate(), Matchers.is(new BirthDate("31/12/1991")));
        assertThat(physical.getCreateAt(), Matchers.notNullValue());
        assertThat(physical.getUpdateAt(), Matchers.nullValue());
    }

    @Test
    void shouldThrowCreateExceptionWhenDocumentOrEmailIsDuplicated() {
        service.create(inputBoundary);
        var exception = assertThrows(CreateException.class, () -> service.create(inputBoundary));
        assertThat(exception.getMessage(), Matchers.is("Document and email needs to be unique!"));
    }

    @Test
    void shouldUpdateCustomerWhenFindById() {
        ArgumentCaptor<Physical> argumentCaptor = ArgumentCaptor.forClass(Physical.class);

        UUID customerId = UUID.fromString("ea5d9369-9111-45d6-bfec-96f3f6217197");
        final var address = new AddressInputBoundary("Any City", "Any Street", "80", "Any Component", "37540000");
        try (MockedStatic<UUID> mocked = Mockito.mockStatic(UUID.class)) {
            mocked.when(UUID::randomUUID).thenReturn(customerId);
            service.create(inputBoundary);
        }

        service.update(new UpdatePhysicalInputBoundary(
                "ea5d9369-9111-45d6-bfec-96f3f6217197",
                "any@change.com", "3499903945", address));

        Mockito.verify(repository, Mockito.times(2)).saveAndFlush(argumentCaptor.capture());
        Physical physical = argumentCaptor.getValue();

        assertThat(physical.getUpdateAt(), Matchers.notNullValue());
        assertThat(physical.getEmail(), Matchers.is(new Email("any@change.com")));
    }

    @Test
    void shouldThrowUpdateExceptionWhenEmailIsDuplicated() {
        UUID customerId = UUID.fromString("ea5d9369-9111-45d6-bfec-96f3f6217197");
        final var address = new AddressInputBoundary("Any City", "Any Street", "80", "Any Component", "37540000");
        try (MockedStatic<UUID> mocked = Mockito.mockStatic(UUID.class)) {
            mocked.when(UUID::randomUUID).thenReturn(customerId);
            service.create(inputBoundary);
        }
        service.create(inputBoundary = new CreatePhysicalInputBoundary("Any Name", "any-other@email.com", "35999902345",
                "87621295032", "CPF", "31/12/1991", addressBoundary
        ));

        var exception = assertThrows(UpdateException.class, () -> service.update(new UpdatePhysicalInputBoundary("ea5d9369-9111-45d6-bfec-96f3f6217197", "any-other@email.com", "3499903945", address)));
        assertThat(exception.getMessage(), Matchers.is("Cannot update physical customer!"));
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenEmailIsNull() {
        Mockito.when(repository.findCustomerById(ArgumentMatchers.any())).thenReturn(Optional.of(Physical.Builder.build().now()));

        var exception = assertThrows(InvalidDataException.class, () -> service.update(new UpdatePhysicalInputBoundary(
                "ea5d9369-9111-45d6-bfec-96f3f6217197", null, "3499903945", addressBoundary)));
        assertThat(exception.getMessage(), Matchers.is("Attribute email is required!"));
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenTelephoneIsNull() {
        Mockito.when(repository.findCustomerById(ArgumentMatchers.any())).thenReturn(Optional.of(Physical.Builder.build().now()));

        var exception = assertThrows(InvalidDataException.class, () -> service.update(new UpdatePhysicalInputBoundary(
                "ea5d9369-9111-45d6-bfec-96f3f6217197", "any@email.com", null, addressBoundary)));
        assertThat(exception.getMessage(), Matchers.is("Attribute telephone is required!"));
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenAddressIsNull() {
        Mockito.when(repository.findCustomerById(ArgumentMatchers.any())).thenReturn(Optional.of(Physical.Builder.build().now()));

        AddressInputBoundary addressMock = Mockito.mock(AddressInputBoundary.class);
        Mockito.when(addressMock.toEntity()).thenReturn(null);

        var exception = assertThrows(Exception.class, () -> service.update(new UpdatePhysicalInputBoundary("ea5d9369-9111-45d6-bfec-96f3f6217197", "any@email.com", "3499903945",
                addressMock)));
        assertThat(exception.getMessage(), Matchers.is("Attribute address is required!"));
    }


    @Test
    void shouldReturnPhysicalCustomerWhenDocumentIsValid() {
        service.create(inputBoundary);
        var response = service.findCustomerByDocument("87621295031", "CPF");

        assertThat(response.document(), Matchers.is("87621295031"));
        assertThat(response.email(), Matchers.is("any@email.com"));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNotFoundCustomerByDocument() {
        var exception = assertThrows(NotFoundException.class, () -> service.findCustomerByDocument("87621295031", "CPF"));
        assertThat(exception.getMessage(), Matchers.is("Customer not found by document!"));
    }

    @Test
    void shouldReturnPhysicalCustomerWhenFoundByEmail() {
        service.create(inputBoundary);
        var response = service.findCustomerByEmail("any@email.com");

        assertThat(response.document(), Matchers.is("87621295031"));
        assertThat(response.email(), Matchers.is("any@email.com"));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNotFoundCustomerByEmail() {
        var exception = assertThrows(NotFoundException.class, () -> service.findCustomerByEmail("any@email.com"));
        assertThat(exception.getMessage(), Matchers.is("Customer not found by email!"));
    }

}