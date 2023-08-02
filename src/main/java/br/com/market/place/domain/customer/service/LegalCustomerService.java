package br.com.market.place.domain.customer.service;

import br.com.market.place.domain.customer.boundary.legal.CreateLegalInputBoundary;
import br.com.market.place.domain.customer.boundary.legal.ReadLegalCustomerOutputBoundary;
import br.com.market.place.domain.customer.boundary.legal.UpdateLegalInputBoundary;

public interface LegalCustomerService {
    void create(CreateLegalInputBoundary data);
    void update(UpdateLegalInputBoundary data);
    ReadLegalCustomerOutputBoundary findLegalCustomerByCNPJ(String cnpj);
    ReadLegalCustomerOutputBoundary findLegalCustomerByEmail(String email);
}
