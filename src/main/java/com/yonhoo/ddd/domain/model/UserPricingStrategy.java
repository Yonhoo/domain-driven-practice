package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 用户定价策略聚合根
 * 职责：管理基于用户属性的定价策略（等级、地域、渠道等）
 */
public class UserPricingStrategy {
    private String strategyId;
    private String strategyName;
    private boolean active;
    private List<UserLevelDiscount> userLevelDiscounts;
    private List<RegionPricing> regionPricings;
    private List<ChannelPricing> channelPricings;
    private PriorityRule priorityRule;

    /**
     * 计算用户策略折扣
     */
    public BigDecimal calculateUserDiscount(BigDecimal basePrice, UserContext userContext) {
        // 验证策略是否激活
        if (!active) {
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
        return active && 
               (hasMatchingUserLevel(userContext.getUserLevel()) ||
                hasMatchingRegion(userContext.getRegion()) ||
                hasMatchingChannel(userContext.getChannel()));
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
} 