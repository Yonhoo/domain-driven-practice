package com.yonhoo.ddd.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
public class HybridOffer {

    private ProductGroups productGroups;

    private String offerNo;

    private List<PriceRule> priceRuleList;

    private Validity validity;


    private CustomerChoice customerChoice;

    public BigDecimal getMinPriceV3(LocalDate checkInDay, Map<String, ? extends AbstractPriceData> priceData) {
        if (!validity.validateCheckInDayIsAvailable(checkInDay)) {
            throw new RuntimeException("checkInDay is not available");
        }

        HotelProduct hotelProduct = productGroups.getHotelProduct();

        BigDecimal hotelPrice = priceRuleList.stream().map(priceRule -> {

            DateRange occupationDateRange = hotelProduct.minOccupationDateRange(checkInDay);
            return occupationDateRange.toStream().map(calculatedDay -> hotelProduct.getHotelProducts().stream().map(room -> priceRule.getPrice(calculatedDay, priceData.get(room.getRoomNo()).getMinPriceByDay(calculatedDay))).reduce(getMinimalPriceCalculateMethod(customerChoice)).orElse(BigDecimal.ZERO)).reduce(BigDecimal::add).orElseThrow(() -> new RuntimeException("price is not available"));

        }).min(BigDecimal::compareTo).orElseThrow(() -> new RuntimeException("price is not available"));

        AttractionProduct attractionProduct = productGroups.getAttractionProduct();

        BigDecimal attractionPrice = priceRuleList.stream().map(priceRule -> attractionProduct.getProductItemList().stream().map(ticket -> priceRule.getPrice(checkInDay, priceData.get(ticket.getProductNumber()).getMinPriceByDay(checkInDay))).reduce(getMinimalPriceCalculateMethod(customerChoice)).orElse(BigDecimal.valueOf(Integer.MAX_VALUE))).min(BigDecimal::compareTo).orElseThrow(() -> new RuntimeException("price is not available"));

        return hotelPrice.add(attractionPrice);
    }


    public List<String> getHotelRoomList() {
        return productGroups.getHotelProduct().getHotelProducts().stream().map(RoomInfo::getRoomNo).collect(Collectors.toList());
    }

    public List<String> getAttractionTicketList() {
        return productGroups.getAttractionProduct().getProductItemList().stream().map(TicketItem::getProductNumber).collect(Collectors.toList());
    }


    private BinaryOperator<BigDecimal> getMinimalPriceCalculateMethod(CustomerChoice customerChoice) {
        if (customerChoice == CustomerChoice.FIXED) {
            return BigDecimal::add;
        } else {
            return BigDecimal::min;
        }
    }
}
