package com.yonhoo.ddd.application;



import com.yonhoo.ddd.domain.model.*;
import com.yonhoo.ddd.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ApplicationService {
    private PriceRuleRepository priceRuleRepository;

    private ProductRepository productRepository;

    private ValidityRepository validityRepository;


    private HotelOfferRepository hotelOfferRepository;


    private PriceDataRepository priceDataRepository;


    public BigDecimal calculateMinPriceV1(String offerNo, LocalDate checkInDay) {

        List<PriceRule> priceRule = priceRuleRepository.queryPriceRuleByOfferNo(offerNo);

        List<HotelProduct> hotelProducts = productRepository.queryHotelProductByOfferNo(offerNo);

        Validity validity = validityRepository.queryValidityByOfferNo(offerNo);

        if (calculateCheckInDayIsAvailable(validity, checkInDay)) {
            throw new RuntimeException("CheckInDay is not available");
        }

        return priceRule.stream().map(priceRuleItem -> calculatePrice(priceRuleItem, hotelProducts, checkInDay))
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo)
                .orElseThrow(() -> new RuntimeException("price is not available"));
    }


    public BigDecimal calculateMinPriceV2(String offerNo, LocalDate checkInDay) {

        HotelOffer hotelOffer = hotelOfferRepository.queryHotelOfferByOfferNo(offerNo);

        List<String> roomNoList = hotelOffer.getRoomNoList();

        Map<String, PriceData> roomPriceDataMap = priceDataRepository.queryPriceDataByRoomList(roomNoList);

        return hotelOffer.getMinPriceByCheckInDay(checkInDay, roomPriceDataMap);
    }


    public Boolean calculateCheckInDayIsAvailable(Validity validity, LocalDate checkInDay) {
        // logical processing

        return true;
    }

    public BigDecimal calculatePrice(PriceRule priceRule, List<HotelProduct> hotelProducts, LocalDate checkInDay) {
        // logical processing

        return BigDecimal.ONE;
    }
}
