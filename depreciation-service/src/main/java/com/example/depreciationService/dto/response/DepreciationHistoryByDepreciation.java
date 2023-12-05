package com.example.depreciationService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepreciationHistoryByDepreciation {
    private int year;
    private Map<String,Double> months;
    private Map<String,String> dates;
}
