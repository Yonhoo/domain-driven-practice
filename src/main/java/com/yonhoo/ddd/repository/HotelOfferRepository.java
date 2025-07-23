package com.yonhoo.ddd.repository;


import com.yonhoo.ddd.domain.model.HotelOffer;

public interface HotelOfferRepository {
    HotelOffer queryHotelOfferByOfferNo(String offerNo);
}
