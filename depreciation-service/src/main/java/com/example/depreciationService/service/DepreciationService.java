package com.example.depreciationService.service;

import com.example.depreciationService.model.Depreciation;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Component
@Service
public interface DepreciationService {

    List<Depreciation> findByAssetId(Long assetId);

    List<Depreciation> findByAssetIdOrderByIdAsc(Long assetId);

    Depreciation findByAssetIdAndToDate(Long assetId, Date date);

    Depreciation saveDepreciation(Depreciation depreciation);

    Page<Depreciation> getAllDepreciation(int page, int size);

    Depreciation findDepreciationToUpdate(Long assetId);
     List<Depreciation> getAllDepreciationNoToDate();
    List<Depreciation> getDepreciationByFromDateAndToDate(Date fromDate, Date toDate);
    Object findLDateAndSumValueByAssetId(Long assetId);
    Depreciation findById(Long id);
}
