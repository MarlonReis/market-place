package br.com.market.place.domain.payment.repository;

import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.payment.entity.Billet;
import br.com.market.place.domain.payment.entity.CredCard;
import br.com.market.place.domain.payment.entity.Payment;
import br.com.market.place.domain.payment.value.PaymentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, PaymentId> {

    @Query("SELECT B FROM Billet B  WHERE B.customer.id=:customerId")
    Streamable<Billet> findBilletByCustomerId(@Param("customerId") CustomerId customerId);
    @Query("SELECT C FROM CredCard C  WHERE C.customer.id=:customerId")
    Streamable<CredCard> findCredCardByCustomerId(@Param("customerId") CustomerId customerId);
}
