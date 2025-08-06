package com.yonhoo.ddd.application;

import com.yonhoo.ddd.domain.model.*;
import com.yonhoo.ddd.domain.service.ComprehensivePricingDomainService;
import com.yonhoo.ddd.domain.service.HotelPricingDomainService;
import com.yonhoo.ddd.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 应用服务层
 * 职责：编排业务流程，协调聚合和领域服务
 */
public class ApplicationService {
    private PriceRuleRepository priceRuleRepository;
    private ProductRepository productRepository;
    private ValidityRepository validityRepository;
    private HotelOfferRepository hotelOfferRepository;
    private PriceDataRepository priceDataRepository;
    
    // 新增策略相关的仓储
    private UserPricingStrategyRepository userPricingStrategyRepository;
    private MarketingPricingStrategyRepository marketingPricingStrategyRepository;

    /**
     * 原始实现 - 应用层直接处理业务逻辑（不推荐）
     */
    public BigDecimal calculateMinPriceV1(String offerNo, LocalDate checkInDay) {
        List<PriceRule> priceRule = priceRuleRepository.queryPriceRuleByOfferNo(offerNo);
        List<HotelProduct> hotelProducts = productRepository.queryHotelProductByOfferNo(offerNo);
        Validity validity = validityRepository.queryValidityByOfferNo(offerNo);

        if (calculateCheckInDayIsAvailable(validity, checkInDay)) {
            throw new RuntimeException("CheckInDay is not available");
        }

        return priceRule.stream().map(priceRuleItem -> calculatePrice(priceRuleItem, hotelProducts, checkInDay))
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo)
                .orElseThrow(() -> new RuntimeException("price is not available"));
    }

    /**
     * 改进版本 - 使用聚合根和领域服务（推荐）
     */
    public BigDecimal calculateMinPriceV2(String offerNo, LocalDate checkInDay) {
        // 1. 获取聚合根
        HotelOffer hotelOffer = hotelOfferRepository.queryHotelOfferByOfferNo(offerNo);
        
        // 2. 获取外部价格数据
        List<String> roomNoList = hotelOffer.getRoomNoList();
        Map<String, PriceData> roomPriceDataMap = priceDataRepository.queryPriceDataByRoomList(roomNoList);

        // 3. 使用领域服务协调聚合和外部数据
        return HotelPricingDomainService.calculateMinPrice(hotelOffer, checkInDay, roomPriceDataMap);
    }

    /**
     * V3版本 - 处理不同的价格数据类型
     */
    public BigDecimal calculateMinPriceV3(String offerNo, LocalDate checkInDay) {
        HotelOffer hotelOffer = hotelOfferRepository.queryHotelOfferByOfferNo(offerNo);
        List<String> roomNoList = hotelOffer.getRoomNoList();
        Map<String, PriceDataV2> roomPriceDataMap = priceDataRepository.queryPriceDataV2ByRoomList(roomNoList);

        // 使用领域服务处理不同版本的价格数据
        return HotelPricingDomainService.calculateMinPrice(hotelOffer, checkInDay, roomPriceDataMap);
    }

    /**
     * V4版本 - 处理不同版本的聚合根
     */
    public BigDecimal calculateMinPriceV4(String offerNo, LocalDate checkInDay) {
        HotelOfferV2 hotelOffer = hotelOfferRepository.queryHotelOfferV2ByOfferNo(offerNo);
        List<String> roomNoList = hotelOffer.getRoomNoList();
        Map<String, PriceDataV2> roomPriceDataMap = priceDataRepository.queryPriceDataV2ByRoomList(roomNoList);

        // 使用领域服务处理不同版本的聚合根
        return HotelPricingDomainService.calculateMinPriceV2(hotelOffer, checkInDay, roomPriceDataMap);
    }

    /**
     * 综合定价服务 - 包含用户策略和营销策略的完整定价流程
     */
    public PricingResult calculateComprehensivePrice(
            String offerNo, 
            LocalDate checkInDay, 
            String userId, 
            UserLevel userLevel, 
            Region region, 
            Channel channel, 
            String sessionId) {

        // 1. 获取酒店产品聚合根
        HotelOffer hotelOffer = hotelOfferRepository.queryHotelOfferByOfferNo(offerNo);
        
        // 2. 获取外部价格数据
        List<String> roomNoList = hotelOffer.getRoomNoList();
        Map<String, PriceData> roomPriceDataMap = priceDataRepository.queryPriceDataByRoomList(roomNoList);

        // 3. 构建用户上下文
        UserContext userContext = new UserContext(userId, userLevel, region, channel, "MEMBER_" + userId);

        // 4. 构建营销上下文
        MarketingContext marketingContext = new MarketingContext(
            LocalDateTime.now(), sessionId, 1, "HOTEL_BOOKING_SYSTEM"
        );

        // 5. 获取适用的用户定价策略
        List<UserPricingStrategy> userStrategies = 
            userPricingStrategyRepository.queryApplicableStrategies(userContext);

        // 6. 获取适用的营销定价策略
        List<MarketingPricingStrategy> marketingStrategies = 
            marketingPricingStrategyRepository.queryEffectiveStrategies(checkInDay, offerNo);

        // 7. 使用综合定价领域服务计算最终价格
        return ComprehensivePricingDomainService.calculateFinalPrice(
            hotelOffer, 
            checkInDay, 
            roomPriceDataMap, 
            userContext, 
            marketingContext, 
            userStrategies, 
            marketingStrategies
        );
    }

    /**
     * 价格趋势分析服务
     */
    public PriceTrendAnalysis analyzePriceTrend(
            String offerNo, 
            LocalDate startDate, 
            LocalDate endDate, 
            String userId, 
            UserLevel userLevel, 
            Region region, 
            Channel channel) {

        // 1. 获取酒店产品聚合根
        HotelOffer hotelOffer = hotelOfferRepository.queryHotelOfferByOfferNo(offerNo);

        // 2. 构建用户上下文
        UserContext userContext = new UserContext(userId, userLevel, region, channel, "MEMBER_" + userId);

        // 3. 获取策略数据
        List<UserPricingStrategy> userStrategies = 
            userPricingStrategyRepository.queryApplicableStrategies(userContext);
        
        List<MarketingPricingStrategy> marketingStrategies = 
            marketingPricingStrategyRepository.queryStrategiesInDateRange(startDate, endDate, offerNo);

        // 4. 执行趋势分析
        DateRange analysisRange = DateRange.of(startDate, endDate);
        return ComprehensivePricingDomainService.analyzePriceTrend(
            hotelOffer, analysisRange, userContext, userStrategies, marketingStrategies
        );
    }

    /**
     * 快速获取今日最优价格（简化版本）
     */
    public BigDecimal getTodayBestPrice(String offerNo, String userId, UserLevel userLevel) {
        LocalDate today = LocalDate.now();
        
        PricingResult result = calculateComprehensivePrice(
            offerNo, today, userId, userLevel, 
            Region.EAST_CHINA, Channel.MOBILE_APP, "QUICK_" + System.currentTimeMillis()
        );
        
        return result.getFinalPrice();
    }

    // === 遗留方法（逐步迁移） ===
    public Boolean calculateCheckInDayIsAvailable(Validity validity, LocalDate checkInDay) {
        return true; // logical processing
    }

    public BigDecimal calculatePrice(PriceRule priceRule, List<HotelProduct> hotelProducts, LocalDate checkInDay) {
        return BigDecimal.ONE; // logical processing
    }

    // === 新增策略仓储的 Setter 方法 ===
    public void setUserPricingStrategyRepository(UserPricingStrategyRepository userPricingStrategyRepository) {
        this.userPricingStrategyRepository = userPricingStrategyRepository;
    }

    public void setMarketingPricingStrategyRepository(MarketingPricingStrategyRepository marketingPricingStrategyRepository) {
        this.marketingPricingStrategyRepository = marketingPricingStrategyRepository;
    }
}
