package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PriceData {
    private LocalDate date;
    private BigDecimal price;

    private String roomNo;


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
}
