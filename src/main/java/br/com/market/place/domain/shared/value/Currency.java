package br.com.market.place.domain.shared.value;

import br.com.market.place.domain.shared.constant.CurrencyType;
import br.com.market.place.domain.shared.exception.CurrencyException;
import br.com.market.place.domain.shared.exception.InvalidDataException;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Objects;

import static am.ik.yavi.builder.StringValidatorBuilder.of;

import am.ik.yavi.builder.ObjectValidatorBuilder;

@Embeddable
public class Currency {
    private BigDecimal amount;
    private CurrencyType currencyType;

    public Currency(String amount, CurrencyType type) {
        var amountVal = of("amount", s -> s
                .notEmpty().message("Attribute amount is required!")
                .isBigDecimal().message("Attribute amount is invalid!")
        ).build().validate(amount);

        if (type == null) throw new InvalidDataException("Attribute currencyType is required!");

        this.amount = new BigDecimal(amountVal.orElseThrow(InvalidDataException::new));
        this.currencyType = type;
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
