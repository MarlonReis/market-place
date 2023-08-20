package br.com.market.place.domain.product.value;

import br.com.market.place.domain.shared.value.EntityId;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductId extends EntityId {
    public ProductId() {
        super();
    }

    public ProductId(String id) {
        super(id);
    }
}
