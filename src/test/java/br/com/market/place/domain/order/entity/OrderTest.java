package br.com.market.place.domain.order.entity;

import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.product.entity.Product;
import br.com.market.place.domain.product.value.Quantity;
import br.com.market.place.domain.shared.exception.InvalidDataException;
import br.com.market.place.domain.shared.value.Address;
import br.com.market.place.domain.shared.value.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;

class OrderTest {
    private Set<OrderItem> orderItems;
    private Address address;
    private Order.Builder orderBuilder;

    @BeforeEach
    void setUp() {
        Product mouse = Product.Builder.build().withQuantity(10).withTag("Electronic").withPrice(new Currency("10.50", "BRL")).withTitle("Mouse", "Mouse USB").now();
        Product iPhone = Product.Builder.build().withQuantity(10).withTag("Mobile").withPrice(new Currency("10000.00", "BRL")).withTitle("iPhone", "iPhone 15 pro max").now();
        OrderItem mouseItem = new OrderItem(mouse, new Quantity(3));
        OrderItem iPhoneItem = new OrderItem(iPhone, new Quantity(3));
        mouseItem.prePersist();
        iPhoneItem.prePersist();

        orderItems = Set.of(mouseItem, iPhoneItem);
        address = Address.Builder.build().withCity("London").withStreet("Baker Street").withNumber("221").withComponent("B").withZipCode("37540232").now();

        orderBuilder = Order.Builder.build().withCustomerId(new CustomerId()).withDeliveryAddress(address).withItems(orderItems);
    }

    @Test
    void shouldReturnOrder() {
        Order order = orderBuilder.now();

        assertThat(order.getCreateAt(), Matchers.nullValue());
        assertThat(order.getUpdateAt(), Matchers.nullValue());
        assertThat(order.getCustomerId(), Matchers.notNullValue());
        assertThat(order.getDeliveryAddress(), Matchers.is(address));
        assertThat(order.getItems(), Matchers.hasSize(2));
        assertThat(order.getTotalAmount(), Matchers.is(new Currency("30031.50", "BRL")));
    }

    @Test
    void shouldSetOrderIdAndCreateAtWhenCallPrePersist() {
        Order order = orderBuilder.now();
        order.prePersist();

        assertThat(order.getId(), Matchers.notNullValue());
        assertThat(order.getCreateAt(), Matchers.notNullValue());
        assertThat(order.getUpdateAt(), Matchers.nullValue());
    }

    @Test
    void shouldSetUpdateAtWhenCallPreUpdate() {
        Order order = orderBuilder.now();
        order.preUpdate();

        assertThat(order.getUpdateAt(), Matchers.notNullValue());
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenCustomerIdIsNull() {
        var exception = assertThrows(InvalidDataException.class, () -> Order.Builder.build().withDeliveryAddress(address).withItems(orderItems).now());
        assertThat(exception.getMessage(), Matchers.is("Attribute customerId is required!"));
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenAddressIsNull() {
        var exception = assertThrows(InvalidDataException.class, () -> Order.Builder.build().withCustomerId(new CustomerId()).withItems(orderItems).now());
        assertThat(exception.getMessage(), Matchers.is("Attribute deliveryAddress is required!"));
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenItemsIsNullOrEmpty() {
        var exceptionNull = assertThrows(InvalidDataException.class, () -> Order.Builder.build().now());
        var exceptionEmpty = assertThrows(InvalidDataException.class, () -> Order.Builder.build().now());

        assertThat(exceptionNull.getMessage(), Matchers.is("Items is requited!"));
        assertThat(exceptionEmpty.getMessage(), Matchers.is("Items is requited!"));
    }

    @Test
    void shouldReturnTrueWhenBothOrderAreEquals() {
        UUID id = UUID.randomUUID();

        Order orderOne = orderBuilder.now();
        Order orderTwo = orderBuilder.now();

        try (MockedStatic<UUID> mocked = Mockito.mockStatic(UUID.class)) {
            mocked.when(UUID::randomUUID).thenReturn(id);

            orderOne.prePersist();
            orderTwo.prePersist();

            assertThat(orderOne.equals(orderTwo), Matchers.is(true));
            assertThat(orderOne.hashCode(), Matchers.is(orderTwo.hashCode()));
        }

        orderTwo.prePersist();
        assertThat(orderOne.equals(orderTwo), Matchers.is(false));
        assertThat(orderOne.hashCode(), Matchers.not(orderTwo.hashCode()));
        assertThat(orderOne.equals(new Object()), Matchers.is(false));
        assertThat(orderOne.equals(null), Matchers.is(false));

    }


}