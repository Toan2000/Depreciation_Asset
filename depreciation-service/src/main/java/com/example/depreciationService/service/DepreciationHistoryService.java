package com.example.depreciationService.service;

import com.example.depreciationService.model.Depreciation;
import com.example.depreciationService.model.DepreciationHistory;

import java.util.Date;
import java.util.List;

public interface DepreciationHistoryService {

    boolean saveDepreciationHistory(DepreciationHistory depreciationHistory);

    List<DepreciationHistory> findByDepreciation(Depreciation depreciation);
    List<Object> getDepreciationValue(int month,int year);
    DepreciationHistory getDepreciationByDate(int month, int year);
    Object getValueByMonthAndYearAndAsset(int mont,int year, Long assetId);
    List<Object> getValueByYear(int year, Long assetId);
}
