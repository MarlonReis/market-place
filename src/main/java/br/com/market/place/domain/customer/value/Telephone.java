package br.com.market.place.domain.customer.value;

import br.com.market.place.domain.shared.valodator.ValueObjectValidator;
import jakarta.persistence.Embeddable;
import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.NotNull;

import java.util.Objects;

@Embeddable
public final class Telephone {
    @NotNull(message = "Attribute telephone is required!")
    @MatchPattern(
            pattern = "^[1-9]{2}?(?:[2-8]|9[1-9])(?!.*(\\d)\\1{5,}).{6,11}$",
            message = "Attribute telephone is invalid!"
    )
    private String telephone;

    protected Telephone() {
    }

    public Telephone(String telephone) {
        this.telephone = telephone;
        new ValueObjectValidator().validate(this);
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
        if (o instanceof Telephone that) {
            return Objects.equals(telephone, that.telephone());
        }
        return false;
    }

}
