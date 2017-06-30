package com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction.viewpageradapter;

import com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction.SalesTransactionData;

import java.util.ArrayList;

/**
 * Created by PPC17 on 20-Jun-17.
 */

public class SalesMachineResultData {

    String MachineNo, TxnAmount, SalesAmount, ToDate, FromDate, TransationStatus, TotalTxnAmount, TotalSalesAmount;
    ArrayList<SalesTransactionData> salesMachineResultDatas;

    public SalesMachineResultData(String machineNo, String txnAmount, String salesAmount, String toDate, String fromDate, String transationStatus, String TotalTxnAmount, String TotalSalesAmount, ArrayList<SalesTransactionData> salesMachineResultDatas) {
        MachineNo = machineNo;
        TxnAmount = txnAmount;
        SalesAmount = salesAmount;
        ToDate = toDate;
        FromDate = fromDate;
        TransationStatus = transationStatus;
        this.TotalTxnAmount = TotalTxnAmount;
        this.TotalSalesAmount = TotalSalesAmount;
        this.salesMachineResultDatas=salesMachineResultDatas;
    }
}
