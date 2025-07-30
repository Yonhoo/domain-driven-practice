package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class PriceData extends AbstractPriceData {
    private LocalDate date;
    private BigDecimal price;
    private String roomNo;

    private List<PricePair> pricePairs;

    @Override
    public BigDecimal getMinPriceByDay(LocalDate day) {
        return pricePairs.stream().filter(item -> day.isEqual(item.getDay()))
                .map(PricePair::getPrice)
                .min(BigDecimal::compareTo)
                .orElseThrow(() -> new RuntimeException("no available price"));
    }

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


    public List<PricePair> getPricePairs() {
        return pricePairs;
    }

    public void setPricePairs(List<PricePair> pricePairs) {
        this.pricePairs = pricePairs;
    }
}
