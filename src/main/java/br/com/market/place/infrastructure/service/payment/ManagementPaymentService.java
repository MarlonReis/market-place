package br.com.market.place.infrastructure.service.payment;

import br.com.market.place.domain.payment.entity.Payment;
import br.com.market.place.domain.payment.repository.PaymentRepository;
import br.com.market.place.domain.payment.service.CancelPaymentService;
import br.com.market.place.domain.payment.service.PaymentService;
import br.com.market.place.domain.payment.service.RunPaymentService;
import br.com.market.place.domain.payment.value.PaymentId;
import br.com.market.place.domain.shared.exception.NotFoundException;
import br.com.market.place.domain.shared.exception.PaymentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagementPaymentService implements PaymentService {
    private final Logger logger = LoggerFactory.getLogger(ManagementPaymentService.class);
    private final PaymentRepository paymentRepository;
    private final CancelPaymentService cancelPaymentService;
    private final RunPaymentService runPaymentService;

    @Autowired
    public ManagementPaymentService(
            PaymentRepository paymentRepository,
            CancelPaymentService cancelPaymentService,
            RunPaymentService runPaymentService
    ) {
        this.paymentRepository = paymentRepository;
        this.cancelPaymentService = cancelPaymentService;
        this.runPaymentService = runPaymentService;
    }

    private Payment findPaymentById(PaymentId id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cannot be found payment by id"));
    }

    @Override
    public void pay(PaymentId id) {
        logger.info("Run payment {}", id);
        Payment payment = findPaymentById(id);
        payment.pay(runPaymentService);
        try {
            paymentRepository.saveAndFlush(payment);
        } catch (Exception ex) {
            logger.error("Run payment {}, error: {}", payment, ex.getMessage());
            throw new PaymentException("Cannot be run payment!");
        }
    }

    @Override
    public void cancel(PaymentId id) {
        logger.info("Cancel payment {}", id);
        Payment payment = findPaymentById(id);
        payment.cancelPayment(cancelPaymentService);
        try {
            paymentRepository.saveAndFlush(payment);
        } catch (Exception ex) {
            logger.error("Cancel payment {}, error: {}", payment, ex.getMessage());
            throw new PaymentException("Cannot be cancel the payment!");
        }
    }
}
