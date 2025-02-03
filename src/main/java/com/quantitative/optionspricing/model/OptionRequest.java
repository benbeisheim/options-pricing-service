package com.quantitative.optionspricing.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data  // @Data annotation creates getters, setters, equals, hashCode, and toString
public class OptionRequest {
    @NotNull
    private String symbol;

    @NotNull
    private String optionType;  // "CALL" or "PUT"
    
    @Positive
    private double strike;
    
    @Positive
    private double timeToMaturity;
    
    @Positive
    private double riskFreeRate;
    
    @Positive
    private double volatility;
}