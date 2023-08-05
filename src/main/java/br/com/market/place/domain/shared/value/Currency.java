package br.com.market.place.domain.shared.value;

import br.com.market.place.domain.shared.constant.CurrencyType;
import br.com.market.place.domain.shared.exception.CurrencyException;
import br.com.market.place.domain.shared.exception.InvalidDataException;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Objects;

@Embeddable
public class Currency {
    private BigDecimal amount;
    private CurrencyType currencyType;

    public Currency(String amount, String type) {
        if (amount == null) throw new InvalidDataException("Attribute amount is required!");
        if (type == null) throw new InvalidDataException("Attribute currencyType is required!");

        if (!amount.matches("^[\\d|.]+")) {
            throw new InvalidDataException("Attribute amount is invalid!");
        }
        this.amount = new BigDecimal(amount);
        this.currencyType = CurrencyType.findByType(type);
    }


    protected Currency() {
    }

    private Currency(BigDecimal amount, CurrencyType type) {
        this.amount = amount;
        this.currencyType = type;
    }

    public Currency add(Currency amount) {
        if (type() != amount.type()) {
            throw new CurrencyException("Currency type are different!");
        }
        return new Currency(amount().add(amount.amount()), amount.type());
    }

    public BigDecimal amount() {
        return amount;
    }

    public CurrencyType type() {
        return currencyType;
    }

    public String formatted() {
        return NumberFormat.getCurrencyInstance(currencyType.country()).format(amount);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Currency that) {
            return Objects.equals(amount(), that.amount()) &&
                    Objects.equals(type(), that.type());
        }
        return false;
    }
}
