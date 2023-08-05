package br.com.market.place.domain.payment.boundary;

import br.com.market.place.domain.shared.boundary.AddressInputBoundary;


public record CreateCardInputBoundary(
        String cardPan,
        String amount,
        String currentType,
        AddressInputBoundary address
) {
}
