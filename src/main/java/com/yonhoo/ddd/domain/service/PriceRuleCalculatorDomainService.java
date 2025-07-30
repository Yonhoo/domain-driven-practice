package com.yonhoo.ddd.domain.service;


import com.yonhoo.ddd.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.function.BinaryOperator;


public class PriceRuleCalculatorDomainService {


    public static BigDecimal calculateItemDiscountedUnitPrice(LocalDate checkInDay,
                                                              Map<String, PriceData> roomPriceData,
                                                              PriceRule priceRule,
                                                              HotelProduct hotelProduct) {
        DateRange occupationDateRange = hotelProduct.minOccupationDateRange(checkInDay);
        return occupationDateRange.toStream()
                .map(calculatedDay -> hotelProduct.getHotelProducts().stream().map(room ->
                                priceRule.getPrice(calculatedDay, roomPriceData.get(room.getRoomNo()).getPrice()))
                        .min(BigDecimal::compareTo)
                        .orElse(BigDecimal.ZERO))
                .reduce(BigDecimal::add)
                .orElseThrow(() -> new RuntimeException("price is not available"));
    }

    public static BigDecimal calculateAttractionItemDiscountedUnitPrice(CalculatedContext calculatedContext) {

        AttractionProduct attractionProduct = calculatedContext.getAttractionProduct();

        PriceRule priceRule = calculatedContext.getPriceRule();

        Map<String, PriceDataV2> priceData = calculatedContext.getPriceData();

        Integer minQuantity = attractionProduct.getMinQuantity();

        LocalDate calculatedDay = calculatedContext.getCheckInDay();

        return attractionProduct.getProductItemList().stream()
                .map(attraction ->
                        BigDecimal.valueOf(minQuantity)
                                .multiply(priceRule.getPrice(calculatedDay, priceData.get(attraction.getTicketCode()).getPrice())))
                .min(BigDecimal::compareTo)
                .orElseThrow(() -> new RuntimeException("price is not available"));
    }

    public static BigDecimal calculateItemDiscountedUnitPriceV2(LocalDate checkInDay,
                                                                Map<String, PriceDataV2> roomPriceData,
                                                                PriceRule priceRule,
                                                                HotelProduct hotelProduct) {
        DateRange occupationDateRange = hotelProduct.minOccupationDateRange(checkInDay);
        return occupationDateRange.toStream()
                .map(calculatedDay -> hotelProduct.getHotelProducts().stream().map(room ->
                                priceRule.getPrice(calculatedDay, roomPriceData.get(room.getRoomNo()).getMinPrice()))
                        .min(BigDecimal::compareTo)
                        .orElse(BigDecimal.ZERO))
                .reduce(BigDecimal::add)
                .orElseThrow(() -> new RuntimeException("price is not available"));
    }


    public static BigDecimal calculateHybridItemDiscountedUnitPriceV3(CalculatedContext calculatedContext) {
        HotelProduct hotelProduct = calculatedContext.getHotelProduct();

        LocalDate checkInDay = calculatedContext.getCheckInDay();

        PriceRule priceRule = calculatedContext.getPriceRule();

        Map<String, PriceDataV2> roomPriceData = calculatedContext.getPriceData();

        DateRange occupationDateRange = hotelProduct.minOccupationDateRange(checkInDay);

        return BigDecimal.ONE;

    }


    public static BigDecimal calculateItemDiscountedUnitPriceV3(CalculatedContext calculatedContext) {

        HotelProduct hotelProduct = calculatedContext.getHotelProduct();

        LocalDate checkInDay = calculatedContext.getCheckInDay();

        PriceRule priceRule = calculatedContext.getPriceRule();

        Map<String, PriceDataV2> roomPriceData = calculatedContext.getPriceData();

        DateRange occupationDateRange = hotelProduct.minOccupationDateRange(checkInDay);

        return occupationDateRange.toStream()
                .map(calculatedDay -> hotelProduct.getHotelProducts().stream().map(room ->
                                priceRule.getPrice(calculatedDay, roomPriceData.get(room.getRoomNo()).getMinPrice()))
                        .reduce(getMinimalPriceCalculateMethod(calculatedContext.getCustomerChoice()))
                        .orElse(BigDecimal.ZERO))
                .reduce(BigDecimal::add)
                .orElseThrow(() -> new RuntimeException("price is not available"));
    }

    private static BinaryOperator<BigDecimal> getMinimalPriceCalculateMethod(CustomerChoice customerChoice) {
        if (customerChoice == CustomerChoice.FIXED) {
            return BigDecimal::add;
        } else {
            return BigDecimal::min;
        }
    }

}
