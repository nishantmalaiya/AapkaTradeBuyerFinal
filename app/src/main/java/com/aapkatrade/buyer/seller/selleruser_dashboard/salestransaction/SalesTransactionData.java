package com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction;

/**
 * Created by PPC17 on 13-Jun-17.
 */

public class SalesTransactionData {


    public String paymentMode,paymentDate,RequestRefNo,BankRefNo,paymentStatus,paymentAmount;

    public SalesTransactionData(String paymentMode, String paymentDate, String requestRefNo, String bankRefNo, String paymentStatus, String paymentAmount) {
        this.paymentMode = paymentMode;
        this.paymentDate = paymentDate;
        RequestRefNo = requestRefNo;
        BankRefNo = bankRefNo;
        this.paymentStatus = paymentStatus;
        this.paymentAmount = paymentAmount;
    }

   /* public String MachineNo, TxnAmount, SalesAmount, ToDate, FromDate, TransationStatus, TotalTxnAmount, TotalSalesAmount;
    public SalesTransactionData(String machineNo, String txnAmount, String salesAmount, String toDate, String fromDate, String transationStatus, String totalTxnAmount, String totalSalesAmount) {
        MachineNo = machineNo;
        TxnAmount = txnAmount;
        SalesAmount = salesAmount;
        ToDate = toDate;
        FromDate = fromDate;
        TransationStatus = transationStatus;
        TotalTxnAmount = totalTxnAmount;
        TotalSalesAmount = totalSalesAmount;
    }*/


}
