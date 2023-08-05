package br.com.market.place.domain.payment.service;

import br.com.market.place.domain.payment.value.PaymentId;

public interface PaymentService {
    void pay(PaymentId id);

    void cancel(PaymentId id);
}
