package com.yonhoo.ddd.domain.service;

import com.yonhoo.ddd.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 用户策略 vs 营销策略对比示例
 * 展示两种定价策略的核心区别和应用场景
 */
public class StrategyComparisonExample {

    public static void demonstrateStrategyDifferences() {
        System.out.println("=== 用户策略 vs 营销策略对比分析 ===");
        System.out.println();

        // 基础场景设置
        BigDecimal basePrice = new BigDecimal("1000.00");
        UserContext diamondUser = new UserContext("user001", UserLevel.DIAMOND, Region.NORTH_CHINA, Channel.MOBILE_APP, "VIP001");
        UserContext bronzeUser = new UserContext("user002", UserLevel.BRONZE, Region.SOUTH_CHINA, Channel.OFFICIAL_WEBSITE, "REG002");

        System.out.println("基础价格: " + basePrice);
        System.out.println("钻石用户: " + diamondUser.getUserLevel() + ", " + diamondUser.getRegion() + ", " + diamondUser.getChannel());
        System.out.println("青铜用户: " + bronzeUser.getUserLevel() + ", " + bronzeUser.getRegion() + ", " + bronzeUser.getChannel());
        System.out.println();

        // 创建用户策略和营销策略
        List<UserPricingStrategy> userStrategies = createUserStrategies();
        List<MarketingPricingStrategy> marketingStrategies = createMarketingStrategies();

        // 对比分析
        analyzeUserStrategyImpact(basePrice, diamondUser, bronzeUser, userStrategies);
        analyzeMarketingStrategyImpact(basePrice, diamondUser, marketingStrategies);
        demonstrateComprehensivePricing(basePrice, diamondUser, userStrategies, marketingStrategies);
    }

    /**
     * 分析用户策略的影响
     */
    private static void analyzeUserStrategyImpact(BigDecimal basePrice, UserContext diamondUser, UserContext bronzeUser, List<UserPricingStrategy> strategies) {
        System.out.println("📊 用户策略分析:");
        System.out.println("特点: 基于用户属性的长期稳定定价");
        System.out.println();

        // 钻石用户定价
        BigDecimal diamondPrice = UserPricingStrategySelector.calculateBestUserPrice(
                basePrice, diamondUser, strategies, UserPricingStrategySelector.SelectionMode.BEST_PRICE
        );
        
        // 青铜用户定价
        BigDecimal bronzePrice = UserPricingStrategySelector.calculateBestUserPrice(
                basePrice, bronzeUser, strategies, UserPricingStrategySelector.SelectionMode.BEST_PRICE
        );

        System.out.println("钻石用户最终价格: " + diamondPrice + " (折扣: " + 
                          basePrice.subtract(diamondPrice) + "元)");
        System.out.println("青铜用户最终价格: " + bronzePrice + " (折扣: " + 
                          basePrice.subtract(bronzePrice) + "元)");
        System.out.println("价格差异: " + bronzePrice.subtract(diamondPrice) + "元");
        System.out.println();
    }

    /**
     * 分析营销策略的影响  
     */
    private static void analyzeMarketingStrategyImpact(BigDecimal basePrice, UserContext user, List<MarketingPricingStrategy> strategies) {
        System.out.println("🎯 营销策略分析:");
        System.out.println("特点: 基于市场活动的短期促销定价");
        System.out.println();

        LocalDate normalDay = LocalDate.now();
        LocalDate holidayDay = LocalDate.of(2024, 12, 25); // 圣诞节
        
        MarketingContext normalContext = new MarketingContext(LocalDateTime.now(), "session001", 1, "web");
        MarketingContext holidayContext = new MarketingContext(LocalDateTime.of(2024, 12, 25, 10, 0), "session002", 2, "mobile");

        // 正常日期价格
        BigDecimal normalPrice = applyMarketingStrategies(basePrice, normalDay, normalContext, strategies);
        
        // 节假日价格  
        BigDecimal holidayPrice = applyMarketingStrategies(basePrice, holidayDay, holidayContext, strategies);

        System.out.println("平常日期价格: " + normalPrice + " (调整: " + 
                          normalPrice.subtract(basePrice) + "元)");
        System.out.println("节假日价格: " + holidayPrice + " (调整: " + 
                          holidayPrice.subtract(basePrice) + "元)");
        System.out.println("节假日溢价: " + holidayPrice.subtract(normalPrice) + "元");
        System.out.println();
    }

    /**
     * 演示综合定价流程
     */
    private static void demonstrateComprehensivePricing(BigDecimal basePrice, UserContext user, 
                                                       List<UserPricingStrategy> userStrategies, 
                                                       List<MarketingPricingStrategy> marketingStrategies) {
        System.out.println("🔄 综合定价流程演示:");
        System.out.println("顺序: 基础价格 → 用户策略 → 营销策略 → 最终价格");
        System.out.println();

        // 步骤1: 基础价格
        System.out.println("步骤1 - 基础价格: " + basePrice);

        // 步骤2: 应用用户策略
        BigDecimal userPrice = UserPricingStrategySelector.calculateBestUserPrice(
                basePrice, user, userStrategies, UserPricingStrategySelector.SelectionMode.BEST_PRICE
        );
        System.out.println("步骤2 - 用户策略后: " + userPrice + " (用户折扣: " + 
                          basePrice.subtract(userPrice) + "元)");

        // 步骤3: 应用营销策略 
        LocalDate targetDate = LocalDate.of(2024, 12, 25); // 节假日
        MarketingContext context = new MarketingContext(LocalDateTime.now(), "session001", 1, "mobile");
        BigDecimal finalPrice = applyMarketingStrategies(userPrice, targetDate, context, marketingStrategies);
        
        System.out.println("步骤3 - 营销策略后: " + finalPrice + " (营销调整: " + 
                          finalPrice.subtract(userPrice) + "元)");
        System.out.println("总优惠金额: " + basePrice.subtract(finalPrice) + "元");
        System.out.println("最终折扣率: " + 
                          basePrice.subtract(finalPrice).multiply(new BigDecimal("100"))
                                  .divide(basePrice, 2, java.math.RoundingMode.HALF_UP) + "%");
    }

    /**
     * 模拟营销策略应用（简化版）
     */
    private static BigDecimal applyMarketingStrategies(BigDecimal basePrice, LocalDate targetDate, 
                                                      MarketingContext context, List<MarketingPricingStrategy> strategies) {
        // 这里简化处理，实际应该调用 MarketingPricingStrategy.calculateMarketingPrice
        if (targetDate.getMonthValue() == 12 && targetDate.getDayOfMonth() == 25) {
            // 圣诞节加价20%
            return basePrice.multiply(new BigDecimal("1.2"));
        }
        return basePrice;
    }

    /**
     * 创建示例用户策略
     */
    private static List<UserPricingStrategy> createUserStrategies() {
        // 会员等级策略
        UserPricingStrategy memberStrategy = new UserPricingStrategy();
        memberStrategy.setStrategyName("会员等级优惠");
        memberStrategy.setActive(true);
        memberStrategy.setStrategyPriority(PriorityLevel.MEDIUM);

        // 钻石会员15%折扣
        UserLevelDiscount diamondDiscount = new UserLevelDiscount();
        diamondDiscount.setTargetLevel(UserLevel.DIAMOND);
        diamondDiscount.setDiscountType(DiscountType.PERCENTAGE);
        diamondDiscount.setDiscountValue(new BigDecimal("15"));
        diamondDiscount.setMinOrderAmount(BigDecimal.ZERO);

        // 青铜会员5%折扣
        UserLevelDiscount bronzeDiscount = new UserLevelDiscount();
        bronzeDiscount.setTargetLevel(UserLevel.BRONZE);
        bronzeDiscount.setDiscountType(DiscountType.PERCENTAGE);
        bronzeDiscount.setDiscountValue(new BigDecimal("5"));
        bronzeDiscount.setMinOrderAmount(BigDecimal.ZERO);

        memberStrategy.setUserLevelDiscounts(Arrays.asList(diamondDiscount, bronzeDiscount));

        // 渠道策略
        UserPricingStrategy channelStrategy = new UserPricingStrategy();
        channelStrategy.setStrategyName("移动端优惠");
        channelStrategy.setActive(true);
        channelStrategy.setStrategyPriority(PriorityLevel.HIGH);

        ChannelPricing mobileDiscount = new ChannelPricing();
        mobileDiscount.setTargetChannel(Channel.MOBILE_APP);
        mobileDiscount.setAdjustmentType(PricingAdjustmentType.DISCOUNT);
        mobileDiscount.setAdjustmentValue(new BigDecimal("10")); // 额外10%折扣

        channelStrategy.setChannelPricings(Arrays.asList(mobileDiscount));

        return Arrays.asList(memberStrategy, channelStrategy);
    }

    /**
     * 创建示例营销策略
     */
    private static List<MarketingPricingStrategy> createMarketingStrategies() {
        // 这里返回空列表，实际实现中会创建具体的营销策略
        return Arrays.asList();
    }

    public static void main(String[] args) {
        demonstrateStrategyDifferences();
    }
}