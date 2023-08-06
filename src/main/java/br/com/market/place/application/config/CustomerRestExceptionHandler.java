package br.com.market.place.application.config;

import br.com.market.place.domain.shared.boundary.ResponseBoundary;
import br.com.market.place.domain.shared.exception.DomainException;
import br.com.market.place.domain.shared.exception.data.MessageExceptionResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice()
public class CustomerRestExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(CustomerRestExceptionHandler.class);

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ResponseBoundary<MessageExceptionResponseData>> domainException(DomainException ex) {
        logger.warn("domainException:{}:{}", ex.getMessage(), ex.getData());
        return ResponseEntity.status(ex.getStatus()).body(new ResponseBoundary<>(ex.getData(), false));
    }


    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<ResponseBoundary<MessageExceptionResponseData>> missingRequestException(ServletRequestBindingException ex) {
        logger.warn("missingRequestException: 400:{}", ex.getMessage());
        final var data = new MessageExceptionResponseData(ex.getBody().getDetail());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(new ResponseBoundary<>(data, false));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseBoundary<MessageExceptionResponseData>> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
       final var message = ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        logger.warn("methodArgumentNotValidException: 400:{}", message);
        final var data = new MessageExceptionResponseData(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(new ResponseBoundary<>(data, false));
    }

    @ExceptionHandler(NestedRuntimeException.class)
    public ResponseEntity<ResponseBoundary<MessageExceptionResponseData>> nestedRuntimeException(NestedRuntimeException ex) {
        logger.warn("nestedRuntimeException: 400:{}", ex.getMessage());
        final var data = new MessageExceptionResponseData("Invalid request content.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(new ResponseBoundary<>(data, false));
    }
}
