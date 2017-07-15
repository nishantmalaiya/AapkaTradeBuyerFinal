package com.aapkatrade.buyer.seller.selleruser_dashboard.servicemanagment.entity;

/**
 * Created by PPC16 on 7/10/2017.
 */

public class ServiceListData {
    public String service_id, service_name, service_image, service_category_name, service_shop_name;

    public ServiceListData(String service_id, String service_name, String service_image, String service_category_name, String service_shop_name) {
        this.service_id = service_id;
        this.service_name = service_name;
        this.service_image = service_image;
        this.service_category_name = service_category_name;

        this.service_shop_name = service_shop_name;


    }
}
