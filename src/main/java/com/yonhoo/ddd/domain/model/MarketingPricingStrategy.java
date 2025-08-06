package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 营销定价策略聚合根
 * 职责：管理节假日、限时活动等可配置的营销策略
 */
public class MarketingPricingStrategy {
    private String strategyId;
    private String strategyName;
    private StrategyType strategyType;
    private boolean active;
    private DateRange effectivePeriod;
    private List<HolidayPricing> holidayPricings;
    private List<FlashSaleActivity> flashSaleActivities;
    private List<SeasonalPricing> seasonalPricings;
    private PriorityLevel priorityLevel;

    /**
     * 计算营销策略价格
     */
    public BigDecimal calculateMarketingPrice(BigDecimal basePrice, LocalDate targetDate, MarketingContext context) {
        // 验证策略是否激活和在有效期内
        if (!isEffective(targetDate)) {
            return basePrice;
        }

        BigDecimal finalPrice = basePrice;

        // 根据策略类型应用不同的定价逻辑
        switch (strategyType) {
            case HOLIDAY_PRICING:
                finalPrice = applyHolidayPricing(finalPrice, targetDate);
                break;
            case FLASH_SALE:
                finalPrice = applyFlashSaleActivity(finalPrice, targetDate, context);
                break;
            case SEASONAL_PRICING:
                finalPrice = applySeasonalPricing(finalPrice, targetDate);
                break;
            case COMBINED:
                finalPrice = applyCombinedStrategy(finalPrice, targetDate, context);
                break;
        }

        return finalPrice;
    }

    private BigDecimal applyHolidayPricing(BigDecimal price, LocalDate targetDate) {
        return holidayPricings.stream()
                .filter(holiday -> holiday.isApplicableDate(targetDate))
                .findFirst()
                .map(holiday -> holiday.adjustPrice(price))
                .orElse(price);
    }

    private BigDecimal applyFlashSaleActivity(BigDecimal price, LocalDate targetDate, MarketingContext context) {
        LocalDateTime now = context.getCurrentTime();
        
        return flashSaleActivities.stream()
                .filter(activity -> activity.isActive(now, targetDate))
                .filter(activity -> activity.hasAvailableQuota())
                .findFirst()
                .map(activity -> activity.calculateSalePrice(price))
                .orElse(price);
    }

    private BigDecimal applySeasonalPricing(BigDecimal price, LocalDate targetDate) {
        return seasonalPricings.stream()
                .filter(seasonal -> seasonal.isInSeason(targetDate))
                .findFirst()
                .map(seasonal -> seasonal.adjustPrice(price))
                .orElse(price);
    }

    private BigDecimal applyCombinedStrategy(BigDecimal price, LocalDate targetDate, MarketingContext context) {
        BigDecimal finalPrice = price;
        
        // 优先级：限时活动 > 节假日 > 季节性
        finalPrice = applyFlashSaleActivity(finalPrice, targetDate, context);
        if (finalPrice.equals(price)) {
            finalPrice = applyHolidayPricing(finalPrice, targetDate);
        }
        if (finalPrice.equals(price)) {
            finalPrice = applySeasonalPricing(finalPrice, targetDate);
        }
        
        return finalPrice;
    }

    /**
     * 验证策略是否在指定日期有效
     */
    public boolean isEffective(LocalDate targetDate) {
        return active && effectivePeriod.contains(targetDate);
    }

    /**
     * 获取策略适用的最优价格类型
     */
    public PricingType getBestPricingType(LocalDate targetDate, MarketingContext context) {
        if (!isEffective(targetDate)) {
            return PricingType.STANDARD;
        }

        // 检查限时活动
        boolean hasFlashSale = flashSaleActivities.stream()
                .anyMatch(activity -> activity.isActive(context.getCurrentTime(), targetDate));
        if (hasFlashSale) {
            return PricingType.FLASH_SALE;
        }

        // 检查节假日
        boolean isHoliday = holidayPricings.stream()
                .anyMatch(holiday -> holiday.isApplicableDate(targetDate));
        if (isHoliday) {
            return PricingType.HOLIDAY;
        }

        // 检查季节性
        boolean isSeasonal = seasonalPricings.stream()
                .anyMatch(seasonal -> seasonal.isInSeason(targetDate));
        if (isSeasonal) {
            return PricingType.SEASONAL;
        }

        return PricingType.STANDARD;
    }

    /**
     * 预留库存（用于限时活动）
     */
    public boolean reserveQuota(String activityId, int quantity) {
        return flashSaleActivities.stream()
                .filter(activity -> activity.getActivityId().equals(activityId))
                .findFirst()
                .map(activity -> activity.reserveQuota(quantity))
                .orElse(false);
    }

    // === Getters and Setters ===
    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public StrategyType getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(StrategyType strategyType) {
        this.strategyType = strategyType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public DateRange getEffectivePeriod() {
        return effectivePeriod;
    }

    public void setEffectivePeriod(DateRange effectivePeriod) {
        this.effectivePeriod = effectivePeriod;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }
} 