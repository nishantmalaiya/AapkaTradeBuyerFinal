package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.sellerproduct;

/**
 * Created by PPC16 on 7/10/2017.
 */

public class SellerProductData
{
    String product_id,product_name,product_image,category_name,State_name,shop_name,product_status;

    SellerProductData(String product_id, String product_name, String product_image, String category_name, String State_name, String shop_name, String product_status)
    {
        this.product_id= product_id;
        this.product_name = product_name;
        this.product_image= product_image;
        this.category_name = category_name;
        this.State_name = State_name;
        this.shop_name= shop_name;
        this.product_status = product_status;

    }
}
