package com.example.assetService.repository;

import com.example.assetService.model.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetTypeRepository extends JpaRepository<AssetType,Long> {
}
