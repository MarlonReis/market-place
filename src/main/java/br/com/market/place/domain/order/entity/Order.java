package br.com.market.place.domain.order.entity;

import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.order.value.OrderId;
import br.com.market.place.domain.shared.exception.InvalidDataException;
import br.com.market.place.domain.shared.validator.DomainValidator;
import br.com.market.place.domain.shared.value.Address;
import br.com.market.place.domain.shared.value.CreateAt;
import br.com.market.place.domain.shared.value.Currency;
import br.com.market.place.domain.shared.value.UpdateAt;
import jakarta.persistence.*;
import net.sf.oval.constraint.NotNull;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    private OrderId id;

    @NotNull(message = "Attribute customerId is required!")
    @Column(updatable = false, nullable = false)
    @AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "customerId"))})
    private CustomerId customerId;

    @AttributeOverrides({@AttributeOverride(name = "amount", column = @Column(name = "totalAmount"))})
    private Currency totalAmount;
    @NotNull(message = "Attribute deliveryAddress is required!")
    private Address deliveryAddress;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private Set<OrderItem> items;

    @Column(updatable = false, nullable = false)
    @AttributeOverrides({@AttributeOverride(name = "date", column = @Column(name = "createAt"))})
    private CreateAt createAt;
    @AttributeOverrides({@AttributeOverride(name = "date", column = @Column(name = "updateAt"))})
    private UpdateAt updateAt;

    protected Order() {
    }

    public Order(CustomerId customerId, Address deliveryAddress, Set<OrderItem> items) {
        this.customerId = customerId;
        this.deliveryAddress = deliveryAddress;
        this.items = items;

        if (items == null || items.isEmpty()) {
            throw new InvalidDataException("Items is requited!");
        }
        this.totalAmount = items.stream().map(OrderItem::getTotalAmount).reduce(Currency::add)
                .orElseThrow();
        new DomainValidator().validate(this);
    }

    @PrePersist
    protected void prePersist() {
        id = new OrderId();
        createAt = new CreateAt();
    }

    @PreUpdate
    protected void preUpdate() {
        updateAt = new UpdateAt();
    }

    public OrderId getId() {
        return id;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Currency getTotalAmount() {
        return totalAmount;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public CreateAt getCreateAt() {
        return createAt;
    }

    public UpdateAt getUpdateAt() {
        return updateAt;
    }


    protected void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }

    protected void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    protected void setItems(Set<OrderItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof Order that) {
            return Objects.equals(getId(), that.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public static final class Builder {
        private Order order;

        private Builder() {
            order = new Order();
        }

        public static Builder build() {
            return new Builder();
        }

        public Builder withCustomerId(CustomerId customerId) {
            order.setCustomerId(customerId);
            return this;
        }

        public Builder withDeliveryAddress(Address deliveryAddress) {
            order.setDeliveryAddress(deliveryAddress);
            return this;
        }

        public Builder withItems(Set<OrderItem> items) {
            order.setItems(items);
            return this;
        }

        public Order now() {
            return new Order(order.getCustomerId(), order.getDeliveryAddress(), order.getItems());
        }
    }
}
