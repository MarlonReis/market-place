package br.com.market.place.domain.shared.boundary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResponseBoundary<T>(T data, boolean success) {
}
