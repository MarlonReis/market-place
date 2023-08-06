package br.com.market.place.infrastructure.service;

import br.com.market.place.domain.customer.entity.Legal;
import br.com.market.place.domain.customer.factory.CustomerEntityMockFactory;
import br.com.market.place.domain.customer.repository.CustomerRepository;
import br.com.market.place.domain.payment.boundary.CreateCardInputBoundary;
import br.com.market.place.domain.payment.entity.CredCard;
import br.com.market.place.domain.payment.repository.PaymentRepository;
import br.com.market.place.domain.payment.service.CredCardPaymentService;
import br.com.market.place.domain.payment.value.CardPan;
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
@Import({CredCardService.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CredCardServiceTest {
    @Autowired
    private CredCardPaymentService service;

    @SpyBean
    private CustomerRepository customerRepository;

    @SpyBean
    private PaymentRepository paymentRepository;

    private Legal legal;
    private CreateCardInputBoundary inputBoundary;

    @BeforeEach
    void setUp() {
        CustomerEntityMockFactory mockFactory = new CustomerEntityMockFactory();
        legal = mockFactory.makeLegalFactory().now();

        Address address = legal.getAddress();
        inputBoundary = new CreateCardInputBoundary(
                "5273548390118161", "10.0", "BRL",
                new AddressInputBoundary(address.city(), address.street(),
                        address.number(), address.component(),
                        address.zipCode()));
    }

    @Test
    void shouldCreateCredPayment() {
        customerRepository.saveAndFlush(legal);

        service.create(legal.getId(), inputBoundary);

        ArgumentCaptor<CredCard> argument = ArgumentCaptor.forClass(CredCard.class);
        Mockito.verify(paymentRepository).saveAndFlush(argument.capture());
        CredCard credCard = argument.getValue();

        assertThat(credCard.getId(), Matchers.notNullValue());
        assertThat(credCard.getCreateAt(), Matchers.notNullValue());
        assertThat(credCard.getCardPan(), Matchers.is(new CardPan("5273548390118161")));
        assertThat(credCard.getAmount(), Matchers.is(new Currency("10.0", "BRL")));
    }

    @Test
    void shouldThrowsNotFoundExceptionWhenNotFoundCustomerById() {
        var exception = assertThrows(NotFoundException.class, () -> service.create(legal.getId(), inputBoundary));
        assertThat(exception.getData().message(), Matchers.is("Cannot be possible to find customer by id"));
    }

    @Test
    void shouldThrowsCreateExceptionWhenRepositoryThrowExceptions() {
        Mockito.when(customerRepository.findCustomerById(legal.getId())).thenReturn(Optional.of(legal));
        Mockito.doThrow(RuntimeException.class).when(paymentRepository).saveAndFlush(ArgumentMatchers.any());

        var exception = assertThrows(CreateException.class, () -> service.create(legal.getId(), inputBoundary));
        assertThat(exception.getData().message(), Matchers.is("Cannot to be create credCard payment!"));
    }

}