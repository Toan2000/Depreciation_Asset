package com.example.depreciationService.repository;

import com.example.depreciationService.model.Depreciation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepreciationRepository extends JpaRepository<Depreciation,Long> {
    List<Depreciation> findByAssetId(Long assetId);
    @Query(value = "SELECT * FROM public.depreciation WHERE public.depreciation.to_date IS NULL AND asset_id=?1",
            countQuery = "SELECT * FROM public.depreciation WHERE public.depreciation.to_date IS NULL AND asset_id=?1",
            nativeQuery = true)
    Optional<Depreciation> findDepreciationIsNull(Long assetId);
}
