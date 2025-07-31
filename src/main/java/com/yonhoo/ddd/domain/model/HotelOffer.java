package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 酒店产品聚合根
 * 职责：管理酒店产品的核心业务规则和数据一致性
 * 原则：只暴露业务行为，不暴露内部数据结构
 */
public class HotelOffer {
    String offerNo;
    HotelProduct products;
    List<PriceRule> priceRuleList;
    Validity validity;

    /**
     * 验证指定日期是否可以入住（聚合内业务规则）
     */
    public boolean isAvailableForCheckIn(LocalDate checkInDay) {
        return validity.validateCheckInDayIsAvailable(checkInDay);
    }

    /**
     * 计算最低价格（核心业务方法）
     * 接受外部价格数据，但内部计算逻辑完全封装
     */
    public BigDecimal calculateMinPrice(LocalDate checkInDay, Map<String, ? extends AbstractPriceData> roomPriceData) {
        // 内部业务逻辑完全封装，外部无需知道PriceRule、HotelProduct等细节
        return priceRuleList.stream().map(priceRule -> {
                    DateRange occupationDateRange = products.minOccupationDateRange(checkInDay);
                    return occupationDateRange.toStream()
                            .map(calculatedDay -> products.getHotelProducts().stream().map(room ->
                                            priceRule.getPrice(calculatedDay, roomPriceData.get(room.getRoomNo()).getMinPriceByDay(calculatedDay)))
                                    .min(BigDecimal::compareTo)
                                    .orElse(BigDecimal.ZERO))
                            .reduce(BigDecimal::add)
                            .orElseThrow(() -> new RuntimeException("price is not available"));
                })
                .min(BigDecimal::compareTo)
                .orElseThrow(() -> new RuntimeException("price is not available"));
    }

    /**
     * 使用适配器的计算方法 - 更纯粹的领域概念
     * 展示如何进一步增强防腐层
     */
    public BigDecimal calculateMinPriceWithAdapter(LocalDate checkInDay, 
                                                   PriceDataAdapter.RoomPriceQuery priceQuery) {
        // 使用领域友好的价格查询接口，而不是直接依赖外部数据结构
        return priceRuleList.stream().map(priceRule -> {
                    DateRange occupationDateRange = products.minOccupationDateRange(checkInDay);
                    return occupationDateRange.toStream()
                            .map(calculatedDay -> products.getHotelProducts().stream()
                                    .filter(room -> priceQuery.hasDataForRoom(room.getRoomNo()))
                                    .map(room -> priceRule.getPrice(calculatedDay, 
                                                    priceQuery.queryRoomMinPrice(room.getRoomNo(), calculatedDay)))
                                    .min(BigDecimal::compareTo)
                                    .orElse(BigDecimal.ZERO))
                            .reduce(BigDecimal::add)
                            .orElseThrow(() -> new RuntimeException("price is not available"));
                })
                .min(BigDecimal::compareTo)
                .orElseThrow(() -> new RuntimeException("price is not available"));
    }

    /**
     * 获取房间编号列表（必要的对外接口，用于获取外部数据的key）
     */
    public List<String> getRoomNoList() {
        return products.hotelProducts.stream().map(RoomInfo::getRoomNo).toList();
    }

    // === 保持原有方法的向后兼容（标记为遗留代码）===
    @Deprecated
    public BigDecimal getMinPriceV1(LocalDate checkInDay, Map<String, ? extends AbstractPriceData> roomPriceData) {
        // 建议使用 calculateMinPrice() 方法
        return calculateMinPrice(checkInDay, roomPriceData);
    }

    // === 基础的 Getters and Setters（只暴露必要的）===
    public String getOfferNo() {
        return offerNo;
    }

    public void setOfferNo(String offerNo) {
        this.offerNo = offerNo;
    }

    public void setProducts(HotelProduct products) {
        this.products = products;
    }

    public void setPriceRuleList(List<PriceRule> priceRuleList) {
        this.priceRuleList = priceRuleList;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
    }
}
