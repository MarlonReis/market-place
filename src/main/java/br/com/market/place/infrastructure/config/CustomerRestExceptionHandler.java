package br.com.market.place.infrastructure.config;

import br.com.market.place.domain.shared.boundary.ResponseBoundary;
import br.com.market.place.domain.shared.exception.DomainException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice()
public class CustomerRestExceptionHandler {
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ResponseBoundary> domainException(DomainException ex) {
        return ResponseEntity.status(ex.getStatus()).body(new ResponseBoundary<>(ex.getData(), false));
    }
}
