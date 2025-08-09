package com.yonhoo.ddd.domain.service;

import com.yonhoo.ddd.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * 酒店定价领域服务
 * 负责协调 HotelOffer 聚合和外部价格数据的业务逻辑
 * 遵循 DDD 聚合边界和封装原则
 */
public class HotelPricingDomainService {

    /**
     * 计算酒店最低价格 - 标准版本
     * 通过聚合根的业务方法进行协调，不暴露内部细节
     */
    public static BigDecimal calculateMinPrice(HotelOffer hotelOffer,
                                               LocalDate checkInDay,
                                               Map<String, ? extends AbstractPriceData> roomPriceData) {

        // 1. 聚合根验证自身业务规则
        if (!hotelOffer.isAvailableForCheckIn(checkInDay)) {
            throw new RuntimeException("checkInDay is not available");
        }

        // 2. 委托给聚合根的业务方法，传入外部数据
        return hotelOffer.calculateMinPrice(checkInDay, roomPriceData);
    }

    /**
     * 计算酒店最低价格 - V2版本（支持客户选择策略）
     */
    public static BigDecimal calculateMinPriceV2(HotelOfferV2 hotelOffer,
                                                 LocalDate checkInDay,
                                                 Map<String, ? extends AbstractPriceData> roomPriceData) {

        if (!hotelOffer.isAvailableForCheckIn(checkInDay)) {
            throw new RuntimeException("checkInDay is not available");
        }

        PriceDataAdapter.RoomPriceQuery priceQuery = PriceDataAdapter.adaptToPriceQuery(roomPriceData);

        // 委托给聚合根的业务方法
        return hotelOffer.calculateMinPrice(checkInDay, priceQuery);
    }
} 