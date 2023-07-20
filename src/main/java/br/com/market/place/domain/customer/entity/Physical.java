package br.com.market.place.domain.customer.entity;

import br.com.market.place.domain.customer.value.*;
import br.com.market.place.domain.shared.value.Address;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "physical",
        indexes = {
                @Index(columnList = "document", name = "physical_document_index", unique = true),
                @Index(columnList = "email", name = "physical_email_index", unique = true)
        })
public final class Physical extends Customer {
    private BirthDate birthDate;

    @Override
    public boolean isLegalPerson() {
        return false;
    }

    public BirthDate getBirthDate() {
        return birthDate;
    }

    private void setBirthDate(BirthDate birthDate) {
        this.birthDate = birthDate;
    }

    public static final class Builder {
        private Physical physical;

        private Builder() {
            physical = new Physical();
        }

        public static Physical.Builder build() {
            return new Physical.Builder();
        }

        public Physical.Builder withName(Name name) {
            physical.setName(name);
            return this;
        }

        public Physical.Builder withEmail(Email email) {
            physical.setEmail(email);
            return this;
        }

        public Physical.Builder withTelephone(Telephone telephone) {
            physical.setTelephone(telephone);
            return this;
        }

        public Physical.Builder withDocument(String cpf, DocumentType type) {
            physical.setDocument(new Document(cpf, type));
            return this;
        }

        public Physical.Builder withAddress(Address address) {
            physical.setAddress(address);
            return this;
        }

        public Physical.Builder withBirthDate(BirthDate birthDate) {
            physical.setBirthDate(birthDate);
            return this;
        }


        public Physical now() {
            return physical;
        }
    }
}
