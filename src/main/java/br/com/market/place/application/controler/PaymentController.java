package br.com.market.place.application.controler;

import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.payment.boundary.CreateBilletInputBoundary;
import br.com.market.place.domain.payment.boundary.CreateCardInputBoundary;
import br.com.market.place.domain.payment.boundary.ReadBilletOutputBoundary;
import br.com.market.place.domain.payment.boundary.ReadCredCardOutputBoundary;
import br.com.market.place.domain.payment.service.BilletPaymentService;
import br.com.market.place.domain.payment.service.CreditCardPaymentService;
import br.com.market.place.domain.payment.service.PaymentService;
import br.com.market.place.domain.payment.value.PaymentId;
import br.com.market.place.domain.shared.boundary.ResponseBoundary;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/v1/api/payment")
public class PaymentController {
    private final BilletPaymentService billetService;
    private final CreditCardPaymentService credCardService;
    private final PaymentService paymentService;

    public PaymentController(BilletPaymentService billetService, CreditCardPaymentService credCardService, PaymentService paymentService) {
        this.billetService = billetService;
        this.credCardService = credCardService;
        this.paymentService = paymentService;
    }

    @PostMapping("/billets")
    public ResponseEntity<?> createBillet(@RequestHeader("x-customer-id") String customerId,
                                          @Valid @RequestBody CreateBilletInputBoundary data) {
        billetService.create(new CustomerId(customerId), data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/billets/{customerId}")
    public ResponseEntity<ResponseBoundary<Set<ReadBilletOutputBoundary>>> findBilletByCustomerId(@PathVariable("customerId") String id) {
        var response = billetService.findBilletByCustomerId(new CustomerId(id));
        return ResponseEntity.ok(new ResponseBoundary<>(response, true));
    }


    @PostMapping("/credit-card")
    public ResponseEntity<?> createCredCard(@RequestHeader("x-customer-id") String customerId,
                                            @Valid @RequestBody CreateCardInputBoundary data) {
        credCardService.create(new CustomerId(customerId), data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/credit-card/{customerId}")
    public ResponseEntity<ResponseBoundary<Set<ReadCredCardOutputBoundary>>> findCredCardPaymentByCustomerId(@PathVariable("customerId") String id) {
        var response = credCardService.findCredCardPaymentByCustomerId(new CustomerId(id));
        return ResponseEntity.ok(new ResponseBoundary<>(response, true));
    }

    @PostMapping("/pay")
    public void pay(@RequestHeader("x-payment-id") String id) {
        paymentService.pay(new PaymentId(id));
    }

    @PostMapping("/cancel")
    public void cancel(@RequestHeader("x-payment-id") String id) {
        paymentService.cancel(new PaymentId(id));
    }
}
