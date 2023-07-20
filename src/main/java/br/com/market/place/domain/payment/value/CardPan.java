package br.com.market.place.domain.payment.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import jakarta.persistence.Embeddable;

import java.util.Objects;

import static am.ik.yavi.builder.StringValidatorBuilder.of;

@Embeddable
public class CardPan {
    private String cardNumber;

    protected CardPan() {
    }

    public CardPan(String card) {
        var cardVal = of("cardNumber", s -> s
                .notNull().message("Attribute cardNumber is required!")
                .luhn().message("Attribute cardNumber is invalid!")
        ).build().validate(card);
        this.cardNumber = cardVal.orElseThrow(InvalidDataException::new);
    }

    public String cardPan() {
        return this.cardNumber;
    }

    public String maskedCard(){
        return cardNumber.substring(0,4).concat("********").concat(cardNumber.substring(12));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof CardPan that){
            return Objects.equals(maskedCard(),that.maskedCard());
        }
        return false;
    }

}
