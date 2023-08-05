package br.com.market.place.infrastructure.service;

import br.com.market.place.domain.customer.entity.Customer;
import br.com.market.place.domain.customer.repository.CustomerRepository;
import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.payment.boundary.CreateBilletInputBoundary;
import br.com.market.place.domain.payment.boundary.ReadBilletOutputBoundary;
import br.com.market.place.domain.payment.entity.Billet;
import br.com.market.place.domain.payment.repository.PaymentRepository;
import br.com.market.place.domain.payment.service.BilletPaymentService;
import br.com.market.place.domain.payment.service.PayLineService;
import br.com.market.place.domain.shared.exception.CreateException;
import br.com.market.place.domain.shared.exception.NotFoundException;
import br.com.market.place.domain.shared.value.Currency;
import br.com.market.place.domain.shared.value.DueDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BilletService implements BilletPaymentService {
    private final Logger logger = LoggerFactory.getLogger(BilletService.class);
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final PayLineService payLineService;

    @Autowired
    public BilletService(PaymentRepository paymentRepository, CustomerRepository customerRepository, PayLineService payLineService) {
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
        this.payLineService = payLineService;
    }

    @Override
    public void create(CustomerId id, CreateBilletInputBoundary data) {
        logger.info("Create billet payment!");
        Customer customer = customerRepository.findCustomerById(id).
                orElseThrow(() -> new NotFoundException("Customer not found!"));

        String payLine = payLineService.payLineGenerate(data);
        try {
            final var billet = Billet.Builder.build().withAddress(data.address().toEntity())
                    .withAmount(new Currency(data.amount(), data.currencyType()))
                    .withCustomer(customer).withPayLine(payLine)
                    .withDueDateExpireInDays(3).withPendingStatus().now();
            paymentRepository.saveAndFlush(billet);
        } catch (Exception ex) {
            logger.error("Error when save billet payment! {}", ex.getMessage());
            throw new CreateException("Cannot be possible to save billet payment!");
        }

    }

    @Override
    public Set<ReadBilletOutputBoundary> findBilletByCustomerId(CustomerId customerId) {
        return paymentRepository.findPaymentByCustomerId(customerId)
                .stream().map(billet -> (Billet) billet)
                .map(Billet::toReadBilletOutputBoundary)
                .collect(Collectors.toUnmodifiableSet());
    }

}
