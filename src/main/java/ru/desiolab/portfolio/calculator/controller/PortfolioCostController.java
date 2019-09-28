package ru.desiolab.portfolio.calculator.controller;

import ru.desiolab.portfolio.calculator.dto.CalculatePortfolioRequest;
import ru.desiolab.portfolio.calculator.dto.CalculatePortfolioResponse;
import ru.desiolab.portfolio.calculator.service.PortfolioCostService;

import javax.inject.Inject;

import static java.util.Objects.isNull;

public class PortfolioCostController {

    private final PortfolioCostService portfolioCostService;

    @Inject
    public PortfolioCostController(PortfolioCostService portfolioCostService) {
        this.portfolioCostService = portfolioCostService;
    }

    public CalculatePortfolioResponse calculateCost(CalculatePortfolioRequest request) {
        if (isNull(request.portfolioEntries()) || request.portfolioEntries().isEmpty()) {
            return new CalculatePortfolioResponse();
        }
        validate(request);
        return portfolioCostService.calculateCost(request);
    }

    private void validate(CalculatePortfolioRequest request) {
        for (CalculatePortfolioRequest.PortfolioEntry portfolioEntry : request.portfolioEntries()) {
            if (portfolioEntry.volume() <= 0) {
                throw new RuntimeException("Stock in portfolio must have volume more than zero");
            }
        }
    }
}
