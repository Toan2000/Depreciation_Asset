package com.example.assetService.service;

import com.example.assetService.model.Accessary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public interface AccesaryService {
    List<Accessary> findByAssetId(Long id);
}
