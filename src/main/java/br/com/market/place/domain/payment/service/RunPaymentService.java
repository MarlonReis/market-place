package br.com.market.place.domain.payment.service;

import br.com.market.place.domain.payment.constant.PaymentStatus;

public interface RunPaymentService {
    PaymentStatus executePayment();
}
