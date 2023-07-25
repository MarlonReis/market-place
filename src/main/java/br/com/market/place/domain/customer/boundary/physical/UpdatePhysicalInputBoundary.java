package br.com.market.place.domain.customer.boundary.physical;

import br.com.market.place.domain.customer.boundary.AddressInputBoundary;
import jakarta.validation.constraints.NotNull;

public record UpdatePhysicalInputBoundary(
        String customerId,
        String email,
        String telephone,
        @NotNull
        AddressInputBoundary address
) {
}
