package com.yonhoo.ddd.domain.model;

import java.time.LocalDate;

public class NumberOfNights {
    int minNight;
    int maxNight;

    public DateRange minOccupationDateRange(LocalDate arrivingDate) {
        LocalDate departureDate = arrivingDate.plusDays(minNight - 1L);
        return DateRange.of(arrivingDate, departureDate);
    }

    public int getMinNight() {
        return minNight;
    }

    public void setMinNight(int minNight) {
        this.minNight = minNight;
    }

    public int getMaxNight() {
        return maxNight;
    }

    public void setMaxNight(int maxNight) {
        this.maxNight = maxNight;
    }
}
