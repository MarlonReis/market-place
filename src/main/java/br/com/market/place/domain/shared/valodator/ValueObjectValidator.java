package br.com.market.place.domain.shared.valodator;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

import java.util.stream.Collectors;

public class ValueObjectValidator {
    private final Validator validator;

    public ValueObjectValidator() {
        this.validator = new Validator();
    }

    public void validate(Object value) {
        var response = validator.validate(value);
        if (!response.isEmpty()) {
            throw new InvalidDataException(response.stream().
                    map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")));
        }
    }
}
