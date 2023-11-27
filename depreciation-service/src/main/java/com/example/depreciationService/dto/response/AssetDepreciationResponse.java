package com.example.depreciationService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetDepreciationResponse {
    private Long assetId;
    private String SerialNumber;
    private String fromDate;
    private Double price;
    private int amountMonth;
    private String dateUpdatePrice;
    private Double valuePerMonth;
    private int amountDayOfMonth;
    private int amountDateDepreciation;
    private Double accumulatedPrev;
    private Double accumulatedPresent;
    private Double accumulated;
    private Double valuePresent;
}
