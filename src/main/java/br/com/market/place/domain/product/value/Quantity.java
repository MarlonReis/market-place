package br.com.market.place.domain.product.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import jakarta.persistence.Embeddable;

import java.math.BigInteger;
import java.util.Objects;

@Embeddable
public class Quantity {
    private BigInteger quantity;

    protected Quantity() {
        quantity = BigInteger.ZERO;
    }

    public Quantity(int value) {
        this.quantity = BigInteger.valueOf(value);
        if (quantity.signum() == -1) {
            throw new InvalidDataException("Quantity cannot be negative!");
        }
    }

    public int value() {
        return quantity.intValue();
    }

    public Quantity subtract(Quantity value) {
        if (value == null) throw new InvalidDataException("It is necessary to pass valid param to subtract quantity!");

        BigInteger result = quantity.subtract(value.quantity);
        if (result.signum() == -1) {
            throw new InvalidDataException("Does not contains enough for this operation!");
        }
        return new Quantity(result.intValue());
    }

    public Quantity add(Quantity value) {
        if (value == null) throw new InvalidDataException("It is necessary to pass valid param to add quantity!");
        return new Quantity(quantity.add(value.quantity).intValue());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof Quantity that) {
            return Objects.equals(quantity, that.quantity);
        }
        return false;
    }
}
