package br.com.market.place.factory;

import br.com.market.place.domain.customer.entity.Legal;
import br.com.market.place.domain.customer.entity.Physical;
import br.com.market.place.domain.customer.value.BirthDate;
import br.com.market.place.domain.customer.value.Email;
import br.com.market.place.domain.customer.value.Name;
import br.com.market.place.domain.customer.value.Telephone;
import br.com.market.place.domain.shared.value.Address;

import static br.com.market.place.domain.customer.constant.DocumentType.CPF;

public class CustomerEntityMockFactory {
    private Address address;

    public CustomerEntityMockFactory() {
        address = Address.Builder.build().withCity("London")
                .withStreet("Baker Street").withNumber("221")
                .withComponent("B").withZipCode("37540232").now();
    }

    public Legal.Builder makeLegalFactory() {
        return Legal.Builder.build()
                .withName(new Name("Josefa e Nicole Pizzaria Delivery LTDA"))
                .withFantasyName(new Name("Josefa e Nicole"))
                .withCNPJ("33.747.249/0001-14")
                .withMunicipalRegistration("807337772144")
                .withStateRegistration("807337772144")
                .withEmail(new Email("financeiro@exemple.com.br"))
                .withTelephone(new Telephone("11999982343"))
                .withAddress(address);
    }

    public Physical.Builder makePhysicalFactory() {
        return Physical.Builder.build()
                .withName(new Name("Benedito Caio Araújo"))
                .withDocument("536.271.871-13", CPF)
                .withEmail(new Email("benedito-araujo91@gmnail.com"))
                .withBirthDate(new BirthDate("23/02/2001"))
                .withTelephone(new Telephone("11999982343"))
                .withAddress(address);
    }
}
