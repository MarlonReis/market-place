package br.com.market.place.domain.shared.value;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public final class UpdateAt extends DomainDate {
    public UpdateAt() {
        super();
    }

    @Override
    @AttributeOverrides({@AttributeOverride(name = "date", column = @Column(name = "updateAt"))})
    public LocalDateTime getDate() {
        return super.getDate();
    }
}