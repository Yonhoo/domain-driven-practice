package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户定价策略聚合根
 * 职责：管理基于用户属性的定价策略（等级、地域、渠道等）
 */
public class UserPricingStrategy {
    private String strategyId;
    private String strategyName;
    private boolean active;
    private PriorityLevel strategyPriority;  // 策略优先级
    
    // 时效性相关字段
    private LocalDateTime effectiveStartTime;  // 策略生效开始时间
    private LocalDateTime effectiveEndTime;    // 策略生效结束时间
    private DateRange validDateRange;          // 有效日期范围（可选，用于更复杂的日期规则）
    private List<UserLevelDiscount> userLevelDiscounts;
    private List<RegionPricing> regionPricings;
    private List<ChannelPricing> channelPricings;
    private PriorityRule priorityRule;

    /**
     * 计算用户策略折扣
     */
    public BigDecimal calculateUserDiscount(BigDecimal basePrice, UserContext userContext) {
        return calculateUserDiscount(basePrice, userContext, LocalDateTime.now());
    }

    /**
     * 计算用户策略折扣（带时间参数）
     */
    public BigDecimal calculateUserDiscount(BigDecimal basePrice, UserContext userContext, LocalDateTime currentTime) {
        // 验证策略是否激活和在有效期内
        if (!isEffectiveAt(currentTime)) {
            return basePrice;
        }

        BigDecimal finalPrice = basePrice;
        
        // 按优先级应用策略
        if (priorityRule.shouldApplyUserLevel()) {
            finalPrice = applyUserLevelDiscount(finalPrice, userContext.getUserLevel());
        }
        
        if (priorityRule.shouldApplyRegion()) {
            finalPrice = applyRegionPricing(finalPrice, userContext.getRegion());
        }
        
        if (priorityRule.shouldApplyChannel()) {
            finalPrice = applyChannelPricing(finalPrice, userContext.getChannel());
        }

        return finalPrice;
    }

    private BigDecimal applyUserLevelDiscount(BigDecimal price, UserLevel userLevel) {
        return userLevelDiscounts.stream()
                .filter(discount -> discount.matchesLevel(userLevel))
                .findFirst()
                .map(discount -> discount.applyDiscount(price))
                .orElse(price);
    }

    private BigDecimal applyRegionPricing(BigDecimal price, Region region) {
        return regionPricings.stream()
                .filter(pricing -> pricing.matchesRegion(region))
                .findFirst()
                .map(pricing -> pricing.adjustPrice(price))
                .orElse(price);
    }

    private BigDecimal applyChannelPricing(BigDecimal price, Channel channel) {
        return channelPricings.stream()
                .filter(pricing -> pricing.matchesChannel(channel))
                .findFirst()
                .map(pricing -> pricing.adjustPrice(price))
                .orElse(price);
    }

    /**
     * 验证策略是否适用于指定用户
     */
    public boolean isApplicableForUser(UserContext userContext) {
        return isApplicableForUser(userContext, LocalDateTime.now());
    }

    /**
     * 验证策略是否适用于指定用户（带时间参数）
     */
    public boolean isApplicableForUser(UserContext userContext, LocalDateTime currentTime) {
        return isEffectiveAt(currentTime) && 
               (hasMatchingUserLevel(userContext.getUserLevel()) ||
                hasMatchingRegion(userContext.getRegion()) ||
                hasMatchingChannel(userContext.getChannel()));
    }

    /**
     * 验证策略在指定时间是否有效
     */
    public boolean isEffectiveAt(LocalDateTime checkTime) {
        // 基础验证：策略必须是激活状态
        if (!active) {
            return false;
        }

        // 时间范围验证
        if (effectiveStartTime != null && checkTime.isBefore(effectiveStartTime)) {
            return false; // 还未到生效时间
        }

        if (effectiveEndTime != null && checkTime.isAfter(effectiveEndTime)) {
            return false; // 已过期
        }

        // 日期范围验证（如果设置了）
        if (validDateRange != null) {
            LocalDate checkDate = checkTime.toLocalDate();
            return validDateRange.contains(checkDate);
        }

        return true;
    }

    /**
     * 验证策略在指定日期是否有效
     */
    public boolean isEffectiveOn(LocalDate targetDate) {
        // 基础验证
        if (!active) {
            return false;
        }

        // 如果设置了时间范围，检查日期是否在范围内
        if (effectiveStartTime != null && targetDate.isBefore(effectiveStartTime.toLocalDate())) {
            return false;
        }

        if (effectiveEndTime != null && targetDate.isAfter(effectiveEndTime.toLocalDate())) {
            return false;
        }

        // 日期范围验证
        if (validDateRange != null) {
            return validDateRange.contains(targetDate);
        }

        return true;
    }

    /**
     * 获取策略剩余有效时间（小时）
     */
    public long getRemainingValidHours() {
        if (!active || effectiveEndTime == null) {
            return Long.MAX_VALUE; // 永久有效
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(effectiveEndTime)) {
            return 0; // 已过期
        }

        return java.time.Duration.between(now, effectiveEndTime).toHours();
    }

    private boolean hasMatchingUserLevel(UserLevel userLevel) {
        return userLevelDiscounts.stream()
                .anyMatch(discount -> discount.matchesLevel(userLevel));
    }

    private boolean hasMatchingRegion(Region region) {
        return regionPricings.stream()
                .anyMatch(pricing -> pricing.matchesRegion(region));
    }

    private boolean hasMatchingChannel(Channel channel) {
        return channelPricings.stream()
                .anyMatch(pricing -> pricing.matchesChannel(channel));
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<UserLevelDiscount> getUserLevelDiscounts() {
        return userLevelDiscounts;
    }

    public void setUserLevelDiscounts(List<UserLevelDiscount> userLevelDiscounts) {
        this.userLevelDiscounts = userLevelDiscounts;
    }

    public List<RegionPricing> getRegionPricings() {
        return regionPricings;
    }

    public void setRegionPricings(List<RegionPricing> regionPricings) {
        this.regionPricings = regionPricings;
    }

    public List<ChannelPricing> getChannelPricings() {
        return channelPricings;
    }

    public void setChannelPricings(List<ChannelPricing> channelPricings) {
        this.channelPricings = channelPricings;
    }

    public PriorityRule getPriorityRule() {
        return priorityRule;
    }

    public void setPriorityRule(PriorityRule priorityRule) {
        this.priorityRule = priorityRule;
    }

    public PriorityLevel getStrategyPriority() {
        return strategyPriority;
    }

    public void setStrategyPriority(PriorityLevel strategyPriority) {
        this.strategyPriority = strategyPriority;
    }

    public LocalDateTime getEffectiveStartTime() {
        return effectiveStartTime;
    }

    public void setEffectiveStartTime(LocalDateTime effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }

    public LocalDateTime getEffectiveEndTime() {
        return effectiveEndTime;
    }

    public void setEffectiveEndTime(LocalDateTime effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }

    public DateRange getValidDateRange() {
        return validDateRange;
    }

    public void setValidDateRange(DateRange validDateRange) {
        this.validDateRange = validDateRange;
    }

    /**
     * 便捷方法：设置策略有效期
     */
    public void setEffectivePeriod(LocalDateTime startTime, LocalDateTime endTime) {
        this.effectiveStartTime = startTime;
        this.effectiveEndTime = endTime;
    }

    /**
     * 便捷方法：设置策略在某个日期范围内有效
     */
    public void setEffectiveDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            this.validDateRange = new DateRange(startDate, endDate);
        }
    }
} 