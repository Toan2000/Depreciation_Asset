package com.example.assetService.repository;

import com.example.assetService.model.UpdateHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdateHistoryRepository extends JpaRepository<UpdateHistory, Long> {
}
