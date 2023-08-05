package br.com.market.place.domain.payment.repository;

import br.com.market.place.domain.customer.value.CustomerId;
import br.com.market.place.domain.payment.entity.Payment;
import br.com.market.place.domain.payment.value.PaymentId;
import br.com.market.place.domain.shared.value.DueDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, PaymentId> {
    Set<Payment> findPaymentByCustomerId(CustomerId id);
}
