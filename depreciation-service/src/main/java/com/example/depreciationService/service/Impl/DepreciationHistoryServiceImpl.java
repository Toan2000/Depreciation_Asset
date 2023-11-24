package com.example.depreciationService.service.Impl;

import com.example.depreciationService.model.Depreciation;
import com.example.depreciationService.model.DepreciationHistory;
import com.example.depreciationService.repository.DepreciationHistoryRepository;
import com.example.depreciationService.service.DepreciationHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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


}
