package com.quantitative.optionspricing.service;

import com.quantitative.optionspricing.client.AlphaVantageClient;
import com.quantitative.optionspricing.client.MarketDataResponse;
import org.springframework.stereotype.Service;

@Service
public class MarketDataService {
    
    private final AlphaVantageClient alphaVantageClient;
    
    public MarketDataService(AlphaVantageClient alphaVantageClient) {
        this.alphaVantageClient = alphaVantageClient;
    }
    
    public double getCurrentPrice(String symbol) {
        MarketDataResponse response = alphaVantageClient.getQuote(symbol);
        String priceStr = response.getGlobalQuote().getPrice();
        System.out.println("Raw price string: " + priceStr); 
        return Double.parseDouble(priceStr);
    }
    
    public double getVolatility(String symbol) {
        // TODO: Implement historical volatility calculation
        return 0.2;
    }
}