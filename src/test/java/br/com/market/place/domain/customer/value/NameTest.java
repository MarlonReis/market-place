package br.com.market.place.domain.customer.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NameTest {
    @Test
    void shouldReturnFullNameWhenSetCorrectParams() {
        Name name = new Name("Michael Jackson");
        assertThat(name.name(), Matchers.is("Michael Jackson"));
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenReceiveNull() {
        var exception = assertThrows(InvalidDataException.class, () -> new Name(null));
        assertThat(exception.getMessage(), Matchers.is("Attribute name is required!"));
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenReceiveEmpty() {
        var exception = assertThrows(InvalidDataException.class, () -> new Name(""));
        assertThat(exception.getMessage(), Matchers.is("Attribute name is required!"));
    }

    @Test
    void shouldReturnNullWhenUseDefaultConstructor() {
        Name name = new Name();
        assertThat(name.name(), Matchers.nullValue());
    }

    @Test
    void shouldReturnFalseWhenNameIsDifferent() {
        Name name = new Name("Michael Jackson");

        assertTrue(name.equals(new Name("Michael Jackson")));
        assertFalse(name.equals(new Name("Michael")));
        assertFalse(name.equals(new Object()));
        assertFalse(name.equals(null));
    }

}