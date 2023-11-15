package com.example.assetService.repository;

import com.example.assetService.model.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AssetRepository extends JpaRepository<Asset,Long> {
    Page<Asset> findByDeptUsedId(Long id, Pageable pageable);
    Page<Asset> findByUserUsedId(Long id, Pageable pageable);
    Page<Asset> findByAssetStatus(Long status, Pageable pageable);
}
