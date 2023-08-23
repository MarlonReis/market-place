package br.com.market.place.domain.product.service;

import br.com.market.place.domain.product.boundary.AddProductQuantityInputBoundary;
import br.com.market.place.domain.product.boundary.CreateProductInputBoundary;
import br.com.market.place.domain.product.boundary.ReadProductOutputBoundary;
import br.com.market.place.domain.product.boundary.UpdateProductInputBoundary;
import br.com.market.place.domain.product.value.ProductId;

import java.util.Set;

public interface ProductService {
    void create(CreateProductInputBoundary product);

    void update(UpdateProductInputBoundary product);

    void addQuantity(AddProductQuantityInputBoundary product);

    Set<ReadProductOutputBoundary> findAllProducts();

    ReadProductOutputBoundary findProductById(ProductId id);
}
