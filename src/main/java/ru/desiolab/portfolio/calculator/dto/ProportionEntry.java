package ru.desiolab.portfolio.calculator.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class ProportionEntry {
    private String sector;
    private Double value;
    private Double proportion;
}
