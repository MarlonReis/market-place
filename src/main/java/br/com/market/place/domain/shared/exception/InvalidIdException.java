package br.com.market.place.domain.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidIdException extends RuntimeException{
    public InvalidIdException(String message){
        super(message);
    }
}