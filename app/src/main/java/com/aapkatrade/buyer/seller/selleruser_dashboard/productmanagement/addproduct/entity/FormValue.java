package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.entity;

/**
 * Created by PPC15 on 03-07-2017.
 */

public class FormValue {
    private String title, value;

    public FormValue(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "FormValue{" +
                "title='" + title + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
