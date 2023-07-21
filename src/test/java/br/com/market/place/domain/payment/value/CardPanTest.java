package br.com.market.place.domain.payment.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class CardPanTest {

    @Test
    void shouldReturnValidCardPan() {
        CardPan cardPan = new CardPan("5487689144558724");
        assertThat(cardPan.cardPan(), Matchers.is("5487689144558724"));
        assertThat(cardPan.maskedCard(), Matchers.is("5487********8724"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "5487689140058724", "548768914005", "548760058724", "00876891400587241"})
    void shouldReturnThrowsInvalidDataExceptionWhenCardNumberIsInvalid(String value) {
        var exception = assertThrows(InvalidDataException.class, () -> new CardPan(value));
        assertThat(exception.getMessage(), Matchers.is("Attribute cardNumber is invalid!"));
    }

    @Test
    void shouldReturnThrowsInvalidDataExceptionWhenCardNumberIsNull() {
        var exception = assertThrows(InvalidDataException.class, () -> new CardPan(null));
        assertThat(exception.getMessage(), Matchers.is("Attribute cardNumber is required!"));
    }

    @Test
    void shouldReturnFalseWhenCardsIsNotEquals() {
        CardPan cardPan = new CardPan("5487689144558724");

        assertTrue(cardPan.equals(new CardPan("5487689144558724")));
        assertFalse(cardPan.equals(new CardPan("5552619476158801")));
        assertFalse(cardPan.equals(new Object()));
        assertFalse(cardPan.equals(null));

    }

    @Test
    void shouldReturnNullWhenCallDefaultConstructor() {
        CardPan cardPan = new CardPan();
        assertThat(cardPan.cardPan(), Matchers.nullValue());
    }


}