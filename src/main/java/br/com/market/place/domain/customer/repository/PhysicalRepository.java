package br.com.market.place.domain.customer.repository;

import br.com.market.place.domain.customer.entity.Physical;
import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.customer.value.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhysicalRepository extends JpaRepository<Physical, CustomerId> {
    Optional<Physical> findPhysicalByDocument(Document document);
}
