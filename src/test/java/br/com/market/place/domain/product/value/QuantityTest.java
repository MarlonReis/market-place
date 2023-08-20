package br.com.market.place.domain.product.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuantityTest {

    @Test
    void shouldReturnQuantity() {
        Quantity quantity = new Quantity(10);

        assertThat(quantity.value(), Matchers.is(10));
    }

    @Test
    void shouldThrowsInvalidDataExceptionWhenValueIsNegative() {
        var exception = assertThrows(InvalidDataException.class, () -> new Quantity(-10));
        assertThat(exception.getData().message(), Matchers.is("Quantity cannot be negative!"));
    }

    @Test
    void shouldReturn10WhenQuantityIs15AndSubtract5() {
        Quantity quantity = new Quantity(15);

        Quantity result = quantity.subtract(new Quantity(5));
        assertThat(result.value(), Matchers.is(10));
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenNotHaveEnoughQuantity() {
        Quantity quantity = new Quantity(15);

        var exception = assertThrows(InvalidDataException.class, () -> quantity.subtract(new Quantity(16)));
        assertThat(exception.getMessage(), Matchers.is("Does not contains enough for this operation!"));
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenSubtractReceiveNullValue() {
        Quantity quantity = new Quantity(15);

        var exception = assertThrows(InvalidDataException.class, () -> quantity.subtract(null));
        assertThat(exception.getMessage(), Matchers.is("It is necessary to pass valid param to subtract quantity!"));

    }

    @Test
    void shouldReturn20WhenQuantityIs10AndAdd10() {
        Quantity quantity = new Quantity(10);

        Quantity result = quantity.add(new Quantity(10));
        assertThat(result.value(), Matchers.is(20));
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenAddReceiveNullValue() {
        Quantity quantity = new Quantity(10);

        var exception = assertThrows(InvalidDataException.class, () -> quantity.add(null));
        assertThat(exception.getMessage(), Matchers.is("It is necessary to pass valid param to add quantity!"));
    }

    @Test
    void shouldReturnZeroWhenUseQuantityDefaultConstructor() {
        Quantity quantity = new Quantity();
        assertThat(quantity.value(), Matchers.is(0));
    }

    @Test
    void shouldReturnTrueWhenBothQuantityAreEquals() {
        Quantity quantity = new Quantity(10);

        assertThat(quantity.hashCode(), Matchers.is(new Quantity(10).hashCode()));
        assertThat(quantity.equals(new Quantity(10)), Matchers.is(true));
        assertThat(quantity.equals(new Quantity(11)), Matchers.is(false));
        assertThat(quantity.equals(new Object()), Matchers.is(false));
        assertThat(quantity.equals(null), Matchers.is(false));
    }


}