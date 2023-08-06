package br.com.market.place.domain.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends DomainException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
