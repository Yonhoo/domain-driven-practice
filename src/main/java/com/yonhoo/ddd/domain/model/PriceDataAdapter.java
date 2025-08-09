package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * 价格数据适配器 - 增强防腐层
 * 将外部价格数据转换为聚合根友好的领域概念
 */
public class PriceDataAdapter {

    /**
     * 将外部价格数据适配为聚合根内部使用的价格查询器
     */
    public static RoomPriceQuery adaptToPriceQuery(Map<String, ? extends AbstractPriceData> externalPriceData) {
        return new RoomPriceQuery() {
            @Override
            public BigDecimal queryRoomMinPrice(String roomNo, LocalDate day) {
                AbstractPriceData priceData = externalPriceData.get(roomNo);
                if (priceData == null) {
                    throw new RuntimeException("No price data found for room: " + roomNo);
                }
                return priceData.getMinPriceByDay(day);
            }

            @Override
            public boolean hasDataForRoom(String roomNo) {
                return externalPriceData.containsKey(roomNo);
            }
        };
    }

    /**
     * 聚合根内部使用的价格查询接口 - 领域概念
     */
    public interface RoomPriceQuery {
        BigDecimal queryRoomMinPrice(String roomNo, LocalDate day);

        boolean hasDataForRoom(String roomNo);
    }
} 