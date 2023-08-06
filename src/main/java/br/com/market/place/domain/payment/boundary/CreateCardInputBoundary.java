package br.com.market.place.domain.payment.boundary;

import br.com.market.place.domain.shared.boundary.AddressInputBoundary;
import jakarta.validation.constraints.NotNull;

public record CreateCardInputBoundary(
        @NotNull(message = "Attribute cardPan is required!")
        String cardPan,
        @NotNull(message = "Attribute amount is required!")
        String amount,
        @NotNull(message = "Attribute currencyType is required!")
        String currencyType,
        @NotNull(message = "Attribute address is required!")
        AddressInputBoundary address
) {
}
