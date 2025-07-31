package com.yonhoo.ddd.application;

import com.yonhoo.ddd.domain.model.*;
import com.yonhoo.ddd.domain.service.HotelPricingDomainService;
import com.yonhoo.ddd.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    private HybridOfferRepository hybridOfferRepository;

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
     * V5版本 - 处理混合版本的聚合根
     */
    public BigDecimal calculateMinPriceV5(String offerNo, LocalDate checkInDay) {
        HybridOffer hybridOffer = hybridOfferRepository.queryHybridOfferByOfferNo(offerNo);
        List<String> roomNoList = hybridOffer.getHotelRoomList();
        List<String> ticketList = hybridOffer.getAttractionTicketList();
        Map<String, PriceDataV2> roomPriceDataMap = priceDataRepository.queryPriceDataV2ByRoomList(roomNoList);
        Map<String, PriceDataV2> ticketPriceDataMap = priceDataRepository.queryPriceDataV2ByRoomList(ticketList);

        roomPriceDataMap.putAll(ticketPriceDataMap);

        return hybridOffer.getMinPriceV3(checkInDay, roomPriceDataMap);
    }

    // === 遗留方法（逐步迁移） ===
    public Boolean calculateCheckInDayIsAvailable(Validity validity, LocalDate checkInDay) {
        return true; // logical processing
    }

    public BigDecimal calculatePrice(PriceRule priceRule, List<HotelProduct> hotelProducts, LocalDate checkInDay) {
        return BigDecimal.ONE; // logical processing
    }
}
