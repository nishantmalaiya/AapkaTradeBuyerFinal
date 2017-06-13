package com.aapkatrade.buyer.dialogs.track_order.orderdetail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PPC17 on 08-May-17.
 */

public class CommomDataTrackingList implements Parcelable {
    public String imageurl, price, name, id, productname, productPrize, address, shopName, expected_Delivery_Address;


    public CommomDataTrackingList(String imageurl, String price, String name, String id, String productname, String address, String expected_Delivery_Address) {
        this.imageurl = imageurl;
        this.price = price;
        this.name = name;
        this.id = id;
        this.productname = productname;

        this.address = address;

        this.expected_Delivery_Address = expected_Delivery_Address;
    }


    protected CommomDataTrackingList(Parcel in) {
        imageurl = in.readString();
        price = in.readString();
        name = in.readString();
        id = in.readString();
        productname = in.readString();

        address = in.readString();

        expected_Delivery_Address = in.readString();


    }

    public static final Creator<CommomDataTrackingList> CREATOR = new Creator<CommomDataTrackingList>() {
        @Override
        public CommomDataTrackingList createFromParcel(Parcel in) {
            return new CommomDataTrackingList(in);
        }

        @Override
        public CommomDataTrackingList[] newArray(int size) {
            return new CommomDataTrackingList[size];
        }
    };


    @Override
    public String toString() {
        return "CommonData{" +
                "imageurl='" + imageurl + '\'' +
                ", price='" + price + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", productname='" + productname + '\'' +

                ", address='" + address + '\'' +

                ", expected_Delivery_Address='" + expected_Delivery_Address + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageurl);
        dest.writeString(price);
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(productname);

        dest.writeString(address);

        dest.writeString(expected_Delivery_Address);
    }

}