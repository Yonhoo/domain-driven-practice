package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

/**
 * 酒店产品聚合根V2
 * 支持客户选择策略的版本
 */
public class HotelOfferV2 {
    String offerNo;
    HotelProduct products;
    CustomerChoice customerChoice;
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
     * 支持客户选择策略，内部逻辑完全封装
     */
    public BigDecimal calculateMinPrice(LocalDate checkInDay, PriceDataAdapter.RoomPriceQuery priceQuery) {
        return priceRuleList.stream().map(priceRule -> {
                    DateRange occupationDateRange = products.minOccupationDateRange(checkInDay);
                    return occupationDateRange.toStream()
                            .map(calculatedDay -> products.getHotelProducts().stream().map(room ->
                                            priceRule.getPrice(calculatedDay, priceQuery.queryRoomMinPrice(room.getRoomNo(), calculatedDay)))
                                    .reduce(getMinimalPriceCalculateMethod(customerChoice))
                                    .orElse(BigDecimal.ZERO))
                            .reduce(BigDecimal::add)
                            .orElseThrow(() -> new RuntimeException("price is not available"));
                })
                .min(BigDecimal::compareTo)
                .orElseThrow(() -> new RuntimeException("price is not available"));
    }

    /**
     * 获取房间编号列表（必要的对外接口）
     */
    public List<String> getRoomNoList() {
        return products.hotelProducts.stream().map(RoomInfo::getRoomNo).toList();
    }

    /**
     * 根据客户选择确定价格计算方法（内部策略）
     */
    private BinaryOperator<BigDecimal> getMinimalPriceCalculateMethod(CustomerChoice customerChoice) {
        if (customerChoice == CustomerChoice.FIXED) {
            return BigDecimal::add;
        } else {
            return BigDecimal::min;
        }
    }

    // === 基础的 Getters and Setters ===
    public CustomerChoice getCustomerChoice() {
        return customerChoice;
    }

    public void setCustomerChoice(CustomerChoice customerChoice) {
        this.customerChoice = customerChoice;
    }

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
