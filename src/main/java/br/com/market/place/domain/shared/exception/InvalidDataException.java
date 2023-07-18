package br.com.market.place.domain.shared.exception;

import am.ik.yavi.core.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidDataException extends RuntimeException{
	public InvalidDataException(List<ConstraintViolation> va){
		super(va.stream().map(ConstraintViolation::message)
				.collect(Collectors.joining(", ")));
	}
}
