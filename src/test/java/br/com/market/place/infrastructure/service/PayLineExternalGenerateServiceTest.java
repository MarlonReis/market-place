package br.com.market.place.infrastructure.service;

import br.com.market.place.domain.payment.boundary.CreateBilletInputBoundary;
import br.com.market.place.domain.payment.service.PayLineService;
import br.com.market.place.infrastructure.service.payment.PayLineExternalGenerateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@Import({PayLineExternalGenerateService.class})
class PayLineExternalGenerateServiceTest {
    @SpyBean
    private PayLineService service;

    @Test
    void shouldReturnPayLineWhenGenerated() {
        var payLine = service.payLineGenerate(new CreateBilletInputBoundary("10.0", "BRL", null));

        assertThat(payLine, Matchers.not(Matchers.blankOrNullString()));
        assertThat(payLine, Matchers.hasLength(50));
    }

}