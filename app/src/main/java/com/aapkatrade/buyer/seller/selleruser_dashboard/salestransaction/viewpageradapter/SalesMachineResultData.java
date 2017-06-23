package com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction.viewpageradapter;

/**
 * Created by PPC17 on 20-Jun-17.
 */

public class SalesMachineResultData {

    String MachineNo,TxnAmount,SalesAmount,ToDate,FromDate,TransationStatus;

    public SalesMachineResultData(String machineNo, String txnAmount, String salesAmount, String toDate, String fromDate, String transationStatus) {
        MachineNo = machineNo;
        TxnAmount = txnAmount;
        SalesAmount = salesAmount;
        ToDate = toDate;
        FromDate = fromDate;
        TransationStatus = transationStatus;
    }
}
