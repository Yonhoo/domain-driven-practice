package com.yonhoo.ddd.domain.model;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Map;


@Builder
public class CalculatedContext {
    private LocalDate checkInDay;
    private Map<String, PriceDataV2> roomPriceData;
    private PriceRule priceRule;
    private HotelProduct hotelProduct;
    private CustomerChoice customerChoice;


    public CustomerChoice getCustomerChoice() {
        return customerChoice;
    }

    public void setCustomerChoice(CustomerChoice customerChoice) {
        this.customerChoice = customerChoice;
    }

    public LocalDate getCheckInDay() {
        return checkInDay;
    }

    public void setCheckInDay(LocalDate checkInDay) {
        this.checkInDay = checkInDay;
    }

    public Map<String, PriceDataV2> getRoomPriceData() {
        return roomPriceData;
    }

    public void setRoomPriceData(Map<String, PriceDataV2> roomPriceData) {
        this.roomPriceData = roomPriceData;
    }

    public PriceRule getPriceRule() {
        return priceRule;
    }

    public void setPriceRule(PriceRule priceRule) {
        this.priceRule = priceRule;
    }

    public HotelProduct getHotelProduct() {
        return hotelProduct;
    }

    public void setHotelProduct(HotelProduct hotelProduct) {
        this.hotelProduct = hotelProduct;
    }
}
