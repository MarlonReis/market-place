package br.com.market.place.domain.order.value;

import br.com.market.place.domain.shared.value.EntityId;
import jakarta.persistence.Embeddable;

@Embeddable
public class OrderItemId extends EntityId {

    public OrderItemId() {
        super();
    }
}
