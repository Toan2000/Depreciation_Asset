package com.example.depreciationService.repository;

import com.example.depreciationService.model.Depreciation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepreciationRepository extends JpaRepository<Depreciation,Long> {
}
