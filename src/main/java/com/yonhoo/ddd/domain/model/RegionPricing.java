package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;

/**
 * 地域定价实体
 * 管理基于地域的价格调整策略
 */
public class RegionPricing {
    private String pricingId;
    private Region targetRegion;
    private PricingAdjustmentType adjustmentType;
    private BigDecimal adjustmentValue;

    public boolean matchesRegion(Region region) {
        return this.targetRegion == region;
    }

    public BigDecimal adjustPrice(BigDecimal originalPrice) {
        switch (adjustmentType) {
            case MARKUP:
                return originalPrice.multiply(BigDecimal.ONE.add(adjustmentValue.divide(BigDecimal.valueOf(100))));
            case DISCOUNT:
                return originalPrice.multiply(BigDecimal.ONE.subtract(adjustmentValue.divide(BigDecimal.valueOf(100))));
            case FIXED_PRICE:
                return adjustmentValue;
            default:
                return originalPrice;
        }
    }

    // Getters and setters
    public String getPricingId() {
        return pricingId;
    }

    public void setPricingId(String pricingId) {
        this.pricingId = pricingId;
    }

    public Region getTargetRegion() {
        return targetRegion;
    }

    public void setTargetRegion(Region targetRegion) {
        this.targetRegion = targetRegion;
    }

    public PricingAdjustmentType getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(PricingAdjustmentType adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public BigDecimal getAdjustmentValue() {
        return adjustmentValue;
    }

    public void setAdjustmentValue(BigDecimal adjustmentValue) {
        this.adjustmentValue = adjustmentValue;
    }
}