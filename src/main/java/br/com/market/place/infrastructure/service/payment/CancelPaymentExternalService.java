package br.com.market.place.infrastructure.service.payment;

import br.com.market.place.domain.payment.constant.PaymentStatus;
import br.com.market.place.domain.payment.entity.Payment;
import br.com.market.place.domain.payment.service.CancelPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CancelPaymentExternalService implements CancelPaymentService {
    private final Logger logger = LoggerFactory.getLogger(CancelPaymentExternalService.class);

    @Override
    public PaymentStatus cancelPayment(Payment payment) {
        logger.info("Cancel payment: {}", payment);
        return switch (payment.getStatus()) {
            case PAID_OUT -> PaymentStatus.REVERSED;
            case PENDING -> PaymentStatus.CANCELED;
            default -> PaymentStatus.FAIL;
        };
    }
}
