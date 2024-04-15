package com.xiaodao.jta.repository.order;

import com.xiaodao.jta.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
