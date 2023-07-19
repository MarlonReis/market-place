package br.com.market.place.domain.shared.value;

import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@MappedSuperclass
public abstract class DomainDate {
    private LocalDateTime date;

    protected DomainDate() {
        date = LocalDateTime.now();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public final String dateFormatted() {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof DomainDate that) {
            return Objects.equals(date, that.getDate());
        }
        return false;
    }

}
