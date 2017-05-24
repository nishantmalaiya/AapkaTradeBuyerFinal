package com.aapkatrade.buyer.dialogs.track_order.orderdetail;

/**
 * Created by PPC17 on 05-May-17.
 */

public class OrderDetailsDatas {
    String orderId, name, email, phone, pincode, address, city, state, product_qty, product_price, vpc_AuthorizeId, vpc_BatchNo, vpc_Card, vpc_TransactionNo, payment_datetime;


    public OrderDetailsDatas(String orderId, String name, String email, String phone, String pincode, String address, String city, String state, String product_qty, String product_price, String vpc_AuthorizeId, String vpc_BatchNo, String vpc_Card, String vpc_TransactionNo, String payment_datetime) {
        this.orderId = orderId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pincode = pincode;
        this.address = address;
        this.city = city;
        this.state = state;
        this.product_qty = product_qty;
        this.product_price = product_price;
        this.vpc_AuthorizeId = vpc_AuthorizeId;
        this.vpc_BatchNo = vpc_BatchNo;
        this.vpc_Card = vpc_Card;
        this.vpc_TransactionNo = vpc_TransactionNo;
        this.payment_datetime = payment_datetime;
    }
}
