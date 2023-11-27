package com.example.depreciationService.repository;

import com.example.depreciationService.model.Depreciation;
import com.example.depreciationService.model.DepreciationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepreciationHistoryRepository extends JpaRepository<DepreciationHistory, Long> {
    List<DepreciationHistory> findByDepreciation(Depreciation depreciation);
    @Query(value = "SELECT asset_id , SUM(value)\n" +
            "FROM depreciation_history\n" +
            "WHERE (month < ?1 AND year = ?2) OR (year < ?2)\n" +
            "GROUP BY asset_id",nativeQuery = true)
    List<Object> getAssetDepreciationHistory(int month,int year);
    Optional<DepreciationHistory> findByMonthAndYear(int month, int year);
    @Query(value = "SELECT asset_id , SUM(value)\n" +
            "FROM depreciation_history\n" +
            "WHERE month = ?1 AND year = ?2 AND asset_id = ?3\n" +
            "GROUP BY asset_id",nativeQuery = true)
    Object getValueByMonthAndValueAndId(int month, int year, Long assetId);
    @Query(value = "SELECT month , SUM(value)\n" +
            "FROM depreciation_history\n" +
            "WHERE year = ?1 AND asset_id = ?2\n" +
            "GROUP BY asset_id, month",nativeQuery = true)
    List<Object> getValueByYearAndId(int year, Long assetId);
    @Query(value = "SELECT asset_id , SUM(value)\n" +
            "FROM depreciation_history\n" +
            "WHERE ((month < ?1 AND year = ?2) OR (year < ?2)) AND asset_id = ?3\n" +
            "GROUP BY asset_id",nativeQuery = true)
    Object getAssetDepreciationHistoryByAssetId(int month,int year, Long assetId);
}
