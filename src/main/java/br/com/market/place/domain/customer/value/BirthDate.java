package br.com.market.place.domain.customer.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static am.ik.yavi.builder.StringValidatorBuilder.of;

@Embeddable
public class BirthDate {
    private LocalDate birthDate;

    protected BirthDate() {
    }

    public BirthDate(String date) {
        final var dateValid = of("birthDate", s -> s.notNull()
                .message("Attribute birthDate is required!")
                .pattern("(0[1-9]|[12][0-9]|3[01])(\\/)(0[1-9]|1[1,2])(\\/)(19|20)\\d{2}")
                .message("Attribute birthDate is invalid!")
        ).build().validate(date).orElseThrow(InvalidDataException::new);

        this.birthDate = LocalDate.parse(dateValid, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public LocalDate birthDate() {
        return birthDate;
    }

    public String dateFormatted() {
        return birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
