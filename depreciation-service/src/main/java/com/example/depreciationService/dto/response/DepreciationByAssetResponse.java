package com.example.depreciationService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepreciationByAssetResponse {
    private Long assetId;
    private String assetName;
    private Double price;
    private String fromDate;
    private String expDate;
    private String changePrice;
    private Double valuePrev;
    private Double valuePre;
    private Double amountMonth;
    private Double totalValue;
    DepreciationAssetHistory depreciationAssetHistory;
    @Data
    @AllArgsConstructor
    public class DepreciationAssetHistory{
        private UserResponse userResponse;
        private String fromDate;
        private String toDate;
        private Double value;
        private int time;

    }
}
