package br.com.market.place.domain.shared.exception;

public abstract class DomainException extends RuntimeException{
    public DomainException(String message) {
        super(message);
    }
}
