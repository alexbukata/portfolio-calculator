package ru.desiolab.portfolio.calculator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
public class CalculatePortfolioResponse {
    @JsonProperty
    private Double volume;
    @JsonProperty
    private List<Allocation> allocations;

    @Data
    @Accessors(fluent = true)
    public static class Allocation {
        @JsonProperty
        private String sector;
        @JsonProperty
        private double assetValue;
        @JsonProperty
        private double proportion;
    }
}
