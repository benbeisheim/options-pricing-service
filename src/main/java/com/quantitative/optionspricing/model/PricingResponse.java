package com.quantitative.optionspricing.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder 
public class PricingResponse {
    private double optionPrice;
    private double delta;
    private double gamma;
    private double vega;
    private double theta;
    private double rho;
}