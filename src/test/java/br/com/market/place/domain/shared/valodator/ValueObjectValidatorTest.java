package br.com.market.place.domain.shared.valodator;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import net.sf.oval.constraint.NotNull;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValueObjectValidatorTest {
    private ValueObjectValidator objectValidator;

    @BeforeEach
    void setUp() {
        objectValidator = new ValueObjectValidator();
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenClassReceiveInvalidData() {
        var exception = assertThrows(InvalidDataException.class, () -> objectValidator.validate(new ExampleStub(null)));
        assertThat(exception.getMessage(), Matchers.is("Error message!"));
    }

    @Test
    void shouldNotThrowExceptionWhenReceiveValidData() {
        objectValidator.validate(new ExampleStub("Any message"));
    }


    private static class ExampleStub {
        @NotNull(message = "Error message!")
        private String example;

        private ExampleStub(String example) {
            this.example = example;
        }
    }

}