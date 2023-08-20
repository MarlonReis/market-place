package br.com.market.place.domain.customer.entity;

import br.com.market.place.domain.customer.value.*;
import br.com.market.place.domain.payment.entity.Payment;
import br.com.market.place.domain.shared.exception.InvalidDataException;
import br.com.market.place.domain.shared.value.Address;
import br.com.market.place.domain.shared.value.CreateAt;
import br.com.market.place.domain.shared.value.UpdateAt;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(indexes = {
        @Index(columnList = "document", name = "document_index", unique = true),
        @Index(columnList = "email", name = "email_index", unique = true)
})
public abstract class Customer {
    @Id
    private CustomerId id;
    private Name name;
    @Column(unique = true)
    private Email email;
    private Telephone telephone;
    @Column(unique = true)
    private Document document;
    private Address address;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Set<Payment> payments;

    @Column(updatable = false, nullable = false)
    @AttributeOverrides({@AttributeOverride(name = "date", column = @Column(name = "createAt"))})
    private CreateAt createAt;

    @AttributeOverrides({@AttributeOverride(name = "date", column = @Column(name = "updateAt"))})
    private UpdateAt updateAt;


    protected Customer() {
    }

    @PrePersist
    protected final void prePersist() {
        this.createAt = new CreateAt();
        this.id = new CustomerId();
    }

    @PreUpdate
    protected final void preUpdate() {
        this.updateAt = new UpdateAt();
    }

    public final void changeAddress(@NotNull Address address) {
        if (address == null) {
            throw new InvalidDataException("Attribute address is required!");
        }
        setAddress(address);
    }


    public final void changeEmail(String email) {
        setEmail(new Email(email));
    }


    public final void changeTelephone(String telephone) {
        setTelephone(new Telephone(telephone));
    }

    public abstract boolean isLegalPerson();

    public CustomerId getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Telephone getTelephone() {
        return telephone;
    }

    public Document getDocument() {
        return document;
    }

    public Address getAddress() {
        return address;
    }

    public CreateAt getCreateAt() {
        return createAt;
    }

    public UpdateAt getUpdateAt() {
        return updateAt;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    protected void setName(Name name) {
        this.name = name;
    }

    protected void setEmail(Email email) {
        this.email = email;
    }

    protected void setTelephone(Telephone telephone) {
        this.telephone = telephone;
    }

    protected void setDocument(Document document) {
        this.document = document;
    }

    protected void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Customer that) {
            return Objects.equals(getId(), that.getId());
        }
        return false;
    }
}
