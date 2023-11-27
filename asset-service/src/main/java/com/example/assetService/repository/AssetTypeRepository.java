package com.example.assetService.repository;

import com.example.assetService.model.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetTypeRepository extends JpaRepository<AssetType,Long> {
    List<AssetType> findByAssetType(Long assetType);
}
