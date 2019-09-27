package ru.desiolab.portfolio.calculator.controller;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.desiolab.portfolio.calculator.config.MainModule;
import ru.desiolab.portfolio.calculator.dto.CalculatePortfolioRequest;
import ru.desiolab.portfolio.calculator.dto.CalculatePortfolioResponse;
import ru.desiolab.portfolio.calculator.dto.CompanyInfo;
import ru.desiolab.portfolio.calculator.service.IexService;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

class PortfolioCostControllerTest {

    @Bind
    private IexService iexService;

    @Inject
    private PortfolioCostController portfolioCostController;

    @BeforeEach
    void setUp() {
        iexService = Mockito.mock(IexService.class);
        Guice.createInjector(new MainModule("token"), BoundFieldModule.of(this)).injectMembers(this);
    }

    @Test
    void emptyRequest() {
        //given
        //when
        CalculatePortfolioResponse response = portfolioCostController.calculateCost(new CalculatePortfolioRequest());
        //then
        assertNull(response.allocations());
        assertNull(response.volume());
        verifyZeroInteractions(iexService);
    }

    @Test
    void singleCompanyRequest() {
        String aapl = "AAPL";
        String sector = "sector1";
        //given
        mockIex(aapl, sector, 10.00);
        CalculatePortfolioRequest request = new CalculatePortfolioRequest()
                .portfolioEntries(List.of(portfolio(aapl, 1000)));
        //when
        CalculatePortfolioResponse response = portfolioCostController.calculateCost(request);
        //then
        assertNotNull(response);
        assertEquals(1000, response.volume());
        List<CalculatePortfolioResponse.Allocation> allocations = response.allocations();
        assertEquals(1, allocations.size());
        CalculatePortfolioResponse.Allocation sectorAllocation = allocations.get(0);
        assertEquals(sector, sectorAllocation.sector());
        assertEquals(1000, sectorAllocation.assetValue());
        assertEquals(1, sectorAllocation.proportion());
    }

    @Test
    void threeCompanyRequest() {
        String aapl = "AAPL";
        double aaplPrice = 10.00;
        int aaplVolume = 100;
        String goog = "GOOG";
        double googPrice = 20.00;
        int googVolume = 20;
        String msft = "MSFT";
        double msftPrice = 30.00;
        int msftVolume = 45;
        String sector1 = "sector1";
        String sector2 = "sector2";
        //given
        mockIex(aapl, sector1, aaplPrice);
        mockIex(goog, sector2, googPrice);
        mockIex(msft, sector2, msftPrice);
        CalculatePortfolioRequest request = new CalculatePortfolioRequest()
                .portfolioEntries(List.of(
                        portfolio(aapl, aaplVolume),
                        portfolio(goog, googVolume),
                        portfolio(msft, msftVolume))
                );
        //when
        CalculatePortfolioResponse response = portfolioCostController.calculateCost(request);
        //then
        assertNotNull(response);
        assertEquals(2750, response.volume());
        List<CalculatePortfolioResponse.Allocation> allocations = response.allocations();
        assertEquals(2, allocations.size());
        CalculatePortfolioResponse.Allocation sector1Allocation = allocations.stream()
                .filter(allocation -> allocation.sector().equals(sector1))
                .findAny().orElseThrow();
        assertEquals(sector1, sector1Allocation.sector());
        assertEquals(1000, sector1Allocation.assetValue());
        assertEquals(0.36364, sector1Allocation.proportion());
        CalculatePortfolioResponse.Allocation sector2Allocation = allocations.stream()
                .filter(allocation -> allocation.sector().equals(sector2))
                .findAny().orElseThrow();
        assertEquals(sector2, sector2Allocation.sector());
        assertEquals(1750, sector2Allocation.assetValue());
        assertEquals(0.63636, sector2Allocation.proportion());
    }

    private CalculatePortfolioRequest.PortfolioEntry portfolio(String aapl, int aaplVolume) {
        return new CalculatePortfolioRequest.PortfolioEntry()
                .symbol(aapl)
                .volume(aaplVolume);
    }

    private void mockIex(String symbol, String sector, double price) {
        CompanyInfo appleCompanyInfo = companyInfo(symbol, sector);
        when(iexService.requestInfo(symbol)).thenReturn(appleCompanyInfo);
        when(iexService.requestPrice(symbol)).thenReturn(price);
    }

    private CompanyInfo companyInfo(String symbol, String sector) {
        return new CompanyInfo()
                .sector(sector)
                .companyName("Apple")
                .symbol(symbol);
    }
}