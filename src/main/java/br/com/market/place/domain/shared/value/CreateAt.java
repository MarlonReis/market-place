package br.com.market.place.domain.shared.value;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class CreateAt extends DomainDate {
    public CreateAt() {
        super();
    }


    @Override
    @Column(updatable = false, nullable = false)
    @AttributeOverrides({@AttributeOverride(name = "date", column = @Column(name = "createAt"))})
    public LocalDateTime getDate() {
        return super.getDate();
    }
}
