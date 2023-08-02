package br.com.market.place.domain.customer.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import jakarta.persistence.Embeddable;


import java.util.Objects;

import static am.ik.yavi.builder.StringValidatorBuilder.of;


@Embeddable
public class Name {

    private String name;

    public Name(String name) {
        var nameVal = of("name", s -> s.notBlank().message("Attribute name is required!")).build().validate(name);
        this.name = name;nameVal.orElseThrow(InvalidDataException::new);
    }

    protected Name() {
    }

    public String name() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof Name that) {
            return Objects.equals(name, that.name);
        }
        return false;
    }
}
