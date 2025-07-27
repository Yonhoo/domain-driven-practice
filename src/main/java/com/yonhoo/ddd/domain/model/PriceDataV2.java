package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PriceDataV2 {
    private LocalDate date;
    private BigDecimal price;

    private String roomNo;

    private List<TimingPrice> timingPriceList;


    public List<TimingPrice> getTimingPriceList() {
        return timingPriceList;
    }

    public void setTimingPriceList(List<TimingPrice> timingPriceList) {
        this.timingPriceList = timingPriceList;
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

    public BigDecimal getMinPrice() {
        return timingPriceList.stream().map(TimingPrice::getPrice)
                .min(BigDecimal::compareTo)
                .orElseThrow(() -> new IllegalStateException("price is invalid"));
    }

    private class TimingPrice {
        private BigDecimal price;
        private LocalTime timing;

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public LocalTime getTiming() {
            return timing;
        }

        public void setTiming(LocalTime timing) {
            this.timing = timing;
        }
    }
}
