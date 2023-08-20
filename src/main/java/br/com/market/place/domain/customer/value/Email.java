package br.com.market.place.domain.customer.value;

import br.com.market.place.domain.shared.validator.DomainValidator;
import jakarta.persistence.Embeddable;
import net.sf.oval.constraint.NotNull;

import java.util.Objects;


@Embeddable
public class Email {

    @NotNull(message = "Attribute email is required!")
    @net.sf.oval.constraint.Email(message = "Attribute email is invalid!")
    private String email;

    public Email(String email) {
        this.email = email;
        new DomainValidator().validate(this);
    }

    protected Email() {
    }

    public String email() {
        return this.email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof Email that) {
            return Objects.equals(email, that.email());
        }
        return false;
    }
}
