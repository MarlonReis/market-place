package br.com.market.place.infrastructure.service.payment;

import br.com.market.place.domain.customer.entity.Customer;
import br.com.market.place.domain.customer.repository.CustomerRepository;
import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.payment.boundary.CreateCardInputBoundary;
import br.com.market.place.domain.payment.boundary.ReadCredCardOutputBoundary;
import br.com.market.place.domain.payment.entity.CreditCard;
import br.com.market.place.domain.payment.repository.PaymentRepository;
import br.com.market.place.domain.payment.service.CreditCardPaymentService;
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
public class CreditCardService implements CreditCardPaymentService {
    private final Logger logger = LoggerFactory.getLogger(CreditCardService.class);
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public CreditCardService(CustomerRepository customerRepo, PaymentRepository paymentRepo) {
        this.customerRepository = customerRepo;
        this.paymentRepository = paymentRepo;
    }

    @Override
    public void create(CustomerId id, CreateCardInputBoundary data) {
        Customer customer = customerRepository.findCustomerById(id)
                .orElseThrow(() -> new NotFoundException("It wasn't possible to find customer by id!"));
        var credCard = CreditCard.Builder.build()
                .withCustomer(customer)
                .withAddress(data.address().toEntity())
                .withCardPan(new CardPan(data.cardNumber()))
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
        var response = paymentRepository.findCredCardByCustomerId(id).map(CreditCard::toReadCredCardOutputBoundary).toSet();

        if (response.isEmpty()){
            throw new NotFoundException("It wasn't possible to find credit card payment by customer!");
        }
        return response;
    }
}
