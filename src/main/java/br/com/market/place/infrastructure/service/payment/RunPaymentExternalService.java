package br.com.market.place.infrastructure.service.payment;

import br.com.market.place.domain.payment.constant.PaymentStatus;
import br.com.market.place.domain.payment.entity.Payment;
import br.com.market.place.domain.payment.service.RunPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RunPaymentExternalService implements RunPaymentService {
    private final Logger logger = LoggerFactory.getLogger(RunPaymentExternalService.class);

    @Override
    public PaymentStatus executePayment(Payment payment) {
        logger.info("Run payment: {}", payment);
        return PaymentStatus.PAID_OUT;
    }
}
