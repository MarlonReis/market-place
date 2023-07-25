package br.com.market.place.domain.customer.service;

import br.com.market.place.domain.customer.boundary.physical.CreatePhysicalInputBoundary;
import br.com.market.place.domain.customer.boundary.physical.ReadPhysicalOutputBoundary;
import br.com.market.place.domain.customer.boundary.physical.UpdatePhysicalInputBoundary;

public interface PhysicalCustomerService {

    void create(CreatePhysicalInputBoundary data);
    void update(UpdatePhysicalInputBoundary data);
    ReadPhysicalOutputBoundary findCustomerByDocument(String document,String documentType);
    ReadPhysicalOutputBoundary findCustomerByEmail(String email);
}
