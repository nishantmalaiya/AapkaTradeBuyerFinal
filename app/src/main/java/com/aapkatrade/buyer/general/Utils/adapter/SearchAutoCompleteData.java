package com.aapkatrade.buyer.general.Utils.adapter;

/**
 * Created by PPC16 on 7/24/2017.
 */

public class SearchAutoCompleteData
{

    private String product_id;
    private String shop_name;
    private String shop_distance;
    private String category_name;
    private String shop_location;

    public SearchAutoCompleteData(String product_id, String shop_name, String shop_distance, String category_name, String shop_location)
    {
        this.product_id = product_id;
        this.shop_name = shop_name;
        this.shop_distance = shop_distance;
        this.category_name = category_name;
        this.shop_location = shop_location;

    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_distance() {
        return shop_distance;
    }

    public void setShop_distance(String shop_distance) {
        this.shop_distance = shop_distance;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getShop_location() {
        return shop_location;
    }

    public void setShop_location(String shop_location) {
        this.shop_location = shop_location;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
