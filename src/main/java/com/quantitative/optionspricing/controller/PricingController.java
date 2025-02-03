package com.quantitative.optionspricing.controller;

import com.quantitative.optionspricing.model.OptionRequest;
import com.quantitative.optionspricing.model.PricingResponse;
import com.quantitative.optionspricing.service.PricingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.quantitative.optionspricing.service.MarketDataService;

@RestController  
@RequestMapping("/api/v1/pricing")  
public class PricingController {
    
    private final PricingService pricingService;
    private final MarketDataService marketDataService;
    
    public PricingController(PricingService pricingService, MarketDataService marketDataService) {
        this.pricingService = pricingService;
        this.marketDataService = marketDataService;
    }
    
    @PostMapping("/calculate") 
    public ResponseEntity<PricingResponse> calculatePrice(@Valid @RequestBody OptionRequest request) {
        PricingResponse response = pricingService.calculatePrice(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Options Pricing Service is running!");
    }

    @GetMapping("/market-data/{symbol}")
public ResponseEntity<Double> getMarketPrice(@PathVariable String symbol) {
    double price = marketDataService.getCurrentPrice(symbol);
    return ResponseEntity.ok(price);
}
}