package br.com.market.place.domain.customer.value;
import br.com.market.place.domain.shared.validator.DomainValidator;
import jakarta.persistence.Embeddable;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;


import java.util.Objects;



@Embeddable
public class Name {
    @NotEmpty(message = "Attribute name is required!")
    @NotNull(message = "Attribute name is required!")
    private String name;

    public Name(String name) {
        this.name = name;
        new DomainValidator().validate(this);
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
