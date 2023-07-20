package br.com.market.place.domain.customer.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import jakarta.persistence.Embeddable;

import java.util.Objects;

import static am.ik.yavi.builder.StringValidatorBuilder.of;
@Embeddable
public final class Telephone {
    private String telephone;

    protected Telephone() {
    }

    public Telephone(String telephone) {
        var phoneVal = of("telephone", s -> s.notNull()
                .message("Attribute telephone is required!")
                .pattern("^[1-9]{2}?(?:[2-8]|9[1-9])(?!.*(\\d)\\1{5,}).{6,11}$")
                .message("Attribute telephone is invalid!")
        ).build().validate(telephone);
        this.telephone = phoneVal.orElseThrow(InvalidDataException::new);
    }

    public String telephone() {
        return telephone;
    }

    public String telephoneFormatted() {
        final var ddd = telephone.substring(0, 2);
        final var phone = telephone.substring(2);
        return String.format("(%s) %s", ddd, phone);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof Telephone that){
            return Objects.equals(telephone, that.telephone());
        }
        return false;
    }

}
