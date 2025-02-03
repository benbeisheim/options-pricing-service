package com.quantitative.optionspricing.service;

import org.springframework.stereotype.Service;
import static java.lang.Math.*;

@Service
public class BlackScholesService {
    
    public double calculateCallPrice(double spot, double strike, double timeToMaturity, 
                                   double riskFreeRate, double volatility) {
        double d1 = calculateD1(spot, strike, timeToMaturity, riskFreeRate, volatility);
        double d2 = d1 - volatility * sqrt(timeToMaturity);
        
        return spot * normalCDF(d1) - strike * exp(-riskFreeRate * timeToMaturity) * normalCDF(d2);
    }
    
    public double calculatePutPrice(double spot, double strike, double timeToMaturity, 
                                  double riskFreeRate, double volatility) {
        double d1 = calculateD1(spot, strike, timeToMaturity, riskFreeRate, volatility);
        double d2 = d1 - volatility * sqrt(timeToMaturity);
        
        return strike * exp(-riskFreeRate * timeToMaturity) * normalCDF(-d2) - spot * normalCDF(-d1);
    }
    
    private double calculateD1(double spot, double strike, double timeToMaturity, 
                             double riskFreeRate, double volatility) {
        return (log(spot / strike) + 
                (riskFreeRate + pow(volatility, 2) / 2) * timeToMaturity) / 
                (volatility * sqrt(timeToMaturity));
    }
    
    // Calculate Greeks
    public double calculateDelta(double spot, double strike, double timeToMaturity, 
                               double riskFreeRate, double volatility, boolean isCall) {
        double d1 = calculateD1(spot, strike, timeToMaturity, riskFreeRate, volatility);
        return isCall ? normalCDF(d1) : normalCDF(d1) - 1;
    }
    
    public double calculateGamma(double spot, double strike, double timeToMaturity, 
                               double riskFreeRate, double volatility) {
        double d1 = calculateD1(spot, strike, timeToMaturity, riskFreeRate, volatility);
        return normalPDF(d1) / (spot * volatility * sqrt(timeToMaturity));
    }
    
    public double calculateVega(double spot, double strike, double timeToMaturity, 
                              double riskFreeRate, double volatility) {
        double d1 = calculateD1(spot, strike, timeToMaturity, riskFreeRate, volatility);
        return spot * sqrt(timeToMaturity) * normalPDF(d1) / 100; // Divided by 100 for percentage
    }

    public double calculateTheta(double spot, double strike, double timeToMaturity,
                               double riskFreeRate, double volatility, boolean isCall) {
        double d1 = calculateD1(spot, strike, timeToMaturity, riskFreeRate, volatility);
        double d2 = d1 - volatility * sqrt(timeToMaturity);
        
        double term1 = -(spot * volatility * normalPDF(d1)) / (2 * sqrt(timeToMaturity));
        double term2 = riskFreeRate * strike * exp(-riskFreeRate * timeToMaturity);
        
        if (isCall) {
            return (term1 - term2 * normalCDF(d2)) / 365.0; // Daily theta
        } else {
            return (term1 + term2 * normalCDF(-d2)) / 365.0; // Daily theta
        }
    }

    public double calculateRho(double spot, double strike, double timeToMaturity,
                             double riskFreeRate, double volatility, boolean isCall) {
        double d1 = calculateD1(spot, strike, timeToMaturity, riskFreeRate, volatility);
        double d2 = d1 - volatility * sqrt(timeToMaturity);
        
        if (isCall) {
            return strike * timeToMaturity * exp(-riskFreeRate * timeToMaturity) * 
                   normalCDF(d2) / 100; // Divided by 100 for percentage
        } else {
            return -strike * timeToMaturity * exp(-riskFreeRate * timeToMaturity) * 
                   normalCDF(-d2) / 100; // Divided by 100 for percentage
        }
    }
    
    // Helper functions
    private double normalCDF(double x) {
        return 0.5 * (1 + erf(x / sqrt(2)));
    }
    
    private double normalPDF(double x) {
        return exp(-0.5 * x * x) / sqrt(2 * PI);
    }
    
    // Error function
    private double erf(double x) {
        double a1 = 0.254829592;
        double a2 = -0.284496736;
        double a3 = 1.421413741;
        double a4 = -1.453152027;
        double a5 = 1.061405429;
        double p = 0.3275911;

        // Save the sign of x
        int sign = 1;
        if (x < 0) {
            sign = -1;
        }
        x = abs(x);

        // A&S formula 7.1.26
        double t = 1.0 / (1.0 + p * x);
        double y = 1.0 - (((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t * exp(-x * x);

        return sign * y;
    }
}