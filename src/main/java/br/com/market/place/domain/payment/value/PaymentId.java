package br.com.market.place.domain.payment.value;

import br.com.market.place.domain.shared.value.EntityId;
import jakarta.persistence.Embeddable;

@Embeddable
public class PaymentId extends EntityId {
    public PaymentId(){
        super();
    }
}
