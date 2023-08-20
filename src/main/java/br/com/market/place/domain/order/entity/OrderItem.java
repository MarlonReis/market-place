package br.com.market.place.domain.order.entity;

import br.com.market.place.domain.order.value.OrderItemId;
import br.com.market.place.domain.product.entity.Product;
import br.com.market.place.domain.product.value.Quantity;
import br.com.market.place.domain.shared.exception.InvalidDataException;
import br.com.market.place.domain.shared.value.CreateAt;
import br.com.market.place.domain.shared.value.Currency;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    private OrderItemId id;

    @ManyToOne()
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Quantity quantity;

    @AttributeOverrides({@AttributeOverride(name = "amount", column = @Column(name = "price"))})
    private Currency totalAmount;


    @Column(updatable = false, nullable = false)
    @AttributeOverrides({@AttributeOverride(name = "date", column = @Column(name = "createAt"))})
    private CreateAt createAt;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false, referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "order_fk"))
    private Order order;

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
        this.totalAmount = product.getPrice().multiply(quantity.value());
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

    public void addOrder(Order order) {
        this.order = order;
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

    public Currency getTotalAmount() {
        return totalAmount;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof OrderItem that) {
            return Objects.equals(getId(), that.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


}
