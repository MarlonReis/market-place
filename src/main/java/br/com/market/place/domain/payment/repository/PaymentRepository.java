package br.com.market.place.domain.payment.repository;

import br.com.market.place.domain.payment.entity.Payment;
import br.com.market.place.domain.payment.value.PaymentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, PaymentId> {

}
