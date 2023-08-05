package br.com.market.place.domain.payment.service;

import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.payment.boundary.CreateCardInputBoundary;

public interface CredCardPaymentService {
    void create(CustomerId id, CreateCardInputBoundary data);


}
