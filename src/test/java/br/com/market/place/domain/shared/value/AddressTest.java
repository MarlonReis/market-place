package br.com.market.place.domain.shared.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AddressTest {
    @Test
    void shouldReturnValidAddress() {
        Address address = new Address("London", "Baker Street", "221", "B", "37540-232");

        assertThat(address.city(), Matchers.is("London"));
        assertThat(address.street(), Matchers.is("Baker Street"));
        assertThat(address.number(), Matchers.is("221"));
        assertThat(address.component(), Matchers.is("B"));
        assertThat(address.zipCode(), Matchers.is("37540232"));
        assertThat(address.toString(), Matchers.is("Baker Street, 221, B, London, 37540232"));

    }

    @Test
    void shouldReturnTrueWhenAddressIsEquals() {
        Address addressOne = new Address("London", "Baker Street", "221", "B", "37540-232");
        Address addressTwo = new Address("London", "Baker Street", "221", "B", "37540-232");

        assertTrue(addressOne.equals(addressTwo));
        assertFalse(addressOne.equals(new Address("York", "Baker Street", "221", "B", "37540-232")));
        assertFalse(addressOne.equals(new Object()));
        assertFalse(addressOne.equals(null));

    }

    @Test
    void shouldThrowsInvalidDataExceptionWhenReceiveInvalidField() {
        var exception = assertThrows(InvalidDataException.class, () -> new Address(
                "London", "Baker Street", "221", "", "37540000")
        );
        assertThat(exception.getMessage(), Matchers.is("Attribute component is required!"));
    }

    @Test
    void shouldReturnNullWhenUseDefaultConstructor() {
        Address address = new Address();

        assertThat(address.city(), Matchers.nullValue());
        assertThat(address.street(), Matchers.nullValue());
        assertThat(address.number(), Matchers.nullValue());
        assertThat(address.component(), Matchers.nullValue());
        assertThat(address.zipCode(), Matchers.nullValue());
    }

}