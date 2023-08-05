package br.com.market.place.infrastructure.service;

import br.com.market.place.domain.customer.entity.Legal;
import br.com.market.place.domain.customer.factory.CustomerEntityMockFactory;
import br.com.market.place.domain.customer.repository.CustomerRepository;
import br.com.market.place.domain.payment.boundary.CreateBilletInputBoundary;
import br.com.market.place.domain.payment.entity.Billet;
import br.com.market.place.domain.payment.repository.PaymentRepository;
import br.com.market.place.domain.payment.service.BilletPaymentService;
import br.com.market.place.domain.shared.boundary.AddressInputBoundary;
import br.com.market.place.domain.shared.exception.CreateException;
import br.com.market.place.domain.shared.exception.NotFoundException;
import br.com.market.place.domain.shared.value.Address;
import br.com.market.place.domain.shared.value.Currency;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest()
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Import({BilletService.class, PayLineExternalGenerateService.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BilletServiceTest {
    @Autowired
    private BilletPaymentService service;

    @SpyBean
    private PaymentRepository paymentRepository;

    @SpyBean
    private CustomerRepository customerRepository;

    private CreateBilletInputBoundary inputBoundary;

    private Legal legal;

    @BeforeEach
    void setUp() {
        CustomerEntityMockFactory mockFactory = new CustomerEntityMockFactory();
        legal = mockFactory.makeLegalFactory().now();

        Address address = legal.getAddress();
        inputBoundary = new CreateBilletInputBoundary("500.00", "USD",
                new AddressInputBoundary(address.city(), address.street(), address.number(), address.component(), address.zipCode()));

    }

    @Test
    void shouldSaveBilletWhenSuccess() {
        customerRepository.saveAndFlush(legal);
        service.create(legal.getId(), inputBoundary);

        ArgumentCaptor<Billet> argumentCaptor = ArgumentCaptor.forClass(Billet.class);
        Mockito.verify(paymentRepository).saveAndFlush(argumentCaptor.capture());

        var arg = argumentCaptor.getValue();
        assertThat(arg.getPayLine(), Matchers.notNullValue());
        assertThat(arg.getAmount(), Matchers.is(new Currency("500.00", "USD")));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNotFoundCustomer() {
        var exception = assertThrows(NotFoundException.class, () -> service.create(legal.getId(), inputBoundary));
        assertThat(exception.getData().message(), Matchers.is("Customer not found!"));
    }

    @Test
    void shouldThrowCreateExceptionWhenCannotCreatePaymentRegister() {
        Mockito.when(customerRepository.findCustomerById(legal.getId())).thenReturn(Optional.of(legal));
        Mockito.doThrow(RuntimeException.class).when(paymentRepository).saveAndFlush(ArgumentMatchers.any(Billet.class));

        var exception = assertThrows(CreateException.class, () -> service.create(legal.getId(), inputBoundary));
        assertThat(exception.getData().message(), Matchers.is("Cannot be possible to save billet payment!"));
    }

    @Test
    void shouldReturnBilletsWhenFoundByCustomerId() {
        customerRepository.saveAndFlush(legal);
        service.create(legal.getId(), inputBoundary);

        var response = service.findBilletByCustomerId(legal.getId());
        assertThat(response, Matchers.hasSize(1));
    }


}