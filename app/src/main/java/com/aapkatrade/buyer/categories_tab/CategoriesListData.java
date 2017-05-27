package com.aapkatrade.buyer.categories_tab;

/**
 * Created by PPC16 on 16-Jan-17.
 */

public class CategoriesListData {

    public String shopId, shopName, shopImage, shopLocation, distance,shopCategory;

    public CategoriesListData(String shopId, String shopName, String shopImage, String shopLocation, String distance,String shopCategory) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopImage = shopImage;
        this.shopLocation = shopLocation;
        this.shopCategory =shopCategory;
        this.distance = distance;
    }
}

