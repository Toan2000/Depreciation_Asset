package com.example.assetService.repository;

import com.example.assetService.model.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;


public interface AssetRepository extends JpaRepository<Asset,Long> {
    Page<Asset> findByDeptUsedId(Long id, Pageable pageable);
    Page<Asset> findByUserUsedId(Long id, Pageable pageable);
    Page<Asset> findByAssetStatus(Long status, Pageable pageable);
    Page<Asset> findByAssetType(Long assetTypeId, Pageable pageable);
    @Query(value = "SELECT * FROM public.assets WHERE date_in_stored >= ?1 AND date_in_stored <= ?2 ORDER BY asset_id ASC\n",
            countQuery = "SELECT * FROM public.assets WHERE date_in_stored >= ?1 AND date_in_stored <= ?2 ORDER BY asset_id ASC\n",
            nativeQuery = true)
    Page<Asset> findByStoredDate(String fromDate, String toDate,Pageable pageable);
    @Query(value = "SELECT * FROM public.assets WHERE date_in_stored >= ?1 AND date_in_stored <= ?2 ORDER BY asset_id ASC\n",
            countQuery = "SELECT * FROM public.assets WHERE date_in_stored >= ?1 AND date_in_stored <= ?2 ORDER BY asset_id ASC\n",
            nativeQuery = true)
    Page<Asset> findByStoredDate1(Date fromDate, Date toDate, Pageable pageable);
}
