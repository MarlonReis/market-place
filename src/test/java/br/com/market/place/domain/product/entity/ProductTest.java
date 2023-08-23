package br.com.market.place.domain.product.entity;

import br.com.market.place.domain.product.value.Quantity;
import br.com.market.place.domain.product.value.Title;
import br.com.market.place.domain.shared.exception.InvalidDataException;
import br.com.market.place.domain.shared.value.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;

class ProductTest {
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

    @Test
    void shouldThrowsInvalidDataExceptionWhenTitleIsNull() {
        var build = Product.Builder.build().withTag("Electronic")
                .withQuantity(10).withPrice(new Currency("10.00", "BRL"));

        var exception = assertThrows(InvalidDataException.class, build::now);
        assertThat(exception.getMessage(), Matchers.is("Attribute title is required!"));
    }

    @NullSource
    @EmptySource
    @ParameterizedTest
    void shouldThrowsInvalidDataExceptionWhenTagIsNullOrEmpty(String tag) {
        var build = Product.Builder.build().withTag(tag).withTitle("Any", "Any")
                .withQuantity(10).withPrice(new Currency("10.00", "BRL"));

        var exceptionEmpty = assertThrows(InvalidDataException.class, build::now);
        assertThat(exceptionEmpty.getMessage(), Matchers.is("Attribute tag is required!"));
    }

    @Test
    void shouldThrowsInvalidDataExceptionWhenQuantityIsNull() {
        var build = Product.Builder.build().withTag("any").withTitle("Any", "Any")
                .withPrice(new Currency("10.00", "BRL"));

        var exceptionEmpty = assertThrows(InvalidDataException.class, build::now);
        assertThat(exceptionEmpty.getMessage(), Matchers.is("Attribute quantity is required!"));
    }

    @Test
    void shouldThrowsInvalidDataExceptionWhenPriceIsNull() {
        var build = Product.Builder.build().withTag("any").withTitle("Any", "Any").withQuantity(10);

        var exceptionEmpty = assertThrows(InvalidDataException.class, build::now);
        assertThat(exceptionEmpty.getMessage(), Matchers.is("Attribute price is required!"));
    }

    @Test
    void shouldChangeProductPrice() {
        product.changePrice("03.39", "USD");
        assertThat(product.getPrice(), Matchers.is(new Currency("3.39", "USD")));
    }

    @Test
    void shouldChangeProductTag() {
        product.changeTag("XXXXXXXX");
        assertThat(product.getTag(), Matchers.is("XXXXXXXX"));
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenReceiveInvalidTag() {
        var exception = assertThrows(InvalidDataException.class, () -> product.changeTag(""));
        assertThat(exception.getMessage(), Matchers.is("Attribute tag is required!"));
    }

}