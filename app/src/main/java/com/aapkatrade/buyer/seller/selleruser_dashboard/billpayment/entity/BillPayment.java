package com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment.entity;

import java.util.ArrayList;

/**
 * Created by PPC17 on 15-Jul-17.
 */

public class BillPayment {

    private String  TotalAmount;
    private String MachineNo;

    public BillPayment(String totalAmount, String machineNo) {
        TotalAmount = totalAmount;
        MachineNo = machineNo;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getMachineNo() {
        return MachineNo;
    }

    public void setMachineNo(String machineNo) {
        MachineNo = machineNo;
    }

    @Override
    public String toString() {
        return "BillPayment{" +
                "TotalAmount='" + TotalAmount + '\'' +
                ", MachineNo='" + MachineNo + '\'' +
                '}';
    }
}
