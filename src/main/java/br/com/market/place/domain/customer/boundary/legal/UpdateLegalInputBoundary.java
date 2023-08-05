package br.com.market.place.domain.customer.boundary.legal;

import br.com.market.place.domain.shared.boundary.AddressInputBoundary;

public record UpdateLegalInputBoundary(
        String telephone,
        String cnpj,
        String email,
        AddressInputBoundary address
) {
}
