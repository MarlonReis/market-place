package br.com.market.place.domain.customer.boundary.legal;

import br.com.market.place.domain.shared.boundary.AddressInputBoundary;
import br.com.market.place.domain.customer.entity.Legal;
import br.com.market.place.domain.customer.value.Email;
import br.com.market.place.domain.customer.value.Name;
import br.com.market.place.domain.customer.value.Telephone;

public record CreateLegalInputBoundary(
        String name,
        String telephone,
        String email,
        String cnpj,
        String fantasyName,
        String municipalRegistration,
        String stateRegistration,
        AddressInputBoundary address
) {
    public Legal toEntity() {
        return Legal.Builder.build()
                .withName(new Name(name()))
                .withFantasyName(new Name(fantasyName()))
                .withStateRegistration(stateRegistration())
                .withMunicipalRegistration(municipalRegistration())
                .withCNPJ(cnpj())
                .withAddress(address.toEntity())
                .withEmail(new Email(email()))
                .withTelephone(new Telephone(telephone()))
                .now();
    }
}
