package br.com.market.place.domain.customer.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import jakarta.persistence.Embeddable;

import java.util.Objects;

import static am.ik.yavi.builder.StringValidatorBuilder.of;
@Embeddable
public class Email {
	private String email;

	public Email(String name) {
		var nameVal = of("email", s -> s.notBlank()
				.message("Attribute email is required!")
				.email().message("Attribute email is invalid!")
		).build().validate(name);
		this.email = nameVal.orElseThrow(InvalidDataException::new);
	}

	protected Email() {
	}

	public String email() {
		return this.email;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (o instanceof Email that){
			return Objects.equals(email, that.email());
		}
		return false;
	}
}
