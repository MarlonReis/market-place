package br.com.market.place.domain.customer.service;

import br.com.market.place.domain.customer.boundary.CreateLegalInputBoundary;
import br.com.market.place.domain.customer.boundary.ReadLegalCustomerOutputBoundary;
import br.com.market.place.domain.customer.boundary.UpdateLegalInputBoundary;

public interface LegalCustomerService {
    void createLegal(CreateLegalInputBoundary data);
    void updateLegal(UpdateLegalInputBoundary data);
    ReadLegalCustomerOutputBoundary findLegalCustomerByCNPJ(String cnpj);
    ReadLegalCustomerOutputBoundary findLegalCustomerByEmail(String email);
}
