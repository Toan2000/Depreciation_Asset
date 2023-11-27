package com.example.depreciationService.service.Impl;

import com.example.depreciationService.model.Depreciation;
import com.example.depreciationService.model.DepreciationHistory;
import com.example.depreciationService.repository.DepreciationHistoryRepository;
import com.example.depreciationService.service.DepreciationHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DepreciationHistoryServiceImpl implements DepreciationHistoryService {
    private final DepreciationHistoryRepository depreciationHistoryRepository;

    @Override
    public boolean saveDepreciationHistory(DepreciationHistory depreciationHistory) {
        if(depreciationHistoryRepository.save(depreciationHistory) == null)
            return false;
        return true;
    }
    @Override
    public List<DepreciationHistory> findByDepreciation(Depreciation depreciation){
        return depreciationHistoryRepository.findByDepreciation(depreciation);
    }

    @Override
    public List<Object> getDepreciationValue(int month, int year) {
        return depreciationHistoryRepository.getAssetDepreciationHistory(month,year);
    }

    @Override
    public DepreciationHistory getDepreciationByDate(int month, int year) {
        Optional<DepreciationHistory> depreciationHistory = depreciationHistoryRepository.findByMonthAndYear(month,year);
        if(depreciationHistory.isPresent())
            return depreciationHistory.get();
        return null;
    }

    @Override
    public Object getValueByMonthAndYearAndAsset(int mont, int year, Long assetId) {
        return depreciationHistoryRepository.getValueByMonthAndValueAndId(mont, year, assetId);
    }

    @Override
    public List<Object> getValueByYear(int year, Long assetId) {
        return depreciationHistoryRepository.getValueByYearAndId(year, assetId);
    }


}
