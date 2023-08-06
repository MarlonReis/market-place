package br.com.market.place.domain.payment.service;

import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.payment.boundary.CreateCardInputBoundary;
import br.com.market.place.domain.payment.boundary.ReadCredCardOutputBoundary;

import java.util.Set;

public interface CredCardPaymentService {
    void create(CustomerId id, CreateCardInputBoundary data);

    Set<ReadCredCardOutputBoundary> findCredCardPaymentByCustomerId(CustomerId id);
}
