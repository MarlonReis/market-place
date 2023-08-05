package br.com.market.place.domain.shared.boundary;

import br.com.market.place.domain.shared.value.Address;

public record AddressInputBoundary(
        String city,
        String street,
        String number,
        String component,
        String zipCode
) {
    public Address toEntity() {
        return Address.Builder.build()
                .withStreet(street())
                .withNumber(number())
                .withComponent(component())
                .withCity(city())
                .withZipCode(zipCode()).now();
    }
}
