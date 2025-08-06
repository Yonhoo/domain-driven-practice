package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 策略分析结果值对象
 * 包含策略选择过程的详细信息
 */
public class StrategyAnalysisResult {
    private BigDecimal basePrice;
    private BigDecimal finalPrice;
    private UserContext userContext;
    private UserPricingStrategy selectedStrategy;
    private String message;
    private List<StrategyResult> strategyResults;

    public StrategyAnalysisResult() {
        this.strategyResults = new ArrayList<>();
    }

    public void addStrategyResult(UserPricingStrategy strategy, BigDecimal price) {
        strategyResults.add(new StrategyResult(strategy, price));
    }

    public List<UserPricingStrategy> getApplicableStrategies() {
        return strategyResults.stream()
                .map(StrategyResult::getStrategy)
                .toList();
    }

    // Getters and setters
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public BigDecimal getFinalPrice() { return finalPrice; }
    public void setFinalPrice(BigDecimal finalPrice) { this.finalPrice = finalPrice; }

    public UserContext getUserContext() { return userContext; }
    public void setUserContext(UserContext userContext) { this.userContext = userContext; }

    public UserPricingStrategy getSelectedStrategy() { return selectedStrategy; }
    public void setSelectedStrategy(UserPricingStrategy selectedStrategy) { this.selectedStrategy = selectedStrategy; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<StrategyResult> getStrategyResults() { return strategyResults; }
    public void setStrategyResults(List<StrategyResult> strategyResults) { this.strategyResults = strategyResults; }

    /**
     * 内部类：单个策略的结果
     */
    public static class StrategyResult {
        private UserPricingStrategy strategy;
        private BigDecimal calculatedPrice;

        public StrategyResult(UserPricingStrategy strategy, BigDecimal calculatedPrice) {
            this.strategy = strategy;
            this.calculatedPrice = calculatedPrice;
        }

        public UserPricingStrategy getStrategy() { return strategy; }
        public BigDecimal getCalculatedPrice() { return calculatedPrice; }
    }
}