package br.com.market.place.domain.customer.boundary.physical;

import br.com.market.place.domain.customer.boundary.ReadAddressOutputBoundary;

public record ReadPhysicalOutputBoundary(
        String customerId,
        String name,
        String email,
        String telephone,
        String document,
        ReadAddressOutputBoundary address
) {
}
