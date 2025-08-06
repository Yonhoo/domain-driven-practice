package com.yonhoo.ddd.domain.service;

import com.yonhoo.ddd.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 用户定价策略使用示例
 * 展示多策略选择和优先级的工作原理
 */
public class UserPricingStrategyExample {

    public static void demonstrateStrategySelection() {
        // 基础价格
        BigDecimal basePrice = new BigDecimal("1000.00");
        
        // 用户上下文：钻石会员，华北地区，移动端
        UserContext userContext = new UserContext(
                "user123", 
                UserLevel.DIAMOND, 
                Region.NORTH_CHINA, 
                Channel.MOBILE_APP, 
                "VIP001"
        );

        // 创建多个定价策略
        List<UserPricingStrategy> strategies = createTestStrategies();

        System.out.println("=== 策略选择演示 ===");
        System.out.println("基础价格: " + basePrice);
        System.out.println("用户信息: " + userContext.getUserLevel() + ", " + 
                          userContext.getRegion() + ", " + userContext.getChannel());
        System.out.println();

        // 1. 最优价格策略（默认）
        System.out.println("1. 最优价格策略选择:");
        BigDecimal bestPrice = UserPricingStrategySelector.calculateBestUserPrice(
                basePrice, userContext, strategies, 
                UserPricingStrategySelector.SelectionMode.BEST_PRICE
        );
        System.out.println("最终价格: " + bestPrice);
        System.out.println();

        // 2. 最高优先级策略
        System.out.println("2. 最高优先级策略选择:");
        BigDecimal priorityPrice = UserPricingStrategySelector.calculateBestUserPrice(
                basePrice, userContext, strategies, 
                UserPricingStrategySelector.SelectionMode.HIGHEST_PRIORITY
        );
        System.out.println("最终价格: " + priorityPrice);
        System.out.println();

        // 3. 第一个适用策略
        System.out.println("3. 第一个适用策略选择:");
        BigDecimal firstPrice = UserPricingStrategySelector.calculateBestUserPrice(
                basePrice, userContext, strategies, 
                UserPricingStrategySelector.SelectionMode.FIRST_APPLICABLE
        );
        System.out.println("最终价格: " + firstPrice);
        System.out.println();

        // 4. 详细分析
        System.out.println("4. 策略详细分析:");
        com.yonhoo.ddd.domain.model.StrategyAnalysisResult analysis = 
                UserPricingStrategySelector.analyzeStrategies(basePrice, userContext, strategies);
        
        System.out.println("分析结果:");
        analysis.getStrategyResults().forEach(result -> {
            UserPricingStrategy strategy = result.getStrategy();
            System.out.println("  策略: " + strategy.getStrategyName() + 
                              " -> 价格: " + result.getCalculatedPrice() +
                              ", 剩余有效时间: " + strategy.getRemainingValidHours() + "小时");
        });

        // 5. 时效性测试
        System.out.println();
        System.out.println("5. 时效性测试:");
        demonstrateTimeEffectiveness();
    }

    /**
     * 演示时效性功能
     */
    private static void demonstrateTimeEffectiveness() {
        BigDecimal basePrice = new BigDecimal("1000.00");
        UserContext userContext = new UserContext(
                "user123", UserLevel.DIAMOND, Region.NORTH_CHINA, Channel.MOBILE_APP, "VIP001"
        );

        // 创建带时效性的策略
        List<UserPricingStrategy> timeBasedStrategies = createTimeBasedStrategies();

        System.out.println("当前时间策略测试:");
        LocalDateTime now = LocalDateTime.now();
        BigDecimal currentPrice = UserPricingStrategySelector.calculateBestUserPrice(
                basePrice, userContext, timeBasedStrategies, 
                UserPricingStrategySelector.SelectionMode.BEST_PRICE, now
        );
        System.out.println("当前最优价格: " + currentPrice);

        System.out.println();
        System.out.println("未来时间策略测试:");
        LocalDateTime futureTime = now.plusDays(3); // 3天后
        BigDecimal futurePrice = UserPricingStrategySelector.calculateBestUserPrice(
                basePrice, userContext, timeBasedStrategies, 
                UserPricingStrategySelector.SelectionMode.BEST_PRICE, futureTime
        );
        System.out.println("3天后最优价格: " + futurePrice);

        System.out.println();
        System.out.println("过期时间策略测试:");
        LocalDateTime pastTime = now.minusDays(10); // 10天前
        BigDecimal pastPrice = UserPricingStrategySelector.calculateBestUserPrice(
                basePrice, userContext, timeBasedStrategies, 
                UserPricingStrategySelector.SelectionMode.BEST_PRICE, pastTime
        );
        System.out.println("10天前价格: " + pastPrice);
    }

    private static List<UserPricingStrategy> createTestStrategies() {
        // 策略1：常规会员优惠
        UserPricingStrategy regularStrategy = new UserPricingStrategy();
        regularStrategy.setStrategyId("strategy-001");
        regularStrategy.setStrategyName("常规会员优惠");
        regularStrategy.setActive(true);
        regularStrategy.setStrategyPriority(PriorityLevel.MEDIUM);
        // 设置为永久有效（不设置结束时间）
        
        UserLevelDiscount diamondDiscount = new UserLevelDiscount();
        diamondDiscount.setTargetLevel(UserLevel.DIAMOND);
        diamondDiscount.setDiscountType(DiscountType.PERCENTAGE);
        diamondDiscount.setDiscountValue(new BigDecimal("10")); // 10%折扣
        diamondDiscount.setMinOrderAmount(BigDecimal.ZERO);
        
        regularStrategy.setUserLevelDiscounts(Arrays.asList(diamondDiscount));

        // 策略2：春节特别优惠（更大折扣，更高优先级）
        UserPricingStrategy springStrategy = new UserPricingStrategy();
        springStrategy.setStrategyId("strategy-002");
        springStrategy.setStrategyName("春节特别优惠");
        springStrategy.setActive(true);
        springStrategy.setStrategyPriority(PriorityLevel.HIGH);
        // 设置春节期间有效（假设已过期）
        springStrategy.setEffectivePeriod(
                LocalDateTime.of(2024, 2, 1, 0, 0),  // 2月1日开始
                LocalDateTime.of(2024, 2, 15, 23, 59) // 2月15日结束
        );
        
        UserLevelDiscount springDiamondDiscount = new UserLevelDiscount();
        springDiamondDiscount.setTargetLevel(UserLevel.DIAMOND);
        springDiamondDiscount.setDiscountType(DiscountType.PERCENTAGE);
        springDiamondDiscount.setDiscountValue(new BigDecimal("15")); // 15%折扣
        springDiamondDiscount.setMinOrderAmount(BigDecimal.ZERO);
        
        springStrategy.setUserLevelDiscounts(Arrays.asList(springDiamondDiscount));

        // 策略3：移动端专享优惠（特定渠道）
        UserPricingStrategy mobileStrategy = new UserPricingStrategy();
        mobileStrategy.setStrategyId("strategy-003");
        mobileStrategy.setStrategyName("移动端专享优惠");
        mobileStrategy.setActive(true);
        mobileStrategy.setStrategyPriority(PriorityLevel.URGENT);
        // 设置移动端优惠从现在开始，7天内有效
        mobileStrategy.setEffectivePeriod(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7)
        );
        
        ChannelPricing mobileChannelPricing = new ChannelPricing();
        mobileChannelPricing.setTargetChannel(Channel.MOBILE_APP);
        mobileChannelPricing.setAdjustmentType(PricingAdjustmentType.DISCOUNT);
        mobileChannelPricing.setAdjustmentValue(new BigDecimal("20")); // 20%折扣
        
        mobileStrategy.setChannelPricings(Arrays.asList(mobileChannelPricing));

        return Arrays.asList(regularStrategy, springStrategy, mobileStrategy);
    }

    /**
     * 创建带时效性的测试策略
     */
    private static List<UserPricingStrategy> createTimeBasedStrategies() {
        LocalDateTime now = LocalDateTime.now();

        // 策略1：已过期的限时优惠
        UserPricingStrategy expiredStrategy = new UserPricingStrategy();
        expiredStrategy.setStrategyId("expired-001");
        expiredStrategy.setStrategyName("已过期限时优惠");
        expiredStrategy.setActive(true);
        expiredStrategy.setStrategyPriority(PriorityLevel.HIGH);
        expiredStrategy.setEffectivePeriod(
                now.minusDays(5),  // 5天前开始
                now.minusDays(1)   // 1天前结束
        );

        UserLevelDiscount expiredDiscount = new UserLevelDiscount();
        expiredDiscount.setTargetLevel(UserLevel.DIAMOND);
        expiredDiscount.setDiscountType(DiscountType.PERCENTAGE);
        expiredDiscount.setDiscountValue(new BigDecimal("25")); // 25%折扣
        expiredDiscount.setMinOrderAmount(BigDecimal.ZERO);
        expiredStrategy.setUserLevelDiscounts(Arrays.asList(expiredDiscount));

        // 策略2：当前有效的策略
        UserPricingStrategy currentStrategy = new UserPricingStrategy();
        currentStrategy.setStrategyId("current-001");
        currentStrategy.setStrategyName("当前有效优惠");
        currentStrategy.setActive(true);
        currentStrategy.setStrategyPriority(PriorityLevel.MEDIUM);
        currentStrategy.setEffectivePeriod(
                now.minusHours(2),  // 2小时前开始
                now.plusDays(5)     // 5天后结束
        );

        UserLevelDiscount currentDiscount = new UserLevelDiscount();
        currentDiscount.setTargetLevel(UserLevel.DIAMOND);
        currentDiscount.setDiscountType(DiscountType.PERCENTAGE);
        currentDiscount.setDiscountValue(new BigDecimal("15")); // 15%折扣
        currentDiscount.setMinOrderAmount(BigDecimal.ZERO);
        currentStrategy.setUserLevelDiscounts(Arrays.asList(currentDiscount));

        // 策略3：未来才生效的策略
        UserPricingStrategy futureStrategy = new UserPricingStrategy();
        futureStrategy.setStrategyId("future-001");
        futureStrategy.setStrategyName("未来生效优惠");
        futureStrategy.setActive(true);
        futureStrategy.setStrategyPriority(PriorityLevel.URGENT);
        futureStrategy.setEffectivePeriod(
                now.plusDays(2),   // 2天后开始
                now.plusDays(10)   // 10天后结束
        );

        UserLevelDiscount futureDiscount = new UserLevelDiscount();
        futureDiscount.setTargetLevel(UserLevel.DIAMOND);
        futureDiscount.setDiscountType(DiscountType.PERCENTAGE);
        futureDiscount.setDiscountValue(new BigDecimal("30")); // 30%折扣
        futureDiscount.setMinOrderAmount(BigDecimal.ZERO);
        futureStrategy.setUserLevelDiscounts(Arrays.asList(futureDiscount));

        return Arrays.asList(expiredStrategy, currentStrategy, futureStrategy);
    }

    public static void main(String[] args) {
        demonstrateStrategySelection();
    }
}