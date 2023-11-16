package com.example.assetService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetResponse {
    private Long assetId;
    private String assetName;
    private long assetTypeId;
    private String assetTypeName;
    private int assetGroupId;
    private String assetGroup;
    private Long status;
    private String statusName;
    private Double price;
    private String dateUsed;
    private Long userIdUsed;
    private Long deptIdUsed;
    private UserResponse user;
    private String dateInStored;
}
