package br.com.market.place.infrastructure.service;

import br.com.market.place.domain.payment.boundary.CreateBilletInputBoundary;
import br.com.market.place.domain.payment.service.PayLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PayLineExternalGenerateService implements PayLineService {
    private final Logger logger = LoggerFactory.getLogger(PayLineExternalGenerateService.class);

    @Override
    public String payLineGenerate(CreateBilletInputBoundary data) {
        logger.info("Generate pay line to {}:{} and address {}", data.currencyType(), data.amount(), data.address());
        Random random = new Random();
        return random.ints(0, 10).limit(50)
                .boxed().collect(StringBuffer::new, StringBuffer::append, StringBuffer::append)
                .toString();
    }
}
