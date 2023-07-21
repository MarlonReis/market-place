package br.com.market.place.domain.payment.service;

import br.com.market.place.domain.payment.constant.PaymentStatus;

public interface PaymentService {
    PaymentStatus executePayment();
    PaymentStatus cancelPayment();
}
