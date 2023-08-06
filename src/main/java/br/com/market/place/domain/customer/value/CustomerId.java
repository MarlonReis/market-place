package br.com.market.place.domain.customer.value;

import br.com.market.place.domain.shared.value.EntityId;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.UUID;

@Embeddable
public class CustomerId extends EntityId {

    public CustomerId(){
        super();
    }
    public CustomerId(String id){
        super(id);
    }
}
