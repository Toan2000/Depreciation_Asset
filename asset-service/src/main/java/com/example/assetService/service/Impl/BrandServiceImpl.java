package com.example.assetService.service.Impl;

import com.example.assetService.model.Brand;
import com.example.assetService.repository.BrandRepository;
import com.example.assetService.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public Brand findById(Long id) {
        Optional<Brand> brand = brandRepository.findById(id);
        if(brand.isPresent())
            return brand.get();
        return null;
    }
}
