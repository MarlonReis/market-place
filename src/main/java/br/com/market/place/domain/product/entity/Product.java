package br.com.market.place.domain.product.entity;

import br.com.market.place.domain.product.value.ProductId;
import br.com.market.place.domain.product.value.Quantity;
import br.com.market.place.domain.product.value.Title;
import br.com.market.place.domain.shared.validator.DomainValidator;
import br.com.market.place.domain.shared.value.CreateAt;
import br.com.market.place.domain.shared.value.Currency;
import br.com.market.place.domain.shared.value.UpdateAt;
import jakarta.persistence.*;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

@Entity
public class Product {
    @Id
    private ProductId id;
    @NotNull(message = "Attribute title is required!")
    private Title title;

    @NotNull(message = "Attribute tag is required!")
    @NotEmpty(message = "Attribute tag is required!")
    private String tag;
    @NotNull(message = "Attribute price is required!")
    @AttributeOverrides({@AttributeOverride(name = "amount", column = @Column(name = "price"))})
    private Currency price;

    @NotNull(message = "Attribute quantity is required!")
    private Quantity quantity;

    @Column(updatable = false, nullable = false)
    @AttributeOverrides({@AttributeOverride(name = "date", column = @Column(name = "createAt"))})
    private CreateAt createAt;

    @AttributeOverrides({@AttributeOverride(name = "date", column = @Column(name = "updateAt"))})
    private UpdateAt updateAt;

    public Product() {
    }

    public Product(Title title, String tag, Currency price, Quantity quantity) {
        this.title = title;
        this.tag = tag;
        this.price = price;
        this.quantity = quantity;
        new DomainValidator().validate(this);
    }

    @PrePersist
    protected void prePersist() {
        this.id = new ProductId();
        this.createAt = new CreateAt();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updateAt = new UpdateAt();
    }

    public void subtract(Quantity quantity) {
        setQuantity(getQuantity().subtract(quantity));
    }

    public void add(Quantity quantity) {
        setQuantity(getQuantity().add(quantity));
    }

    public ProductId getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    protected void setTitle(Title title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    protected void setTag(String tag) {
        this.tag = tag;
    }

    public CreateAt getCreateAt() {
        return createAt;
    }

    public Currency getPrice() {
        return price;
    }

    protected void setPrice(Currency price) {
        this.price = price;
    }

    public UpdateAt getUpdateAt() {
        return updateAt;
    }

    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    public static final class Builder {
        private Product product;

        private Builder() {
            product = new Product();
        }

        public static Builder build() {
            return new Builder();
        }

        public Builder withTitle(String title, String description) {
            product.setTitle(new Title(title, description));
            return this;
        }

        public Builder withTag(String tag) {
            product.setTag(tag);
            return this;
        }

        public Builder withQuantity(int quantity) {
            product.setQuantity(new Quantity(quantity));
            return this;
        }

        public Builder withPrice(Currency currency) {
            product.setPrice(currency);
            return this;
        }


        public Product now() {
            return new Product(
                    product.getTitle(),
                    product.getTag(),
                    product.getPrice(),
                    product.getQuantity()
            );
        }
    }
}
