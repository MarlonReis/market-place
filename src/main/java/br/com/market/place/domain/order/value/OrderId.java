package br.com.market.place.domain.order.value;

import br.com.market.place.domain.shared.value.EntityId;
import jakarta.persistence.Embeddable;

@Embeddable
public class OrderId extends EntityId {
    public OrderId() {
        super();
    }
}
