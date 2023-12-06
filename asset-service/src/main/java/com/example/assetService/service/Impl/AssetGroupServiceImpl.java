package com.example.assetService.service.Impl;

import com.example.assetService.model.AssetGroup;
import com.example.assetService.repository.AssetGroupRepository;
import com.example.assetService.service.AssetGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AssetGroupServiceImpl implements AssetGroupService {
    private final AssetGroupRepository assetGroupRepository;
    @Override
    public List<AssetGroup> getAllGroup(){
        return assetGroupRepository.findAll();
    }
}
