package br.com.market.place.domain.customer.entity;

import br.com.market.place.domain.customer.value.*;
import br.com.market.place.domain.shared.value.Address;
import br.com.market.place.domain.shared.value.CreateAt;
import br.com.market.place.domain.shared.value.UpdateAt;
import jakarta.persistence.*;

@MappedSuperclass
public sealed abstract class Customer permits Physical, Legal {
    @Id
    private CustomerId id;
    private Name name;
    @Column(unique = true)
    private Email email;
    private Telephone telephone;
    @Column(unique = true)
    private Document document;
    private Address address;
    @Column(updatable = false, nullable = false)
    @AttributeOverrides({@AttributeOverride(name = "date", column = @Column(name = "createAt"))})
    private CreateAt createAt;


    @AttributeOverrides({@AttributeOverride(name = "date", column = @Column(name = "updateAt"))})
    private UpdateAt updateAt;

    public Customer() {
    }

    @PrePersist
    private void prePersist() {
        this.createAt = new CreateAt();
        this.id = new CustomerId();
    }

    @PreUpdate
    private void preUpdate() {
        this.updateAt = new UpdateAt();
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


    protected final void setName(Name name) {
        this.name = name;
    }

    protected final void setEmail(Email email) {
        this.email = email;
    }

    protected final void setTelephone(Telephone telephone) {
        this.telephone = telephone;
    }

    protected final void setDocument(Document document) {
        this.document = document;
    }

    protected final void setAddress(Address address) {
        this.address = address;
    }

}
