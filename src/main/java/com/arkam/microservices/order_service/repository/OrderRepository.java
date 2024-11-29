package com.arkam.microservices.order_service.repository;

import com.arkam.microservices.order_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Override
    Optional<Order> findById(Long id);
    Optional<Order> findByUserId(Long userId);
    Optional<Order> findBySkuCode(String skuCode);
}
