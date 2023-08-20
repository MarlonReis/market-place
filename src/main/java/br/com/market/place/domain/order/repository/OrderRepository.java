package br.com.market.place.domain.order.repository;

import br.com.market.place.domain.order.entity.Order;
import br.com.market.place.domain.order.value.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, OrderId> {
}
