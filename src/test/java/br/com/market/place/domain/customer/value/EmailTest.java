package br.com.market.place.domain.customer.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {
    @Test
    void shouldReturnValidEmail() {
        Email email = new Email("any@email.com");
        assertThat(email.email(), Matchers.is("any@email.com"));
    }

    @NullSource
    @ParameterizedTest
    @ValueSource(strings = {""})
    void shouldInvalidDataExceptionWhenReceiveNullOrEmpty(String email) {
        var exception = assertThrows(InvalidDataException.class, () -> new Email(email));
        assertThat(exception.getMessage(), Matchers.is("Attribute email is required!"));
    }


    @ParameterizedTest
    @ValueSource(strings = {"any", "any.com", "any com", "@com.br"})
    void shouldInvalidDataExceptionWhenReceiveInvalidEmail(String email) {
        var exception = assertThrows(InvalidDataException.class, () -> new Email(email));
        assertThat(exception.getMessage(), Matchers.is("Attribute email is invalid!"));
    }

    @Test
    void shouldReturnNullWhenNotSpecifyArgument() {
        assertThat(new Email().email(), Matchers.nullValue());
    }

    @Test
    void shouldReturnFalseWhenEmailIsNotEquals() {
        Email email = new Email("any@email.com");

        assertFalse(email.equals(new Email("any@different.com")));
        assertFalse(email.equals(new Object()));
        assertFalse(email.equals(null));
    }
}