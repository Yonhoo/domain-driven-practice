package com.yonhoo.ddd.domain.service;

import com.yonhoo.ddd.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 综合定价领域服务
 * 职责：协调 HotelOffer 基础价格、用户策略定价、营销策略定价，计算最终价格
 */
public class ComprehensivePricingDomainService {

    /**
     * 计算综合最终价格
     * 
     * @param hotelOffer 酒店产品聚合根
     * @param checkInDay 入住日期
     * @param roomPriceData 外部价格数据
     * @param userContext 用户上下文
     * @param marketingContext 营销上下文
     * @param userPricingStrategies 用户定价策略列表
     * @param marketingPricingStrategies 营销定价策略列表
     * @return 最终价格结果
     */
    public static PricingResult calculateFinalPrice(
            HotelOffer hotelOffer,
            LocalDate checkInDay,
            Map<String, ? extends AbstractPriceData> roomPriceData,
            UserContext userContext,
            MarketingContext marketingContext,
            List<UserPricingStrategy> userPricingStrategies,
            List<MarketingPricingStrategy> marketingPricingStrategies) {

        // 1. 计算基础价格 (HotelOffer)
        BigDecimal basePrice = HotelPricingDomainService.calculateMinPrice(
            hotelOffer, checkInDay, roomPriceData);

        // 2. 应用用户策略定价
        BigDecimal userDiscountedPrice = applyUserPricingStrategies(
            basePrice, userContext, userPricingStrategies);

        // 3. 应用营销策略定价
        BigDecimal marketingPrice = applyMarketingPricingStrategies(
            userDiscountedPrice, checkInDay, marketingContext, marketingPricingStrategies);

        // 4. 构建定价结果
        return buildPricingResult(basePrice, userDiscountedPrice, marketingPrice, 
            checkInDay, userContext, marketingContext);
    }

    /**
     * 应用用户定价策略
     */
    private static BigDecimal applyUserPricingStrategies(
            BigDecimal basePrice, 
            UserContext userContext, 
            List<UserPricingStrategy> strategies) {

        if (strategies == null || strategies.isEmpty()) {
            return basePrice;
        }

        BigDecimal finalPrice = basePrice;
        BigDecimal bestUserPrice = basePrice;

        // 找到最优的用户策略价格
        for (UserPricingStrategy strategy : strategies) {
            if (strategy.isApplicableForUser(userContext)) {
                BigDecimal strategyPrice = strategy.calculateUserDiscount(basePrice, userContext);
                if (strategyPrice.compareTo(bestUserPrice) < 0) {
                    bestUserPrice = strategyPrice;
                }
            }
        }

        return bestUserPrice;
    }

    /**
     * 应用营销定价策略
     */
    private static BigDecimal applyMarketingPricingStrategies(
            BigDecimal userDiscountedPrice,
            LocalDate checkInDay,
            MarketingContext marketingContext,
            List<MarketingPricingStrategy> strategies) {

        if (strategies == null || strategies.isEmpty()) {
            return userDiscountedPrice;
        }

        // 按优先级排序策略
        strategies.sort(Comparator.comparing(s -> s.getPriorityLevel().getLevel(), Comparator.reverseOrder()));

        BigDecimal bestMarketingPrice = userDiscountedPrice;

        // 应用营销策略，选择最优价格
        for (MarketingPricingStrategy strategy : strategies) {
            if (strategy.isEffective(checkInDay)) {
                BigDecimal strategyPrice = strategy.calculateMarketingPrice(
                    userDiscountedPrice, checkInDay, marketingContext);
                
                if (strategyPrice.compareTo(bestMarketingPrice) < 0) {
                    bestMarketingPrice = strategyPrice;
                }
            }
        }

        return bestMarketingPrice;
    }

    /**
     * 构建定价结果
     */
    private static PricingResult buildPricingResult(
            BigDecimal basePrice,
            BigDecimal userDiscountedPrice, 
            BigDecimal finalPrice,
            LocalDate checkInDay,
            UserContext userContext,
            MarketingContext marketingContext) {

        PricingResult result = new PricingResult();
        result.setBasePrice(basePrice);
        result.setUserDiscountedPrice(userDiscountedPrice);
        result.setFinalPrice(finalPrice);
        
        // 计算折扣金额
        result.setUserDiscountAmount(basePrice.subtract(userDiscountedPrice));
        result.setMarketingDiscountAmount(userDiscountedPrice.subtract(finalPrice));
        result.setTotalDiscountAmount(basePrice.subtract(finalPrice));
        
        // 计算折扣率
        if (basePrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountRate = result.getTotalDiscountAmount()
                    .divide(basePrice, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            result.setDiscountRate(discountRate);
        }

        result.setCheckInDay(checkInDay);
        result.setUserId(userContext.getUserId());
        result.setUserLevel(userContext.getUserLevel());
        result.setCalculationTime(marketingContext.getCurrentTime());

        return result;
    }

    /**
     * 检查价格变化趋势（用于动态定价建议）
     */
    public static PriceTrendAnalysis analyzePriceTrend(
            HotelOffer hotelOffer,
            DateRange dateRange,
            UserContext userContext,
            List<UserPricingStrategy> userStrategies,
            List<MarketingPricingStrategy> marketingStrategies) {

        List<DailyPriceInfo> dailyPrices = new ArrayList<>();
        
        dateRange.toStream().forEach(date -> {
            // 这里需要模拟获取每日价格数据的逻辑
            // 在实际应用中，这可能需要调用外部服务
            MarketingContext context = new MarketingContext(
                date.atStartOfDay(), 
                "trend-analysis", 
                1, 
                "PRICE_ANALYSIS_SYSTEM"
            );
            
            // 简化示例：使用固定的价格数据进行趋势分析
            // 实际场景中需要获取真实的每日价格数据
        });

        return new PriceTrendAnalysis(dailyPrices);
    }
}

/**
 * 定价结果值对象
 */
class PricingResult {
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
    private java.time.LocalDateTime calculationTime;

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

    public java.time.LocalDateTime getCalculationTime() { return calculationTime; }
    public void setCalculationTime(java.time.LocalDateTime calculationTime) { this.calculationTime = calculationTime; }
}

/**
 * 价格趋势分析值对象
 */
class PriceTrendAnalysis {
    private List<DailyPriceInfo> dailyPrices;
    private BigDecimal averagePrice;
    private BigDecimal lowestPrice;
    private BigDecimal highestPrice;
    private LocalDate bestDealDate;

    public PriceTrendAnalysis(List<DailyPriceInfo> dailyPrices) {
        this.dailyPrices = dailyPrices;
        calculateStatistics();
    }

    private void calculateStatistics() {
        if (dailyPrices.isEmpty()) return;

        this.lowestPrice = dailyPrices.stream()
                .map(DailyPriceInfo::getFinalPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        this.highestPrice = dailyPrices.stream()
                .map(DailyPriceInfo::getFinalPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        this.bestDealDate = dailyPrices.stream()
                .min(Comparator.comparing(DailyPriceInfo::getFinalPrice))
                .map(DailyPriceInfo::getDate)
                .orElse(null);
    }

    // Getters
    public List<DailyPriceInfo> getDailyPrices() { return dailyPrices; }
    public BigDecimal getLowestPrice() { return lowestPrice; }
    public BigDecimal getHighestPrice() { return highestPrice; }
    public LocalDate getBestDealDate() { return bestDealDate; }
}

/**
 * 每日价格信息值对象
 */
class DailyPriceInfo {
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