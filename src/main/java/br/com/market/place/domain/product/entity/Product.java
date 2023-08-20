package br.com.market.place.domain.product.entity;

import br.com.market.place.domain.product.value.ProductId;
import br.com.market.place.domain.product.value.Quantity;
import br.com.market.place.domain.product.value.Title;
import br.com.market.place.domain.shared.value.CreateAt;
import br.com.market.place.domain.shared.value.UpdateAt;
import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    private ProductId id;
    private Title title;
    private String tag;

    private Quantity quantity;

    @Column(updatable = false, nullable = false)
    @AttributeOverrides({@AttributeOverride(name = "date", column = @Column(name = "createAt"))})
    private CreateAt createAt;

    @AttributeOverrides({@AttributeOverride(name = "date", column = @Column(name = "updateAt"))})
    private UpdateAt updateAt;

    @PrePersist
    protected void prePersist() {
        this.id = new ProductId();
        this.createAt = new CreateAt();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updateAt = new UpdateAt();
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

        public Product now() {
            return product;
        }
    }
}
