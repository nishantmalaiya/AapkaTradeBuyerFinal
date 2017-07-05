package com.aapkatrade.buyer.general.Utils;

import com.aapkatrade.buyer.R;

/**
 * Created by PPC15 on 12-05-2017.
 */

public enum SharedPreferenceConstants {

    USER_ID("USER_ID"),
    FIRST_NAME("FIRST_NAME"),
    LAST_NAME("LAST_NAME"),
    USER_NAME("USER_NAME"),
    ORDER_QUANTITY("ORDER_QUANTITY"),
    CURRENT_STATE_NAME("CURRENT_STATE_NAME"),
    CURRENT_LATTITUDE("CURRENT_LATTITUDE"),
    CURRENT_LONGITUDE("CURRENT_LONGITUDE"),
    FATHER_NAME("FATHER_NAME"),
    REFERENCE_NO("REFERENCE_NO"),
    PINCODE("PINCODE"),
    BRANCH_CODE("BRANCH_CODE"),
    BRANCH_NAME("BRANCH_NAME"),
    IFSC_CODE("IFSC_CODE"),
    MICR_CODE("MICR_CODE"),
    PROFILE_PIC("PROFILE_PIC"),
    PROFILE_VIDEO("PROFILE_VIDEO"),
    PROFILE_VIDEO_THUMBNAIL("PROFILE_VIDEO_THUMBNAIL"),
    REGISTERED_MOBILE("REGISTERED_MOBILE"),
    ACCOUNT_NO("ACCOUNT_NO"),
    USER_TYPE("USER_TYPE"),
    USER_TYPE_BUYER("1"),
    USER_TYPE_SELLER("2"),
    USER_TYPE_ASSOCIATE("3"),
    LANGUAGE("LANGUAGE"),
    EMAIL_ID("EMAIL_ID"),
    IS_FIRST_TIME("IS_FIRST_TIME"),
    MOBILE("MOBILE"),
    COUNTRY_ID("COUNTRY_ID"),
    STATE_ID("STATE_ID"),
    CITY_ID("CITY_ID"),
    ADDRESS("ADDRESS"),
    DEVICE_ID("DEVICE_ID"),
    UPDATED_AT("UPDATED_AT"),
    STATUS("STATUS"),
    ORDER_LIST_COUNT("ORDER_LIST_COUNT"),
    CREATED_AT("CREATED_AT"),
    EMAIL_SMALL("email"),
    PASSWORD_SMALL("password"),
    CART_COUNT("CART_COUNT"),
    FIREBASE_REG_ID("FIREBASE_REG_ID"),
    CLIENT_ID("CLIENT_ID"),
    OTP_ID("OTP_ID"),
    TEMP_USER_ID("TEMP_USER_ID"),
    SHIPPING_ADDRESS_NAME("SHIPPING_ADDRESS_NAME"),
    SHIPPING_ADDRESS_PHONE("SHIPPING_ADDRESS_PHONE"),
    SHIPPING_ADDRESS_STATE("SHIPPING_ADDRESS_STATE"),
    SHIPPING_ADDRESS_CITY("SHIPPING_ADDRESS_CITY"),
    SHIPPING_ADDRESS_LANDMARK("SHIPPING_ADDRESS_LANDMARK"),
    SHIPPING_ADDRESS_PINCODE("SHIPPING_ADDRESS_PINCODE"),
    SHIPPING_ADDRESS("SHIPPING_ADDRESS"),
    SHOP_LIST_COUNT("0"),
    ENQUIRY_LIST_COUNT("0");




    private final String text;

    SharedPreferenceConstants(final String text) {
        this.text = text;
    }

    public String toString() {
        return this.text;
    }
}
