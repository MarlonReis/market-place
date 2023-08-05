package br.com.market.place.domain.shared.value;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Embeddable
public final class DueDate extends DomainDate {
    protected DueDate() {
        super();
    }

    public DueDate(int expireInDays) {
        super(LocalDateTime.now().plusDays(expireInDays));
    }



    @Override
    public String dateFormatted() {
        return getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof DueDate that) {
            return Objects.equals(getDate().getDayOfMonth(), that.getDate().getDayOfMonth()) &&
                    Objects.equals(getDate().getMonth(), that.getDate().getMonth()) &&
                    Objects.equals(getDate().getYear(), that.getDate().getYear());
        }
        return false;
    }
}
