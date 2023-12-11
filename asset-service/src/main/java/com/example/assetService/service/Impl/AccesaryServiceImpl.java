package com.example.assetService.service.Impl;

import com.example.assetService.model.Accessary;
import com.example.assetService.repository.AccessaryRepository;
import com.example.assetService.service.AccesaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class AccesaryServiceImpl implements AccesaryService {
    @Autowired
    AccessaryRepository accessaryRepository;

    @Override
    public List<Accessary> findByAssetId(Long id) {
        return accessaryRepository.findByAssetId(id);
    }

    @Override
    public Accessary save(Accessary accessary) {
        return accessaryRepository.save(accessary);
    }
}
