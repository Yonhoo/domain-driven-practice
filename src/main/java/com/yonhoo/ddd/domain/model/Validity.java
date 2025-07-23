package com.yonhoo.ddd.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Validity {
    private LocalDate salesStartDate;
    private LocalDate salesEndDate;
    private LocalDate visitingStartDate;
    private LocalDate visitingEndDate;
    private LocalDateTime publishTime;
    private LocalDateTime unpublishTime;
    private Integer advanceBookingDays;
    private List<DateRange> blackOutDateRanges;


    public boolean validateCheckInDayIsAvailable(LocalDate checkInDay) {
        return true;
    }

    public LocalDate getSalesStartDate() {
        return salesStartDate;
    }

    public void setSalesStartDate(LocalDate salesStartDate) {
        this.salesStartDate = salesStartDate;
    }

    public LocalDate getSalesEndDate() {
        return salesEndDate;
    }

    public void setSalesEndDate(LocalDate salesEndDate) {
        this.salesEndDate = salesEndDate;
    }

    public LocalDate getVisitingStartDate() {
        return visitingStartDate;
    }

    public void setVisitingStartDate(LocalDate visitingStartDate) {
        this.visitingStartDate = visitingStartDate;
    }

    public LocalDate getVisitingEndDate() {
        return visitingEndDate;
    }

    public void setVisitingEndDate(LocalDate visitingEndDate) {
        this.visitingEndDate = visitingEndDate;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public LocalDateTime getUnpublishTime() {
        return unpublishTime;
    }

    public void setUnpublishTime(LocalDateTime unpublishTime) {
        this.unpublishTime = unpublishTime;
    }

    public Integer getAdvanceBookingDays() {
        return advanceBookingDays;
    }

    public void setAdvanceBookingDays(Integer advanceBookingDays) {
        this.advanceBookingDays = advanceBookingDays;
    }

    public List<DateRange> getBlackOutDateRanges() {
        return blackOutDateRanges;
    }

    public void setBlackOutDateRanges(List<DateRange> blackOutDateRanges) {
        this.blackOutDateRanges = blackOutDateRanges;
    }
}
