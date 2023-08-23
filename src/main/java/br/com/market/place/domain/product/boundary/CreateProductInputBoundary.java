package br.com.market.place.domain.product.boundary;

import br.com.market.place.domain.product.entity.Product;
import br.com.market.place.domain.shared.value.Currency;
import jakarta.validation.constraints.NotNull;

public record CreateProductInputBoundary(
        @NotNull(message = "Attribute title is required!")
        String title,
        @NotNull(message = "Attribute description is required!")
        String description,
        @NotNull(message = "Attribute tag is required!")
        String tag,
        @NotNull(message = "Attribute quantity is required!")
        Integer quantity,
        @NotNull(message = "Attribute price is required!")
        String price,
        @NotNull(message = "Attribute currencyType is required!")
        String currencyType
) {
    public Product toEntity() {
        return Product.Builder.build()
                .withTag(tag)
                .withQuantity(quantity)
                .withTitle(title, description)
                .withPrice(new Currency(price, currencyType))
                .now();
    }

}
