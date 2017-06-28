package com.aapkatrade.buyer.home.cart;

/**
 * Created by PPC16 on 4/25/2017.
 */

public class CartData
{
    public String productName,id,price,quantity,product_image,product_id,subtotal_price,shipping_cost,available_status;

    public CartData(String id, String productName, String quantity,String price,String product_image,String product_id,String subtotal_price,String available_status,String shipping_cost)
    {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.price=price;
        this.product_image = product_image;
        this.product_id = product_id;
        this.subtotal_price= subtotal_price;
        this.available_status = available_status;
        this.shipping_cost = shipping_cost;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProduct_id() {
        return id;
    }

    public void setProduct_id(String product_id) {
        this.id = product_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }
}
