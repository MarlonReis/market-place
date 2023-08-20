package br.com.market.place.domain.order.entity;

import br.com.market.place.domain.order.value.OrderItemId;
import br.com.market.place.domain.product.entity.Product;
import br.com.market.place.domain.product.value.Quantity;
import br.com.market.place.domain.shared.exception.InvalidDataException;
import br.com.market.place.domain.shared.validator.ValueObjectValidator;
import br.com.market.place.domain.shared.value.CreateAt;
import jakarta.persistence.*;


public class OrderItem {
    @Id
    private OrderItemId id;
    private Product product;

    private Quantity quantity;

    @Column(updatable = false, nullable = false)
    @AttributeOverrides({@AttributeOverride(name = "date", column = @Column(name = "createAt"))})
    private CreateAt createAt;

    protected OrderItem() {
    }

    public OrderItem(Product product, Quantity quantity) {
        if (product == null) {
            throw new InvalidDataException("Product is required!");
        }
        if (quantity == null) {
            throw new InvalidDataException("Quantity is required!");
        }

        product.subtract(quantity);
        this.product = product;
        this.quantity = quantity;
    }

    @PrePersist
    protected void prePersist() {
        id = new OrderItemId();
        createAt = new CreateAt();
    }

    @PreRemove
    protected void rollBackProductQuantity() {
        product.add(getQuantity());
        quantity = quantity.subtract(getQuantity());
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public CreateAt getCreateAt() {
        return createAt;
    }

    public OrderItemId getId() {
        return id;
    }


}
