package com.aapkatrade.buyer.home;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Netforce on 7/25/2016.
 */
public class CommonData implements Parcelable
{
    public String imageurl,price,name,id,product_location, categoryName,distance,shop_location;

    public CommonData(String id, String name, String price, String imageurl){
        this.id=id;
        this.imageurl=imageurl;
        this.price=price;
        this.name=name;

    }
    public CommonData(String id, String name, String price, String imageurl, String product_location){
        this.id=id;
        this.imageurl=imageurl;
        this.price=price;
        this.name=name;
        this.product_location=product_location;
    }






    public CommonData(String id, String name, String price, String imageurl, String product_location, String categoryName){
        this.id=id;
        this.imageurl=imageurl;
        this.price=price;
        this.name=name;
        this.product_location=product_location;
        this.categoryName = categoryName;
    }


    public CommonData(String id, String name, String price, String imageurl, String product_location, String categoryName, String distance, String shop_location){
        this.id=id;
        this.imageurl=imageurl;
        this.price=price;
        this.name=name;
        this.product_location=product_location;
        this.categoryName = categoryName;
        this.distance = distance;
        this.shop_location = shop_location;
    }

    protected CommonData(Parcel in) {
        imageurl = in.readString();
        price = in.readString();
        name = in.readString();
        id = in.readString();
        product_location=in.readString();

    }

    public static final Creator<CommonData> CREATOR = new Creator<CommonData>() {
        @Override
        public CommonData createFromParcel(Parcel in) {
            return new CommonData(in);
        }

        @Override
        public CommonData[] newArray(int size) {
            return new CommonData[size];
        }
    };

    @Override
    public String toString() {
        return "CommonData{" +
                "imageurl='" + imageurl + '\'' +
                ", price='" + price + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
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
        dest.writeString(product_location);
    }
}
