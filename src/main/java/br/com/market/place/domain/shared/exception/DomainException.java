package br.com.market.place.domain.shared.exception;

import br.com.market.place.domain.shared.exception.data.MessageExceptionResponseData;
import org.springframework.http.HttpStatus;

public abstract class DomainException extends RuntimeException {
    private final HttpStatus status;

    public DomainException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public final HttpStatus getStatus() {
        return status;
    }

    public final MessageExceptionResponseData getData(){
        return new MessageExceptionResponseData(getMessage());
    }

}
