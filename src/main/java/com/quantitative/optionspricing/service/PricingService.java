package com.quantitative.optionspricing.service;

import com.quantitative.optionspricing.model.OptionRequest;
import com.quantitative.optionspricing.model.PricingResponse;
import org.springframework.stereotype.Service;

@Service
public class PricingService {
    
    private final MarketDataService marketDataService;
    private final BlackScholesService blackScholesService;
    
    public PricingService(MarketDataService marketDataService, BlackScholesService blackScholesService) {
        this.marketDataService = marketDataService;
        this.blackScholesService = blackScholesService;
    }
    
    public PricingResponse calculatePrice(OptionRequest request) {
        // Get current market data
        double spotPrice = marketDataService.getCurrentPrice(request.getSymbol());
        double volatility = marketDataService.getVolatility(request.getSymbol()); // Currently returns fixed volatility of 0.2 for any symbol
        
        boolean isCall = request.getOptionType().equalsIgnoreCase("CALL");
        
        // Calculate option price
        double optionPrice = isCall 
            ? blackScholesService.calculateCallPrice(
                spotPrice, 
                request.getStrike(), 
                request.getTimeToMaturity(), 
                request.getRiskFreeRate(), 
                volatility)
            : blackScholesService.calculatePutPrice(
                spotPrice, 
                request.getStrike(), 
                request.getTimeToMaturity(), 
                request.getRiskFreeRate(), 
                volatility);
        
        // Calculate Greeks
        double delta = blackScholesService.calculateDelta(
            spotPrice, request.getStrike(), request.getTimeToMaturity(), 
            request.getRiskFreeRate(), volatility, isCall);
            
        double gamma = blackScholesService.calculateGamma(
            spotPrice, request.getStrike(), request.getTimeToMaturity(), 
            request.getRiskFreeRate(), volatility);
            
        double vega = blackScholesService.calculateVega(
            spotPrice, request.getStrike(), request.getTimeToMaturity(), 
            request.getRiskFreeRate(), volatility);
            
        double theta = blackScholesService.calculateTheta(
            spotPrice, request.getStrike(), request.getTimeToMaturity(),
            request.getRiskFreeRate(), volatility, isCall);
            
        double rho = blackScholesService.calculateRho(
            spotPrice, request.getStrike(), request.getTimeToMaturity(),
            request.getRiskFreeRate(), volatility, isCall);
        
        return PricingResponse.builder()
            .optionPrice(optionPrice)
            .delta(delta)
            .gamma(gamma)
            .vega(vega)
            .theta(theta)
            .rho(rho)
            .build();
    }
}