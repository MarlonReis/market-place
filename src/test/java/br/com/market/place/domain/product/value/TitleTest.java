package br.com.market.place.domain.product.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TitleTest {

    @Test
    void shouldReturnValidTitle() {
        Title title = new Title("Mouse", "Mouse USB-C");

        assertThat(title.title(), Matchers.is("Mouse"));
        assertThat(title.description(), Matchers.is("Mouse USB-C"));
        assertThat(title.toString(), Matchers.is("Mouse, Mouse USB-C"));
    }

    @NullSource
    @EmptySource
    @ParameterizedTest
    void shouldThrowInvalidDataExceptionWhenTitleIsEmpty(String title) {
        var exception = assertThrows(InvalidDataException.class, () -> new Title(title, "Mouse USB-C"));
        assertThat(exception.getMessage(), Matchers.is("Attribute title is required!"));
    }

    @NullSource
    @EmptySource
    @ParameterizedTest
    void shouldThrowInvalidDataExceptionWhenDescriptionTitleIsEmpty(String description) {
        var exception = assertThrows(InvalidDataException.class, () -> new Title("Mouse", description));
        assertThat(exception.getMessage(), Matchers.is("Attribute description is required!"));
    }

    @Test
    void shouldReturnTrueWhenBothTitleAreEquals() {
        Title title = new Title("Mouse", "Mouse USB-C");
        Title titleOther = new Title("Mouse", "Mouse USB-C");


        assertThat(title.hashCode(), Matchers.is(titleOther.hashCode()));
        assertThat(title.equals(titleOther), Matchers.is(true));
        assertThat(title.equals(new Title("Mouse", "USB-C")), Matchers.is(false));
        assertThat(title.equals(new Object()), Matchers.is(false));
        assertThat(title.equals(null), Matchers.is(false));
    }

    @Test
    void shouldReturnNullWhenCallTitleDefaultConstructor() {
        Title title = new Title();

        assertThat(title.title(), Matchers.nullValue());
        assertThat(title.description(), Matchers.nullValue());
    }

}