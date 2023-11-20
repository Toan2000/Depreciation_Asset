package com.example.depreciationService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepreciationResponse {
    private Long id;
    private String fromDate;
    private String toDate;
    private int amountMonth;
    private Double valueDepreciation;
    private String createAt;
    private boolean active;
    private int status;
    private AssetResponse assetResponse;
    private UserResponse userResponse;
}
