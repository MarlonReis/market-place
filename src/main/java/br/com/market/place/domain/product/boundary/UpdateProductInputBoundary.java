package br.com.market.place.domain.product.boundary;

import jakarta.validation.constraints.NotNull;

public record UpdateProductInputBoundary(
        @NotNull(message = "Attribute id is required!")
        String id,
        @NotNull(message = "Attribute title is required!")
        String title,
        @NotNull(message = "Attribute description is required!")
        String description,
        @NotNull(message = "Attribute tag is required!")
        String tag,
        @NotNull(message = "Attribute price is required!")
        String price,
        @NotNull(message = "Attribute currencyType is required!")
        String currencyType
) {
}
