package br.com.market.place.domain.shared.value;

import br.com.market.place.domain.shared.exception.CurrencyException;
import br.com.market.place.domain.shared.exception.InvalidDataException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static br.com.market.place.domain.shared.constant.CurrencyType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {
    @Test
    void shouldReturnCurrencyValue() {
        Currency currency = new Currency("10.00", "USD");

        assertThat(currency.amount(), Matchers.is(new BigDecimal("10.00")));
        assertThat(currency.type(), Matchers.is(USD));
        assertThat(currency.formatted(), Matchers.is("$10.00"));
    }

    @Test
    void shouldReturnCurrencyFormattedByCurrency() {
        Currency currencyUSD = new Currency("10.00", "USD");
        Currency currencyGBP = new Currency("10.00", "GBP");
        Currency currencyBRL = new Currency("10.00", "BRL");

        assertThat(currencyUSD.formatted(), Matchers.is("$10.00"));
        assertThat(currencyGBP.formatted(), Matchers.is("£10.00"));
        assertThat(currencyBRL.formatted(), Matchers.is("R$ 10,00"));
    }

    @Test
    void shouldReturnNewCurrencyWhenAddValue() {
        Currency currency = new Currency("10.00", "USD");
        Currency total = currency.add(new Currency("5.00", "USD"));
        assertThat(total, Matchers.is(new Currency("15.00", "USD")));
    }

    @Test
    void shouldThrowsCurrencyExceptionWhenTypesWereDifferent() {
        Currency currency = new Currency("10.00", "USD");
        var exception = assertThrows(CurrencyException.class, () -> currency.add(new Currency("0", "BRL")));
        assertThat(exception.getMessage(), Matchers.is("Currency type are different!"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"$23.09", "23,34", "USD 24.43"})
    void shouldThrowInvalidDataExceptionWhenReceiveInvalidValue(String value) {
        var exception = assertThrows(InvalidDataException.class, () -> new Currency(value, "BRL"));
        assertThat(exception.getMessage(), Matchers.is("Attribute amount is invalid!"));
    }

    @Test
    void shouldThrowsInvalidDataExceptionWhenReceiveNull() {
        var exception = assertThrows(InvalidDataException.class, () -> new Currency(null, "BRL"));
        assertThat(exception.getMessage(), Matchers.is("Attribute amount is required!"));

        var exceptionType = assertThrows(InvalidDataException.class, () -> new Currency("10.00", null));
        assertThat(exceptionType.getMessage(), Matchers.is("Attribute currencyType is required!"));
    }

    @Test
    void shouldReturnFalseWhenCurrencyIsDifferent() {
        Currency currency = new Currency("10.00", "USD");

        assertTrue(currency.equals(new Currency("10.00", "USD")));
        assertFalse(currency.equals(new Currency("14.00", "USD")));
        assertFalse(currency.equals(new Object()));
        assertFalse(currency.equals(null));
    }

    @Test
    void shouldReturnNullWhenCallDefaultConstructor() {
        Currency currency = new Currency();
        assertThat(currency.amount(), Matchers.nullValue());
        assertThat(currency.type(), Matchers.nullValue());
    }

}