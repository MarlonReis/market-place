package br.com.market.place.domain.customer.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import org.exparity.hamcrest.date.LocalDateMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Month;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BirthDateTest {

    @Test
    void shouldReturnValidDateWhenReceiveValidParam() {
        var date = new BirthDate("31/12/1991");
        assertThat(date.birthDate(), LocalDateMatchers.isDay(1991, Month.DECEMBER, 31));
        assertThat(date.dateFormatted(), Matchers.is("31/12/1991"));
    }

    @Test
    void shouldThrowsInvalidDataExceptionWhenNull() {
        var exception = assertThrows(InvalidDataException.class, () -> new BirthDate(null));
        assertThat(exception.getMessage(), Matchers.is("Attribute birthDate is required!"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"31/22/1991", "10/13/1991", "02/02/1891"})
    void shouldThrowsInvalidDataExceptionWhenReceiveInvalidDate(String date) {
        var exception = assertThrows(InvalidDataException.class, () -> new BirthDate(date));
        assertThat(exception.getMessage(), Matchers.is("Attribute birthDate is invalid!"));
    }

    @Test
    void shouldReturnNullWhenUseDefaultConstructor() {
        assertThat(new BirthDate().birthDate(), Matchers.nullValue());
    }

    @Test
    void shouldReturnFalseWhenDateEqualsIsDifferent() {
        var date = new BirthDate("31/12/1991");

        assertTrue(date.equals(new BirthDate("31/12/1991")));
        assertFalse(date.equals(new BirthDate("31/12/1992")));
        assertFalse(date.equals(new Object()));
        assertFalse(date.equals(null));
    }

}