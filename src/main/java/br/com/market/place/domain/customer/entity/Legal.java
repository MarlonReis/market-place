package br.com.market.place.domain.customer.entity;

import br.com.market.place.domain.customer.boundary.ReadAddressOutputBoundary;
import br.com.market.place.domain.customer.boundary.ReadLegalCustomerOutputBoundary;
import br.com.market.place.domain.customer.constant.DocumentType;
import br.com.market.place.domain.customer.value.*;
import br.com.market.place.domain.shared.value.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public final class Legal extends Customer {
    @AttributeOverrides({@AttributeOverride(name = "name", column = @Column(name = "fantasyName"))})
    private Name fantasyName;
    private String municipalRegistration;
    private String stateRegistration;


    public void changeAddress(@NotNull Address address) {
        super.setAddress(new Address(
                address.city(),
                address.street(),
                address.number(),
                address.component(),
                address.zipCode()
        ));
    }


    public void changeEmail(String email) {
        super.setEmail(new Email(email));
    }


    public void changeTelephone(String telephone) {
        super.setTelephone(new Telephone(telephone));
    }

    @Override
    public boolean isLegalPerson() {
        return true;
    }

    public Name getFantasyName() {
        return fantasyName;
    }

    public String getMunicipalRegistration() {
        return municipalRegistration;
    }

    public String getStateRegistration() {
        return stateRegistration;
    }

    private void setFantasyName(Name fantasyName) {
        this.fantasyName = fantasyName;
    }

    private void setMunicipalRegistration(String municipalRegistration) {
        this.municipalRegistration = municipalRegistration;
    }

    private void setStateRegistration(String stateRegistration) {
        this.stateRegistration = stateRegistration;
    }

    public ReadLegalCustomerOutputBoundary toReadLegalCustomerOutputBoundary() {
        return new ReadLegalCustomerOutputBoundary(
                getId().toString(),
                getName().name(),
                getTelephone().telephoneFormatted(),
                getDocument().document(),
                getFantasyName().name(),
                getMunicipalRegistration(),
                getStateRegistration(),
                new ReadAddressOutputBoundary(getAddress().toString()));
    }

    public static final class Builder {
        private Legal legal;

        private Builder() {
            legal = new Legal();
        }

        public static Builder build() {
            return new Builder();
        }

        public Builder withName(Name name) {
            legal.setName(name);
            return this;
        }

        public Builder withFantasyName(Name name) {
            legal.setFantasyName(name);
            return this;
        }

        public Builder withMunicipalRegistration(String municipalRegistration) {
            legal.setMunicipalRegistration(municipalRegistration);
            return this;
        }

        public Builder withStateRegistration(String stateRegistration) {
            legal.setStateRegistration(stateRegistration);
            return this;
        }

        public Builder withEmail(Email email) {
            legal.setEmail(email);
            return this;
        }

        public Builder withTelephone(Telephone telephone) {
            legal.setTelephone(telephone);
            return this;
        }

        public Builder withCNPJ(String cnpj) {
            legal.setDocument(new Document(cnpj, DocumentType.CNPJ));
            return this;
        }

        public Builder withAddress(Address address) {
            legal.setAddress(address);
            return this;
        }


        public Legal now() {
            return legal;
        }
    }
}
