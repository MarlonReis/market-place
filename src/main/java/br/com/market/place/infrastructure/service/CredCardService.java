package br.com.market.place.infrastructure.service;

import br.com.market.place.domain.customer.entity.Customer;
import br.com.market.place.domain.customer.repository.CustomerRepository;
import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.payment.boundary.CreateCardInputBoundary;
import br.com.market.place.domain.payment.boundary.ReadCredCardOutputBoundary;
import br.com.market.place.domain.payment.entity.CredCard;
import br.com.market.place.domain.payment.repository.PaymentRepository;
import br.com.market.place.domain.payment.service.CredCardPaymentService;
import br.com.market.place.domain.payment.value.CardPan;
import br.com.market.place.domain.shared.exception.CreateException;
import br.com.market.place.domain.shared.exception.NotFoundException;
import br.com.market.place.domain.shared.value.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class CredCardService implements CredCardPaymentService {
    private final Logger logger = LoggerFactory.getLogger(CredCardService.class);
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public CredCardService(CustomerRepository customerRepo, PaymentRepository paymentRepo) {
        this.customerRepository = customerRepo;
        this.paymentRepository = paymentRepo;
    }

    @Override
    public void create(CustomerId id, CreateCardInputBoundary data) {
        Customer customer = customerRepository.findCustomerById(id)
                .orElseThrow(() -> new NotFoundException("Cannot be possible to find customer by id"));
        var credCard = CredCard.Builder.build()
                .withCustomer(customer)
                .withAddress(data.address().toEntity())
                .withCardPan(new CardPan(data.cardPan()))
                .withAmount(new Currency(data.amount(), data.currencyType()))
                .withStatusPending()
                .now();
        try {
            paymentRepository.saveAndFlush(credCard);
        } catch (Exception ex) {
            logger.error("Error when crate credCard payment: Error: {}", ex.getMessage());
            throw new CreateException("Cannot to be create credCard payment!");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Set<ReadCredCardOutputBoundary> findCredCardPaymentByCustomerId(CustomerId id) {
        var response = paymentRepository.findCredCardByCustomerId(id).map(CredCard::toReadCredCardOutputBoundary).toSet();

        if (response.isEmpty()){
            throw new NotFoundException("Cannot be possible to find cred card payment by customer!");
        }
        return response;
    }
}
