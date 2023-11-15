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
    private String assetGroup;
    private Date dateInStored;
    private Long status;
    private String statusName;
    private Double price;
    private Date dateUsed;
    private Long userIdUsed;
    private Long deptIdUsed;
}
