package com.example.assetService.dto.request;

import com.example.assetService.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetRequest {
    private String assetName;
    private Long status;
    private long assetTypeId;
    private Double price;
    private String serial;
}