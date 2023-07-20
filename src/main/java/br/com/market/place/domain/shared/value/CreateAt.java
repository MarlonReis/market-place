package br.com.market.place.domain.shared.value;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public final class CreateAt extends DomainDate {
    public CreateAt() {
        super();
    }


    @Override
    public LocalDateTime getDate() {
        return super.getDate();
    }
}
