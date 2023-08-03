package br.com.market.place.domain.shared.value;

import br.com.market.place.domain.shared.valodator.ValueObjectValidator;
import jakarta.persistence.Embeddable;
import net.sf.oval.constraint.Digits;
import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.util.Objects;


@Embeddable
public class Address {
    @NotEmpty(message = "Attribute city is required!")
    @NotNull(message = "Attribute city is required!")
    private String city;
    @NotNull(message = "Attribute street is required!")
    @NotEmpty(message = "Attribute street is required!")
    private String street;
    @NotNull(message = "Attribute number is required!")
    @Digits(message = "Attribute number is invalid!")
    private String number;
    @NotNull(message = "Attribute component is required!")
    @NotEmpty(message = "Attribute component is required!")
    private String component;

    @NotNull(message = "Attribute zipCode is required!")
    @MatchPattern(pattern = "((\\d{5})(?:[-]|)(\\d{3}))", message = "Attribute zipCode is invalid!")
    private String zipCode;

    protected Address() {
    }


    public Address(String city, String street, String number, String component, String zipCode) {
        this.city = city;
        this.street = street;
        this.number = number;
        this.component = component;
        this.zipCode = zipCode == null ? null : zipCode.replaceAll("-", "");
        new ValueObjectValidator().validate(this);
    }

    public String city() {
        return city;
    }

    public String street() {
        return street;
    }

    public String number() {
        return number;
    }

    public String component() {
        return component;
    }

    public String zipCode() {
        return zipCode;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s", street, number, component, city, zipCode);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof Address address) {
            return Objects.equals(city, address.city()) &&
                    Objects.equals(street, address.street()) &&
                    Objects.equals(number, address.number()) &&
                    Objects.equals(component, address.component()) &&
                    Objects.equals(zipCode, address.zipCode());
        }
        return false;
    }


    public static final class Builder {
        private Address address;

        private Builder() {
            address = new Address();
        }

        public static Builder build() {
            return new Builder();
        }

        public Builder withCity(String city) {
            address.city = city;
            return this;
        }

        public Builder withStreet(String street) {
            address.street = street;
            return this;
        }

        public Builder withNumber(String number) {
            address.number = number;
            return this;
        }

        public Builder withComponent(String component) {
            address.component = component;
            return this;
        }

        public Builder withZipCode(String zipCode) {
            address.zipCode = zipCode;
            return this;
        }

        public Address now() {
            return new Address(
                    address.city(),
                    address.street(),
                    address.number(),
                    address.component(),
                    address.zipCode()
            );
        }
    }
}
