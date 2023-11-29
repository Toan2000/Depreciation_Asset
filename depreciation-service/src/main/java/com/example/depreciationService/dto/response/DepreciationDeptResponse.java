package com.example.depreciationService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepreciationDeptResponse {
    private Long deptId;
    private String deptName;
    private Double totalPrice = 0.0;
    private Double totalValuePerMonth = 0.0;
    private Double totalValuePrev = 0.0;
    private Double totalValuePresent = 0.0;
    private List<AssetType> assetTypes;
}
