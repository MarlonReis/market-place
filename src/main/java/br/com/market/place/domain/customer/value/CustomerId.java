package br.com.market.place.domain.customer.value;

import br.com.market.place.domain.shared.value.EntityId;
import jakarta.persistence.Embeddable;

@Embeddable
public class CustomerId extends EntityId {
    public CustomerId(){
        super();
    }
    public CustomerId(String id){
        super(id);
    }
}
