package com.example.assetService.service;

import com.example.assetService.model.Brand;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public interface BrandService {
    Brand findById(Long id);
}
