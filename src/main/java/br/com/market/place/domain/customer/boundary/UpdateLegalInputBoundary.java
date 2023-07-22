package br.com.market.place.domain.customer.boundary;

public record UpdateLegalInputBoundary(
        String telephone,
        String cnpj,
        String email,
        AddressInputBoundary address
) {
}
