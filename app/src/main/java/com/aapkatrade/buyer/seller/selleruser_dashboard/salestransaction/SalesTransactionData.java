package com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction;

/**
 * Created by PPC17 on 13-Jun-17.
 */

public class SalesTransactionData {


    String paymentMode,paymentDate,RequestRefNo,BankRefNo,paymentStatus,paymentAmount;

    public SalesTransactionData(String paymentMode, String paymentDate, String requestRefNo, String bankRefNo, String paymentStatus, String paymentAmount) {
        this.paymentMode = paymentMode;
        this.paymentDate = paymentDate;
        RequestRefNo = requestRefNo;
        BankRefNo = bankRefNo;
        this.paymentStatus = paymentStatus;
        this.paymentAmount = paymentAmount;
    }
}
