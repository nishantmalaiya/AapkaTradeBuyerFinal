package com.aapkatrade.buyer.dialogs.track_order.orderdetail;

/**
 * Created by PPC17 on 05-May-17.
 */

public class ProductDatas {
    String product_name,product_qty_order,product_price_order,product_net_price,discount,image_url;

    public ProductDatas(String product_name, String product_qty_order, String product_price_order, String product_net_price, String discount, String image_url) {
        this.product_name = product_name;
        this.product_qty_order = product_qty_order;
        this.product_price_order = product_price_order;
        this.product_net_price = product_net_price;
        this.discount = discount;
        this.image_url = image_url;
    }
}
