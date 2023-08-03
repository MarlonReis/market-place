package br.com.market.place.domain.payment.value;

import br.com.market.place.domain.shared.valodator.ValueObjectValidator;
import jakarta.persistence.Embeddable;
import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.NotNull;

import java.util.Objects;


@Embeddable
public class CardPan {
    @NotNull(message = "Attribute cardNumber is required!")
    @MatchPattern(pattern = "^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$"
            , message = "Attribute cardNumber is invalid!")
    private String cardNumber;

    protected CardPan() {
    }

    public CardPan(String card) {
        this.cardNumber = card;
        new ValueObjectValidator().validate(this);
    }

    public String cardPan() {
        return this.cardNumber;
    }

    public String maskedCard() {
        return cardNumber.substring(0, 4).concat("********").concat(cardNumber.substring(12));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof CardPan that) {
            return Objects.equals(maskedCard(), that.maskedCard());
        }
        return false;
    }

}
