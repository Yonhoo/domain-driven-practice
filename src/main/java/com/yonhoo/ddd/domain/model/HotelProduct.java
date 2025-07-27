package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class HotelProduct {
    Integer advanceBookingDay;
    NumberOfNights numberOfNights;
    List<RoomInfo> hotelProducts;

    public DateRange minOccupationDateRange(LocalDate checkInDate) {
        return numberOfNights.minOccupationDateRange(checkInDate);
    }

    public Integer getAdvanceBookingDay() {
        return advanceBookingDay;
    }

    public void setAdvanceBookingDay(Integer advanceBookingDay) {
        this.advanceBookingDay = advanceBookingDay;
    }

    public NumberOfNights getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(NumberOfNights numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public List<RoomInfo> getHotelProducts() {
        return hotelProducts;
    }

    public void setHotelProducts(List<RoomInfo> hotelProducts) {
        this.hotelProducts = hotelProducts;
    }
}
