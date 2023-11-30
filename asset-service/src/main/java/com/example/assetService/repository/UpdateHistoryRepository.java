package com.example.assetService.repository;

import com.example.assetService.model.UpdateHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface UpdateHistoryRepository extends JpaRepository<UpdateHistory, Long> {
//    public List<UpdateHistory> findByAssetId(Long assetId, Pageable pageable);
}
