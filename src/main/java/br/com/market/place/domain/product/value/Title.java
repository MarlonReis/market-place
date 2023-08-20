package br.com.market.place.domain.product.value;

import br.com.market.place.domain.shared.validator.ValueObjectValidator;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.util.Objects;

public class Title {
    @NotEmpty(message = "Attribute title is required!")
    @NotNull(message = "Attribute title is required!")
    private String title;

    @NotEmpty(message = "Attribute description is required!")
    @NotNull(message = "Attribute description is required!")
    private String description;

    protected Title() {
    }

    public Title(String title, String description) {
        this.title = title;
        this.description = description;

        new ValueObjectValidator().validate(this);
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Title that) {
            return Objects.equals(this.description, that.description) &&
                    Objects.equals(this.title, that.title);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s, %s", title, description);
    }
}
