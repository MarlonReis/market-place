package br.com.market.place.infrastructure.service.payment;

import br.com.market.place.domain.payment.constant.PaymentStatus;
import br.com.market.place.domain.payment.entity.Billet;
import br.com.market.place.domain.payment.entity.CredCard;
import br.com.market.place.domain.payment.service.CancelPaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Import({CancelPaymentExternalService.class})
class CancelPaymentExternalServiceTest {
    @SpyBean
    private CancelPaymentService service;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void shouldReturnReversedWhenReceiveStatusPaidOut() throws Exception {
        final var json = "{\"id\": {\"id\": \"2b285470-86fb-486b-8671-3f4e3cfc5556\"},\"status\": \"PAID_OUT\"}";
        var payment = mapper.readValue(json, Billet.class);
        var status = service.cancelPayment(payment);
        assertEquals(status, PaymentStatus.REVERSED);
    }

    @Test
    void shouldReturnCanceledWhenReceiveStatusPending() throws Exception {
        final var json = "{\"id\": {\"id\": \"2b285470-86fb-486b-8671-3f4e3cfc5556\"},\"status\": \"PENDING\"}";
        var payment = mapper.readValue(json, CredCard.class);
        var status = service.cancelPayment(payment);
        assertEquals(status, PaymentStatus.CANCELED);
    }

    @Test
    void shouldReturnFailWhenReceiveStatusExpired() throws Exception {
        final var json = "{\"id\": {\"id\": \"2b285470-86fb-486b-8671-3f4e3cfc5556\"},\"status\": \"EXPIRED\"}";
        var payment = mapper.readValue(json, CredCard.class);
        var status = service.cancelPayment(payment);
        assertEquals(status, PaymentStatus.FAIL);
    }

}