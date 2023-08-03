package br.com.market.place.domain.customer.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Embeddable
public class BirthDate {
    private LocalDate birthDate;

    protected BirthDate() {
    }

    public BirthDate(String date) {
        if (date == null) throw new InvalidDataException("Attribute birthDate is required!");

        if (!date.matches("(0[1-9]|[12][0-9]|3[01])(\\/)(0[1-9]|1[1,2])(\\/)(19|20)\\d{2}")) {
            throw new InvalidDataException("Attribute birthDate is invalid!");
        }

        this.birthDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public LocalDate birthDate() {
        return birthDate;
    }

    public String dateFormatted() {
        return birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof BirthDate date) {
            return Objects.equals(birthDate, date.birthDate());
        }
        return false;
    }
}
