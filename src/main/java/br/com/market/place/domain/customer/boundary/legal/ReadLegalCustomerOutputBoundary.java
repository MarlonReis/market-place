package br.com.market.place.domain.customer.boundary.legal;

import br.com.market.place.domain.shared.boundary.ReadAddressOutputBoundary;

public record ReadLegalCustomerOutputBoundary(
        String customerId,
        String name,
        String telephone,
        String cnpj,
        String fantasyName,
        String municipalRegistration,
        String stateRegistration,
        ReadAddressOutputBoundary address
) {
}
