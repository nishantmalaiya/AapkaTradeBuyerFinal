package com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment;

import java.util.ArrayList;

/**
 * Created by PPC17 on 15-Jul-17.
 */

public class MachineTotalBillDatas {

    String  TotalAmount;
    ArrayList<String> MachineNo;

    public MachineTotalBillDatas(String totalAmount, ArrayList<String> machineNo) {
        TotalAmount = totalAmount;
        MachineNo = machineNo;
    }
}
