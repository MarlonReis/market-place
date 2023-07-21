package br.com.market.place.domain.payment.entity;

import br.com.market.place.domain.customer.entity.Customer;
import br.com.market.place.domain.payment.constant.PaymentStatus;
import br.com.market.place.domain.payment.service.CancelPaymentService;
import br.com.market.place.domain.payment.service.RunPaymentService;
import br.com.market.place.domain.payment.value.PaymentId;
import br.com.market.place.domain.shared.value.Address;
import br.com.market.place.domain.shared.value.CreateAt;
import br.com.market.place.domain.shared.value.Currency;
import br.com.market.place.domain.shared.value.UpdateAt;
import jakarta.persistence.*;
import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import java.util.Objects;


@Entity
@Polymorphism(type = PolymorphismType.EXPLICIT)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Payment {
    @EmbeddedId
    private PaymentId id;
    private Currency amount;
    private Address address;
    private PaymentStatus status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "customer_fk"))
    private Customer customer;

    @Column(updatable = false, nullable = false)
    @AttributeOverrides({@AttributeOverride(name = "date", column = @Column(name = "createAt"))})
    private CreateAt createAt;

    @AttributeOverrides({@AttributeOverride(name = "date", column = @Column(name = "updateAt"))})
    private UpdateAt updateAt;

    public Payment() {
    }

    public Payment(Currency amount, Address address, PaymentStatus status, Customer customer) {
        this.amount = amount;
        this.address = address;
        this.status = status;
        this.customer = customer;
    }

    public abstract void pay(RunPaymentService payment);

    public abstract void cancelPayment(CancelPaymentService payment);

    @PrePersist
    protected final void prePersist() {
        id = new PaymentId();
        createAt = new CreateAt();
    }

    @PreUpdate
    protected final void preUpdate() {
        updateAt = new UpdateAt();
    }

    public PaymentId getId() {
        return id;
    }

    public Currency getAmount() {
        return amount;
    }

    public Address getAddress() {
        return address;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public CreateAt getCreateAt() {
        return createAt;
    }

    public UpdateAt getUpdateAt() {
        return updateAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    protected void setAmount(Currency amount) {
        this.amount = amount;
    }

    protected void setAddress(Address address) {
        this.address = address;
    }

    protected void setStatus(PaymentStatus status) {
        this.status = status;
    }

    protected void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Payment that){
            return Objects.equals(getId(),that.getId());
        }
        return false;
    }
}
