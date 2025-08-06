package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 每日价格信息值对象
 * 包含特定日期的价格详情
 */
public class DailyPriceInfo {
    private LocalDate date;
    private BigDecimal basePrice;
    private BigDecimal finalPrice;
    private PricingType pricingType;

    public DailyPriceInfo(LocalDate date, BigDecimal basePrice, BigDecimal finalPrice, PricingType pricingType) {
        this.date = date;
        this.basePrice = basePrice;
        this.finalPrice = finalPrice;
        this.pricingType = pricingType;
    }

    // Getters
    public LocalDate getDate() { return date; }
    public BigDecimal getBasePrice() { return basePrice; }
    public BigDecimal getFinalPrice() { return finalPrice; }
    public PricingType getPricingType() { return pricingType; }
}