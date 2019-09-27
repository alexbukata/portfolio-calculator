package ru.desiolab.portfolio.calculator.service;

import ru.desiolab.portfolio.calculator.dto.CalculatePortfolioRequest;
import ru.desiolab.portfolio.calculator.dto.CalculatePortfolioResponse;
import ru.desiolab.portfolio.calculator.dto.StockInfo;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PortfolioCostService {
    private final IexService iexService;

    @Inject
    public PortfolioCostService(IexService iexService) {
        this.iexService = iexService;
    }

    public CalculatePortfolioResponse calculateCost(CalculatePortfolioRequest request) {
        List<StockInfo> stockInfos = getStockInfos(request);
        BigDecimal priceSum = stockInfos.stream()
                .map(stockInfo -> new BigDecimal(stockInfo.price()).multiply(new BigDecimal(stockInfo.volume())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        List<CalculatePortfolioResponse.Allocation> allocations = calculateAllocations(stockInfos, priceSum);
        double volume = priceSum.setScale(2, RoundingMode.HALF_UP).doubleValue();
        return new CalculatePortfolioResponse()
                .allocations(allocations)
                .volume(volume);
    }

    private List<StockInfo> getStockInfos(CalculatePortfolioRequest request) {
        return request.portfolioEntries().parallelStream()
                .map(this::requestStockInfo)
                .collect(Collectors.toList());
    }

    private StockInfo requestStockInfo(CalculatePortfolioRequest.PortfolioEntry portfolioEntry) {
        return new StockInfo()
                .companyInfo(iexService.requestInfo(portfolioEntry.symbol()))
                .price(iexService.requestPrice(portfolioEntry.symbol()))
                .volume(portfolioEntry.volume());
    }

    private CalculatePortfolioResponse.Allocation calculateAllocation(BigDecimal priceSum, Map.Entry<String, List<StockInfo>> entry) {
        String sector = entry.getKey();
        List<StockInfo> stocks = entry.getValue();
        BigDecimal sectorPriceSum = stocks.stream()
                .map(stockInfo -> new BigDecimal(stockInfo.price()).multiply(new BigDecimal(stockInfo.volume())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        double proportion = sectorPriceSum.divide(priceSum, 5, RoundingMode.HALF_UP).doubleValue();
        double assetValue = sectorPriceSum.setScale(2, RoundingMode.HALF_UP).doubleValue();
        return new CalculatePortfolioResponse.Allocation()
                .sector(sector)
                .assetValue(assetValue)
                .proportion(proportion);
    }

    private List<CalculatePortfolioResponse.Allocation> calculateAllocations(List<StockInfo> stockInfos, BigDecimal priceSum) {
        Map<String, List<StockInfo>> sectorMap = stockInfos.stream()
                .collect(Collectors.groupingBy(stockInfo -> stockInfo.companyInfo().sector()));
        return sectorMap.entrySet().stream()
                .map(entry -> calculateAllocation(priceSum, entry))
                .collect(Collectors.toList());
    }
}
