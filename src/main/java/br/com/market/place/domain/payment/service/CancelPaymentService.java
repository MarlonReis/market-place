package br.com.market.place.domain.payment.service;

import br.com.market.place.domain.payment.constant.PaymentStatus;
import br.com.market.place.domain.payment.entity.Payment;

public interface CancelPaymentService {

    PaymentStatus cancelPayment(Payment payment);
}
