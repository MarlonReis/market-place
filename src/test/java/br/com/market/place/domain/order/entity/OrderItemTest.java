package br.com.market.place.domain.order.entity;

import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.product.entity.Product;
import br.com.market.place.domain.product.value.Quantity;
import br.com.market.place.domain.shared.exception.InvalidDataException;
import br.com.market.place.domain.shared.value.Address;
import br.com.market.place.domain.shared.value.Currency;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class OrderItemTest {
    private Product product;
    private Address address;


    @BeforeEach
    void setUp() {

        product = Product.Builder.build().
                withQuantity(10).withTag("Any").
                withPrice(new Currency("10.00", "BRL")).
                withTitle("Any Title", "Any description").
                now();
        address = Address.Builder.build().withCity("London").withStreet("Baker Street")
                .withNumber("221").withComponent("B").withZipCode("37540232").now();

    }

    @Test
    void shouldReturnItemProduct() {
        OrderItem item = new OrderItem(product, new Quantity(10));

        assertThat(item.getQuantity(), Matchers.is(new Quantity(10)));
        assertThat(item.getProduct(), Matchers.notNullValue());
        assertThat(item.getOrder(), Matchers.nullValue());
        assertThat(item.getTotalAmount(), Matchers.is(new Currency("100.00", "BRL")));
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

        Order order = Order.Builder.build().withCustomerId(new CustomerId()).withDeliveryAddress(address)
                .withItems(Set.of(item)).now();
        item.addOrder(order);

        assertThat(item.getId(), Matchers.notNullValue());
        assertThat(item.getCreateAt(), Matchers.notNullValue());
        assertThat(item.getOrder(), Matchers.is(order));
    }

    @Test
    void shouldRollBackProductQuantityWhenCallOrderItem() {
        OrderItem item = new OrderItem(product, new Quantity(10));
        item.rollBackProductQuantity();

        assertThat(item.getQuantity(), Matchers.is(new Quantity(0)));
        assertThat(product.getQuantity(), Matchers.is(new Quantity(10)));
    }

    @Test
    void shouldReturnTrueWhenBothObjectItemAreEquals() {
        UUID id = UUID.fromString("1d6d86e0-979e-41be-bde5-36978c23435f");
        OrderItem itemOne = new OrderItem(product, new Quantity(1));
        OrderItem itemTwo = new OrderItem(product, new Quantity(1));

        try (MockedStatic<UUID> mocked = Mockito.mockStatic(UUID.class)) {
            mocked.when(UUID::randomUUID).thenReturn(id);
            itemTwo.prePersist();
            itemOne.prePersist();

            assertThat(itemOne.equals(itemTwo), Matchers.is(true));
            assertThat(itemOne.hashCode(), Matchers.is(itemTwo.hashCode()));
        }

        itemOne.prePersist();
        assertThat(itemOne.equals(itemTwo), Matchers.is(false));
        assertThat(itemOne.hashCode(), Matchers.not(itemTwo.hashCode()));
        assertThat(itemOne.equals(new Object()), Matchers.is(false));
        assertThat(itemOne.equals(null), Matchers.is(false));
    }

}