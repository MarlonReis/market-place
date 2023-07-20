package br.com.market.place.domain.shared.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import jakarta.persistence.Embeddable;

import java.util.Objects;

import static am.ik.yavi.builder.StringValidatorBuilder.of;

@Embeddable
public class Address {

    private String city;
    private String street;
    private String number;
    private String component;
    private String zipCode;

    protected Address() {
    }

    public Address(String city, String street, String number, String component, String zipCode) {
        var cityVal = of("city", s -> s.notBlank().message("Attribute city is required!")).build().validate(city);
        var streetVal = of("street", s -> s.notBlank().message("Attribute street is required!")).build()
                .validate(street);
        var numberVal = of("number", s -> s.notBlank().message("Attribute number is required!").pattern("\\d+")
                .message("Attribute number is invalid!")).build().validate(number);
        var componentVal = of("component", s -> s.notBlank().message("Attribute component is required!")).build().validate(component);
        var zipCodeVal = of("zipCode", s -> s.notBlank().message("Attribute zipCode is required!")
                .pattern("((\\d{5})(?:[-]|)(\\d{3}))").message("Attribute zipCode is invalid!")
        ).build().validate(zipCode);

        this.city = cityVal.orElseThrow(InvalidDataException::new);
        this.street = streetVal.orElseThrow(InvalidDataException::new);
        this.number = numberVal.orElseThrow(InvalidDataException::new);
        this.component = componentVal.orElseThrow(InvalidDataException::new);
        this.zipCode = zipCodeVal.orElseThrow(InvalidDataException::new)
                .replace("-", "");
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
