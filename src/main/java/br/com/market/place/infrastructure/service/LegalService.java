package br.com.market.place.infrastructure.service;

import br.com.market.place.domain.customer.boundary.CreateLegalInputBoundary;
import br.com.market.place.domain.customer.boundary.ReadAddressOutputBoundary;
import br.com.market.place.domain.customer.boundary.ReadLegalCustomerOutputBoundary;
import br.com.market.place.domain.customer.boundary.UpdateLegalInputBoundary;
import br.com.market.place.domain.customer.constant.DocumentType;
import br.com.market.place.domain.customer.entity.Customer;
import br.com.market.place.domain.customer.entity.Legal;
import br.com.market.place.domain.customer.repository.CustomerRepository;
import br.com.market.place.domain.customer.service.LegalCustomerService;
import br.com.market.place.domain.customer.value.Document;
import br.com.market.place.domain.customer.value.Email;
import br.com.market.place.domain.customer.value.Telephone;
import br.com.market.place.domain.shared.exception.CreateException;
import br.com.market.place.domain.shared.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LegalService implements LegalCustomerService {
    private final Logger logger = LoggerFactory.getLogger(LegalService.class);
    private final CustomerRepository repository;

    @Autowired
    public LegalService(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createLegal(CreateLegalInputBoundary data) {
        try {
            repository.saveAndFlush(data.toEntity());
            logger.info("Create a new legal customer!");
        } catch (DataIntegrityViolationException ex) {
            logger.error("Error when try create customer {}", ex.getMessage());
            throw new CreateException("E-mail or document need to be unique!");
        }
    }

    @Override
    public void updateLegal(UpdateLegalInputBoundary data) {
        Optional<Legal> response = repository.findCustomerByDocument(new Document(data.cnpj(), DocumentType.CNPJ))
                .map(customer -> (Legal) customer);

        response.ifPresent(legal -> {
            logger.info("Update legal customer with document equals {}", legal.getId().getId());
            legal.changeEmail(data.email());
            legal.changeAddress(data.address().toAddress());
            legal.changeTelephone(data.telephone());
            repository.saveAndFlush(legal);
        });

        if (response.isEmpty()) {
            logger.warn("Customer not found by document {}", data.cnpj());
            throw new NotFoundException("Customer not found by CNPJ!");
        }
    }

    @Override
    public ReadLegalCustomerOutputBoundary findLegalCustomerByCNPJ(String cnpj) {
        return repository.findCustomerByDocument(new Document(cnpj, DocumentType.CNPJ))
                .map(customer -> (Legal) customer)
                .map(Legal::toReadLegalCustomerOutputBoundary)
                .orElseThrow(() -> new NotFoundException("Customer not found!"));

    }

    @Override
    public ReadLegalCustomerOutputBoundary findLegalCustomerByEmail(String email) {
        return repository.findCustomerByEmail(new Email(email))
                .map(customer -> (Legal) customer)
                .map(Legal::toReadLegalCustomerOutputBoundary)
                .orElseThrow(() -> new NotFoundException("Customer not found!"));
    }
}
