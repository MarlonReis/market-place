package br.com.market.place.domain.payment.service;

import br.com.market.place.domain.payment.boundary.CreateBilletInputBoundary;

public interface PayLineService {
    String payLineGenerate(CreateBilletInputBoundary data);
}
