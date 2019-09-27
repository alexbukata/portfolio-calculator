package ru.desiolab.portfolio.calculator.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
public class PortfolioCost {
    private Double value;
    private List<ProportionEntry> allocations;
}
