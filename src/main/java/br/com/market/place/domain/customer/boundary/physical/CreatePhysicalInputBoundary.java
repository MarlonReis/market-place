package br.com.market.place.domain.customer.boundary.physical;

import br.com.market.place.domain.shared.boundary.AddressInputBoundary;
import br.com.market.place.domain.customer.constant.DocumentType;
import br.com.market.place.domain.customer.entity.Physical;
import br.com.market.place.domain.customer.value.BirthDate;
import br.com.market.place.domain.customer.value.Email;
import br.com.market.place.domain.customer.value.Name;
import br.com.market.place.domain.customer.value.Telephone;

public record CreatePhysicalInputBoundary(
        String name,
        String email,
        String telephone,
        String document,
        String documentType,
        String birthDate,
        AddressInputBoundary address
) {
    public Physical toEntity() {
        return Physical.Builder.build()
                .withName(new Name(name()))
                .withEmail(new Email(email()))
                .withTelephone(new Telephone(telephone()))
                .withBirthDate(new BirthDate(birthDate()))
                .withDocument(document(), DocumentType.physicalType(documentType()))
                .withAddress(address().toEntity())
                .now();
    }
}
