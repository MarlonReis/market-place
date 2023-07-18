package br.com.market.place.domain.customer.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class TelephoneTest {
    @Test
    void shouldReturnValidTelephone() {
        Telephone telephone = new Telephone("35999902345");
        assertThat(telephone.telephone(), Matchers.is("35999902345"));
        assertThat(telephone.telephoneFormatted(), Matchers.is("(35) 999902345"));
    }

    @Test
    void shouldThrowsInvalidDataExceptionWhenPhoneIsNull() {
        var exception = assertThrows(InvalidDataException.class, () -> new Telephone(null));
        assertThat(exception.getMessage(), Matchers.is("Attribute telephone is required!"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "999999999", "(35)99990-3234", "99999999999", "218888888", "111111111"})
    void shouldThrowsInvalidDataExceptionWhenPhoneIsInvalid(String phone) {
        var exception = assertThrows(InvalidDataException.class, () -> new Telephone(phone));
        assertThat(exception.getMessage(), Matchers.is("Attribute telephone is invalid!"));
    }

    @Test
    void shouldReturnNullWhenCreateTelephoneWithDefaultConstructor(){
        Telephone telephone = new Telephone();
        assertThat(telephone.telephone(), CoreMatchers.nullValue());
    }

}