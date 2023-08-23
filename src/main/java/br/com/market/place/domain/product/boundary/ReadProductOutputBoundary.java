package br.com.market.place.domain.product.boundary;

public record ReadProductOutputBoundary(
        String id,
        String title,
        String description,
        String tag,
        int quantity,
        String price,
        String createAt
) {
}
