package com.aapkatrade.buyer.user_dashboard.order_list;

/**
 * Created by PPC16 on 17-Jan-17.
 */

public class OrderListData {


    String order_id, product_price, order_date, product_qty, product_name, image_url;


    public OrderListData(String order_id, String product_name, String product_price, String product_qty, String order_date, String image_url) {
        this.order_id = order_id;
        this.product_qty = product_qty;
        this.product_price = product_price;
        this.product_name = product_name;
        this.image_url = image_url;
        this.order_date = order_date;

    }
}


