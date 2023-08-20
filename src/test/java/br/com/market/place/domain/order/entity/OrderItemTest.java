package br.com.market.place.domain.order.entity;

import br.com.market.place.domain.product.entity.Product;
import br.com.market.place.domain.product.value.Quantity;
import br.com.market.place.domain.shared.exception.InvalidDataException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class OrderItemTest {
    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.Builder.build().
                withQuantity(10).withTag("Any").
                withTitle("Any Title", "Any description").
                now();
    }

    @Test
    void shouldReturnItemProduct() {
        OrderItem item = new OrderItem(product, new Quantity(10));

        assertThat(item.getQuantity(), Matchers.is(new Quantity(10)));
        assertThat(item.getProduct(), Matchers.notNullValue());
        assertThat(product.getQuantity(), Matchers.is(new Quantity(0)));
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenOrderItemReceiveNull() {
        var productNull = assertThrows(InvalidDataException.class, () -> new OrderItem(null, new Quantity(10)));
        var quantityNull = assertThrows(InvalidDataException.class, () -> new OrderItem(product, null));

        assertThat(productNull.getMessage(), Matchers.is("Product is required!"));
        assertThat(quantityNull.getMessage(), Matchers.is("Quantity is required!"));
    }

    @Test
    void shouldReturnNullWhenUseOrderItemDefaultConstructor() {
        OrderItem orderItem = new OrderItem();

        assertThat(orderItem.getProduct(), Matchers.nullValue());
        assertThat(orderItem.getQuantity(), Matchers.nullValue());
    }

    @Test
    void shouldSetIdAndCreateAtWhenCallOrderItemPrePersist() {
        OrderItem item = new OrderItem(product, new Quantity(10));
        item.prePersist();

        assertThat(item.getId(), Matchers.notNullValue());
        assertThat(item.getCreateAt(), Matchers.notNullValue());
    }

    @Test
    void shouldRollBackProductQuantityWhenCallOrderItem() {
        OrderItem item = new OrderItem(product, new Quantity(10));
        item.rollBackProductQuantity();

        assertThat(item.getQuantity(), Matchers.is(new Quantity(0)));
        assertThat(product.getQuantity(), Matchers.is(new Quantity(10)));
    }

}