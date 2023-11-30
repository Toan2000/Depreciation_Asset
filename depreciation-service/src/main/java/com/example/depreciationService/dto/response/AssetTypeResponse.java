package com.example.depreciationService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetTypeResponse {
    private Long id;
    private String assetName;
}
