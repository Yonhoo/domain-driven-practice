package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PriceDataV2 extends AbstractPriceData {

    private String roomNo;

    private List<TimingPrice> timingPriceList;


    public List<TimingPrice> getTimingPriceList() {
        return timingPriceList;
    }

    public void setTimingPriceList(List<TimingPrice> timingPriceList) {
        this.timingPriceList = timingPriceList;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    @Override
    public BigDecimal getMinPriceByDay(LocalDate day) {
        return timingPriceList.stream()
                .filter(timingPrice -> timingPrice.getDay().isEqual(day))
                .map(TimingPrice::getPrice)
                .min(BigDecimal::compareTo)
                .orElseThrow(() -> new IllegalStateException("price is invalid"));
    }

    private class TimingPrice {
        private BigDecimal price;
        private LocalTime timing;

        private LocalDate day;

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

        public LocalDate getDay() {
            return day;
        }

        public void setDay(LocalDate day) {
            this.day = day;
        }
    }
}
