package com.yonhoo.ddd.domain.model;


import com.yonhoo.ddd.domain.service.PriceRuleCalculatorDomainService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class HotelOffer {
    String offerNo;
    HotelProduct products;
    List<PriceRule> priceRuleList;
    Validity validity;

    public BigDecimal getMinPriceByCheckInDay(LocalDate checkInDay, Map<String, PriceData> roomPriceData) {

        if (!validity.validateCheckInDayIsAvailable(checkInDay)) {
            throw new RuntimeException("checkInDay is not available");
        }

        return priceRuleList.stream().map(priceRule ->
                        PriceRuleCalculatorDomainService.calculateItemDiscountedUnitPrice(checkInDay, roomPriceData, priceRule, products))
                .min(BigDecimal::compareTo)
                .orElseThrow(() -> new RuntimeException("price is not available"));
    }


    public List<String> getRoomNoList() {
        return products.hotelProducts.stream().map(RoomInfo::getRoomNo).toList();
    }


    public String getOfferNo() {
        return offerNo;
    }

    public void setOfferNo(String offerNo) {
        this.offerNo = offerNo;
    }

    public HotelProduct getProducts() {
        return products;
    }

    public void setProducts(HotelProduct products) {
        this.products = products;
    }

    public List<PriceRule> getPriceRuleList() {
        return priceRuleList;
    }

    public void setPriceRuleList(List<PriceRule> priceRuleList) {
        this.priceRuleList = priceRuleList;
    }

    public Validity getValidity() {
        return validity;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
    }
}
