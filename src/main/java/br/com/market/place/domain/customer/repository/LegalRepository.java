package br.com.market.place.domain.customer;

import br.com.market.place.domain.customer.entity.Legal;
import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.customer.value.Document;
import br.com.market.place.domain.customer.value.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LegalRepository extends JpaRepository<Legal, CustomerId> {
    Optional<Legal> findByDocument(Document document);
    Optional<Legal> findByEmail(Email email);

}
