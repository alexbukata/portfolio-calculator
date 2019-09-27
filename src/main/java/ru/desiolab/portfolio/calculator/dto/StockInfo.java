package ru.desiolab.portfolio.calculator.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class StockInfo {
    private CompanyInfo companyInfo;
    private Double price;
    private Integer volume;
}
