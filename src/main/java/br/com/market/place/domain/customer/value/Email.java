package br.com.market.place.domain.customer.value;

import br.com.market.place.domain.shared.exception.InvalidDataException;
import jakarta.persistence.Embeddable;

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
}
