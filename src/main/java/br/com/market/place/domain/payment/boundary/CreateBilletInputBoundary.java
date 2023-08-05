package br.com.market.place.domain.payment.boundary;

import br.com.market.place.domain.shared.boundary.AddressInputBoundary;

import java.math.BigDecimal;

public record CreateBilletInputBoundary(
        String amount,
        String currencyType,
        AddressInputBoundary address
) {
}
