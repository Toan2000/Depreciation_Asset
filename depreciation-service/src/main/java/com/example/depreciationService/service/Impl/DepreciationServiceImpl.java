package com.example.depreciationService.service.Impl;

import com.example.depreciationService.client.DepreciationServiceClient;
import com.example.depreciationService.model.Depreciation;
import com.example.depreciationService.repository.DepreciationRepository;
import com.example.depreciationService.service.DepreciationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DepreciationServiceImpl implements DepreciationService {
    private final DepreciationRepository depreciationRepository;
    private final DepreciationServiceClient depreciationServiceClient;
    @Override
    public List<Depreciation> findByAssetId(Long assetId) {
        return depreciationRepository.findByAssetId(assetId);
    }
    @Override
    public List<Depreciation> findByAssetIdOrderByIdAsc(Long assetId) {
        return depreciationRepository.findByAssetIdOrderByIdAsc(assetId);
    }

    @Override
    public Depreciation findByAssetIdAndToDate(Long assetId, Date date) {
        return depreciationRepository.findByAssetIdAndToDate(assetId,date);
    }

    @Override
    public boolean saveDepreciation(Depreciation depreciation ){
        Depreciation temp = depreciationRepository.save(depreciation);
        if(temp != null)
            return true;
        return false;
    }
    @Override
    public Page<Depreciation> getAllDepreciation(int page,int size){
        Pageable pageable = PageRequest.of(page,size);
        return depreciationRepository.findAll(pageable);
    }
    @Override
    public Depreciation findDepreciationToUpdate(Long assetId){
        Optional<Depreciation> depreciation = depreciationRepository.findDepreciationIsNull(assetId);
        if(depreciation.isPresent())
            return depreciation.get();
        return null;
    }

    @Override
    public List<Depreciation> getAllDepreciationNoToDate() {
        return depreciationRepository.getAllDepreciationNoToDate();
    }

    @Override
    public List<Depreciation> getDepreciationByFromDateAndToDate(Date fromDate, Date toDate) {
        return depreciationRepository.getDepreciationByFromDateAndToDate(fromDate,toDate);
    }

    @Override
    public Object findLDateAndSumValueByAssetId(Long assetId) {
        return depreciationRepository.findLastDepreciationByAssetId(assetId);
    }

    @Override
    public Depreciation findById(Long id) {
        Optional<Depreciation> depreciation = depreciationRepository.findById(id);
        if(depreciation.isPresent())
            return depreciation.get();
        return null;
    }

}
