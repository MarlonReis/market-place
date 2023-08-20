package br.com.market.place.domain.product.repository;

import br.com.market.place.domain.product.entity.Product;
import br.com.market.place.domain.product.value.Quantity;
import br.com.market.place.domain.shared.value.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest()
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {
    @Autowired
    private ProductRepository repository;
    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.Builder.build().withTag("Electronic").
                withTitle("Title", "Any description").
                withPrice(new Currency("10.0", "BRL")).
                withQuantity(10).
                now();
    }

    @Test
    void shouldReturnIdAndCreateAtWhenCreateProduct() {
        repository.saveAndFlush(product);

        assertThat(product.getId(), Matchers.notNullValue());
        assertThat(product.getCreateAt(), Matchers.notNullValue());
    }

    @Test
    void shouldReturnUpdateAtWhenUpdateProduct() {
        repository.saveAndFlush(product);
        product.add(new Quantity(10));
        repository.saveAndFlush(product);

        Product response = repository.findById(product.getId()).orElseThrow();

        assertThat(response.getId(), Matchers.is(product.getId()));
        assertThat(response.getUpdateAt(), Matchers.notNullValue());
        assertThat(response.getQuantity(), Matchers.is(new Quantity(20)));
    }

}