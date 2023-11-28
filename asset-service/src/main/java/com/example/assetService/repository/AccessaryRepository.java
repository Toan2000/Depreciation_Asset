package com.example.assetService.repository;

import com.example.assetService.model.Accessary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessaryRepository extends JpaRepository<Accessary, Long> {
    List<Accessary> findByAssetId(Long id);
}
