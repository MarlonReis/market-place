package br.com.market.place.domain.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class CreateException extends DomainException {
    public CreateException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
