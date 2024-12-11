package com.doziem.treading.repository;

import com.doziem.treading.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByUserId(Long userId);
}
