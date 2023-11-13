package com.example.orderservicems.repository;

import com.example.orderservicems.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
