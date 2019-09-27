package ru.desiolab.portfolio.calculator.handler;

import io.jooby.Context;
import io.jooby.MediaType;
import io.jooby.Route;
import ru.desiolab.portfolio.calculator.controller.PortfolioCostController;
import ru.desiolab.portfolio.calculator.dto.CalculatePortfolioRequest;

import javax.annotation.Nonnull;

public class CalculatePortfolioHandler implements Route.Handler {

    private final PortfolioCostController portfolioCostController;

    public CalculatePortfolioHandler(PortfolioCostController portfolioCostController) {
        this.portfolioCostController = portfolioCostController;
    }

    @Nonnull
    @Override
    public Object apply(@Nonnull Context ctx) {
        CalculatePortfolioRequest request = ctx.body(CalculatePortfolioRequest.class, MediaType.json);
        return portfolioCostController.calculateCost(request);
    }
}
