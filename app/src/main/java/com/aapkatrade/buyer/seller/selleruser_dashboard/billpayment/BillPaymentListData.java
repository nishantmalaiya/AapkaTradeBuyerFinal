package com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment;

/**
 * Created by PPC16 on 16-Jan-17.
 */

public class BillPaymentListData {

    public String machineType, machineNo, machineCost;

    boolean selected;
    int background_color;

    public BillPaymentListData(String machineType, String machineNo, String machineCost, boolean selected, int background_color) {
        this.machineType = machineType;
        this.machineNo = machineNo;
        this.machineCost = machineCost;
        this.background_color = background_color;
        this.selected = selected;

    }
}

