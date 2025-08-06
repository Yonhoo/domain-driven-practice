package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 定价结果值对象
 * 包含价格计算的详细结果信息
 */
public class PricingResult {
    private BigDecimal basePrice;
    private BigDecimal userDiscountedPrice;
    private BigDecimal finalPrice;
    private BigDecimal userDiscountAmount;
    private BigDecimal marketingDiscountAmount;
    private BigDecimal totalDiscountAmount;
    private BigDecimal discountRate;
    private LocalDate checkInDay;
    private String userId;
    private UserLevel userLevel;
    private LocalDateTime calculationTime;

    // Getters and setters
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public BigDecimal getUserDiscountedPrice() { return userDiscountedPrice; }
    public void setUserDiscountedPrice(BigDecimal userDiscountedPrice) { this.userDiscountedPrice = userDiscountedPrice; }

    public BigDecimal getFinalPrice() { return finalPrice; }
    public void setFinalPrice(BigDecimal finalPrice) { this.finalPrice = finalPrice; }

    public BigDecimal getUserDiscountAmount() { return userDiscountAmount; }
    public void setUserDiscountAmount(BigDecimal userDiscountAmount) { this.userDiscountAmount = userDiscountAmount; }

    public BigDecimal getMarketingDiscountAmount() { return marketingDiscountAmount; }
    public void setMarketingDiscountAmount(BigDecimal marketingDiscountAmount) { this.marketingDiscountAmount = marketingDiscountAmount; }

    public BigDecimal getTotalDiscountAmount() { return totalDiscountAmount; }
    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) { this.totalDiscountAmount = totalDiscountAmount; }

    public BigDecimal getDiscountRate() { return discountRate; }
    public void setDiscountRate(BigDecimal discountRate) { this.discountRate = discountRate; }

    public LocalDate getCheckInDay() { return checkInDay; }
    public void setCheckInDay(LocalDate checkInDay) { this.checkInDay = checkInDay; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public UserLevel getUserLevel() { return userLevel; }
    public void setUserLevel(UserLevel userLevel) { this.userLevel = userLevel; }

    public LocalDateTime getCalculationTime() { return calculationTime; }
    public void setCalculationTime(LocalDateTime calculationTime) { this.calculationTime = calculationTime; }
}