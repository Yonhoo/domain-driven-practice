package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

public class HotelOfferV2 {
    String offerNo;
    HotelProduct products;
    CustomerChoice customerChoice;
    List<PriceRule> priceRuleList;
    Validity validity;

    public CustomerChoice getCustomerChoice() {
        return customerChoice;
    }

    public void setCustomerChoice(CustomerChoice customerChoice) {
        this.customerChoice = customerChoice;
    }

    public BigDecimal getMinPriceV2(LocalDate checkInDay, Map<String, ? extends AbstractPriceData> roomPriceData) {
        if (!validity.validateCheckInDayIsAvailable(checkInDay)) {
            throw new RuntimeException("checkInDay is not available");
        }

        return priceRuleList.stream().map(priceRule -> {

                    DateRange occupationDateRange = products.minOccupationDateRange(checkInDay);
                    return occupationDateRange.toStream()
                            .map(calculatedDay -> products.getHotelProducts().stream().map(room ->
                                            priceRule.getPrice(calculatedDay, roomPriceData.get(room.getRoomNo()).getMinPriceByDay(calculatedDay)))
                                    .reduce(getMinimalPriceCalculateMethod(customerChoice))
                                    .orElse(BigDecimal.ZERO))
                            .reduce(BigDecimal::add)
                            .orElseThrow(() -> new RuntimeException("price is not available"));

                })
                .min(BigDecimal::compareTo)
                .orElseThrow(() -> new RuntimeException("price is not available"));
    }

    private BinaryOperator<BigDecimal> getMinimalPriceCalculateMethod(CustomerChoice customerChoice) {
        if (customerChoice == CustomerChoice.FIXED) {
            return BigDecimal::add;
        } else {
            return BigDecimal::min;
        }
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
