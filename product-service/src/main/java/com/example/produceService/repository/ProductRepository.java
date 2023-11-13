package com.example.produceService.repository;

import com.example.produceService.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource

public interface ProductRepository extends JpaRepository<Product, Long> {
}
