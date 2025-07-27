package com.yonhoo.ddd.domain.service;




import com.yonhoo.ddd.domain.model.DateRange;
import com.yonhoo.ddd.domain.model.HotelProduct;
import com.yonhoo.ddd.domain.model.PriceData;
import com.yonhoo.ddd.domain.model.PriceRule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


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
}
