package com.example.assetService.service;

import com.example.assetService.model.UpdateHistory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public interface UpdateHistoryService {
    List<UpdateHistory> getListUpdateHistoryByAssetId(Long assetId);

    List<UpdateHistory> getListReduceHistoryByAssetId(Long assetId);
}