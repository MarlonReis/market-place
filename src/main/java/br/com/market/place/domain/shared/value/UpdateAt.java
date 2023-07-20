package br.com.market.place.domain.shared.value;

import jakarta.persistence.Embeddable;


@Embeddable
public final class UpdateAt extends DomainDate {
    public UpdateAt() {
        super();
    }
}