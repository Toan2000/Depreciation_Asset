package com.example.depreciationService.service.Impl;

import com.example.depreciationService.model.Depreciation;
import com.example.depreciationService.model.DepreciationHistory;
import com.example.depreciationService.repository.DepreciationHistoryRepository;
import com.example.depreciationService.service.DepreciationHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

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

    @Override
    public Object getValueHistoryByAsset(int month, int year, Long assetId) {
        return depreciationHistoryRepository.getAssetDepreciationHistoryByAssetId(month, year, assetId);
    }

    @Override
    public Object getValueHistoryByDepreciation(int month, int year, Long depreciationId) {
        return depreciationHistoryRepository.getAssetDepreciationHistoryByDepreciationId(month, year, depreciationId);
    }

    @Override
    public List<Object> getDepreciationByAllDept(int year) {
        return depreciationHistoryRepository.getDepreciationByAllDept(year);
    }

    @Override
    public List<Object> getDepreciationByDeptIds(int year, List<Long> deptIds) {
        return depreciationHistoryRepository.getDepreciationByDeptIds(deptIds,year);
    }

    @Override
    public List<Object> getDepreciationByAllDeptInYear(int year, Long deptId) {
        return depreciationHistoryRepository.getDepreciationByAllDeptInYear(year, deptId);
    }
    @Override
    public Double totalValueDepreciation(){
        return depreciationHistoryRepository.totalValueDepreciation();
    }
    @Override
    public Double getTotalValueByDeptIdAndAssetType(Long deptId, Long assetTypeId,int year){
        Double result = depreciationHistoryRepository.getTotalValueByDeptIdAndAssetType(deptId,assetTypeId,year);
        if(result==null)
            return 0.0;
        return result;
    }

    @Override
    public Double totalValueDepreciationByAssetId(Long assetId) {
        return depreciationHistoryRepository.totalValueDepreciationByAssetId(assetId);
    }

    @Override
    public Double totalValueDepreciationByAssetId(Long assetId, int month, int year) {
        return depreciationHistoryRepository.totalValueDepreciationByAssetId(assetId, month, year);
    }

    @Override
    public Double totalValueDepreciationByDepreciationId(Long depreciationId, int month, int year) {
        return depreciationHistoryRepository.totalValueDepreciationByAssetId(depreciationId, month, year);
    }

}
