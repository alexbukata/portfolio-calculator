package ru.desiolab.portfolio.calculator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
public class CalculatePortfolioRequest {
    @JsonProperty("stocks")
    private List<PortfolioEntry> portfolioEntries;

    @Data
    @Accessors(fluent = true)
    public static class PortfolioEntry {
        @JsonProperty
        private String symbol;
        @JsonProperty
        private Integer volume;
    }
}
