package br.com.market.place.domain.payment.service;

import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.payment.boundary.CreateBilletInputBoundary;
import br.com.market.place.domain.payment.boundary.ReadBilletOutputBoundary;

import java.util.Set;

public interface BilletPaymentService {
    void create(CustomerId id, CreateBilletInputBoundary data);

    Set<ReadBilletOutputBoundary> findBilletByCustomerId(CustomerId customerId);
}
