package com.aapkatrade.buyer.shopdetail.shop_all_product;

/**
 * Created by PPC16 on 4/21/2017.
 */

public class ShopAllProductData {

    public String productId, productName, productShortDescription, productPrice, productImage,product_qty;

    public ShopAllProductData(String productId, String productName, String productShortDescription, String productPrice, String productImage,String product_qty) {
        this.productId = productId;
        this.productName = productName;
        this.productShortDescription = productShortDescription;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.product_qty = product_qty;
    }

}
