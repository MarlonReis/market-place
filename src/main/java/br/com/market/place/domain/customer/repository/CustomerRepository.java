package br.com.market.place.domain.customer.repository;

import br.com.market.place.domain.customer.entity.Customer;
import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.customer.value.Document;
import br.com.market.place.domain.customer.value.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, CustomerId> {
    Optional<Customer> findCustomerByDocument(Document document);
    Optional<Customer> findCustomerByEmail(Email email);



}
