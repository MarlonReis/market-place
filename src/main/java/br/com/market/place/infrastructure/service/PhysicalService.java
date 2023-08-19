package br.com.market.place.infrastructure.service;

import br.com.market.place.domain.customer.boundary.physical.CreatePhysicalInputBoundary;
import br.com.market.place.domain.customer.boundary.physical.ReadPhysicalOutputBoundary;
import br.com.market.place.domain.customer.boundary.physical.UpdatePhysicalInputBoundary;
import br.com.market.place.domain.customer.constant.DocumentType;
import br.com.market.place.domain.customer.entity.Physical;
import br.com.market.place.domain.customer.repository.CustomerRepository;
import br.com.market.place.domain.customer.service.PhysicalCustomerService;
import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.customer.value.Document;
import br.com.market.place.domain.customer.value.Email;
import br.com.market.place.domain.shared.exception.CreateException;
import br.com.market.place.domain.shared.exception.NotFoundException;
import br.com.market.place.domain.shared.exception.UpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class PhysicalService implements PhysicalCustomerService {
    private final Logger logger = LoggerFactory.getLogger(PhysicalService.class);
    private final CustomerRepository repository;

    @Autowired
    public PhysicalService(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void create(CreatePhysicalInputBoundary data) {
        try {
            repository.saveAndFlush(data.toEntity());
            logger.info("Create physical customer!");
        } catch (DataIntegrityViolationException ex) {
            logger.error("Error when create physical customer: {}", ex.getMessage());
            throw new CreateException("Document and email needs to be unique!");
        }
    }

    @Override
    public void update(UpdatePhysicalInputBoundary data) {
        CustomerId customerId = new CustomerId(data.customerId());
        final var response = repository.findCustomerById(customerId);
        try {
            response.map(cap -> (Physical) cap).ifPresent(physical -> {
                logger.info("Update physical customer by id {}", data.customerId());
                physical.changeAddress(data.address().toEntity());
                physical.changeTelephone(data.telephone());
                physical.changeEmail(data.email());
                repository.saveAndFlush(physical);
            });
        } catch (DataIntegrityViolationException ex) {
            logger.error("Error when update physical customer by id {}, error: {}", data.customerId(), ex.getMessage());
            throw new UpdateException("Cannot update physical customer!");
        }
        if (response.isEmpty()) throw new NotFoundException("Cannot found customer by id!");
    }

    @Override
    public ReadPhysicalOutputBoundary findCustomerByDocument(String document, String type) {
        return repository.findCustomerByDocument(new Document(document, DocumentType.physicalType(type)))
                .map(customer -> (Physical) customer).map(Physical::toReadPhysicalOutputBoundary)
                .orElseThrow(() -> new NotFoundException("Customer not found by document!"));
    }

    @Override
    public ReadPhysicalOutputBoundary findCustomerByEmail(String email) {
        return repository.findCustomerByEmail(new Email(email))
                .map(customer -> (Physical) customer).map(Physical::toReadPhysicalOutputBoundary)
                .orElseThrow(() -> new NotFoundException("Customer not found by email!"));
    }
}
