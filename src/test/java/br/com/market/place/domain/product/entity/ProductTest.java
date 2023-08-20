package br.com.market.place.domain.product.entity;

import br.com.market.place.domain.product.value.Quantity;
import br.com.market.place.domain.product.value.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;

class ProductTest {
    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.Builder.build().withTag("Electronic").
                withTitle("Title", "Any description").
                withQuantity(10).
                now();
    }

    @Test
    void shouldCreateProductInstance() {
        assertThat(product.getCreateAt(), Matchers.nullValue());
        assertThat(product.getUpdateAt(), Matchers.nullValue());
        assertThat(product.getId(), Matchers.nullValue());
        assertThat(product.getTag(), Matchers.is("Electronic"));
        assertThat(product.getTitle(), Matchers.is(new Title("Title", "Any description")));
        assertThat(product.getQuantity(), Matchers.is(new Quantity(10)));
    }

    @Test
    void shouldCreateIdWhenCallPrePersist() {
        product.prePersist();

        assertThat(product.getCreateAt(), Matchers.notNullValue());
        assertThat(product.getUpdateAt(), Matchers.nullValue());
        assertThat(product.getId(), Matchers.notNullValue());
    }

    @Test
    void shouldCreateUpdateAtWhenCallPreUpdate() {
        product.preUpdate();

        assertThat(product.getUpdateAt(), Matchers.notNullValue());
        assertThat(product.getCreateAt(), Matchers.nullValue());
        assertThat(product.getId(), Matchers.nullValue());
    }

}