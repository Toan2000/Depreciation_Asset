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
    private Month month = new Month(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0);
    @Data
    @AllArgsConstructor
    public class Month{
        private Double jan;
        private Double feb;
        private Double mar;
        private Double apr;
        private Double may;
        private Double jun;
        private Double jul;
        private Double aug;
        private Double sep;
        private Double oct;
        private Double nov;
        private Double dec;
    }
}
