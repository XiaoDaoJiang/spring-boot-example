package com.xiaodao.multids.repository.order;

import com.xiaodao.multids.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
