package br.com.market.place.domain.product.boundary;

import jakarta.validation.constraints.NotNull;

public record AddProductQuantityInputBoundary(
        @NotNull(message = "Attribute id is required!")
        String id,

        @NotNull(message = "Attribute quantity is required!")
        Integer quantity
) {
}
