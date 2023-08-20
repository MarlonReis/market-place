package br.com.market.place.domain.product.repository;

import br.com.market.place.domain.product.entity.Product;
import br.com.market.place.domain.product.value.ProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, ProductId> {
}
