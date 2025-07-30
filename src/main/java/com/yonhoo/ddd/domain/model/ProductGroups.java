package com.yonhoo.ddd.domain.model;


public class ProductGroups {
    private HotelProduct hotelProduct;
    private AttractionProduct attractionProduct;

    public ProductGroups(HotelProduct hotelProduct, AttractionProduct attractionProduct) {
        this.hotelProduct = hotelProduct;
        this.attractionProduct = attractionProduct;
    }

    public HotelProduct getHotelProduct() {
        return hotelProduct;
    }

    public void setHotelProduct(HotelProduct hotelProduct) {
        this.hotelProduct = hotelProduct;
    }

    public AttractionProduct getAttractionProduct() {
        return attractionProduct;
    }

    public void setAttractionProduct(AttractionProduct attractionProduct) {
        this.attractionProduct = attractionProduct;
    }
}
