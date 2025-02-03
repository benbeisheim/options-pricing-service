package com.quantitative.optionspricing.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AlphaVantageClient {
    
    private final RestTemplate restTemplate;
    private final String apiKey;
    private static final String BASE_URL = "https://www.alphavantage.co/query";
    
    public AlphaVantageClient(@Value("${alphavantage.api.key}") String apiKey) {
        this.restTemplate = new RestTemplate();
        this.apiKey = apiKey;
    }
    
    public MarketDataResponse getQuote(String symbol) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL)
            .queryParam("function", "GLOBAL_QUOTE")
            .queryParam("symbol", symbol)
            .queryParam("apikey", apiKey)
            .toUriString();
            
        MarketDataResponse response = restTemplate.getForObject(url, MarketDataResponse.class);
        System.out.println("Raw API Response: " + response);
        return response;
    }
}
