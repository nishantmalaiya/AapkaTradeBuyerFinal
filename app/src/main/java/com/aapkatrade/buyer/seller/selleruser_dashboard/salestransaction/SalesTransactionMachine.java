package com.aapkatrade.buyer.seller.selleruser_dashboard.salestransaction;

/**
 * Created by PPC17 on 21-Jun-17.
 */

public class SalesTransactionMachine {

    public String TxnId, TxnAmount, SalesAmount, paymentStatus, TxnDate, benificalPayoutDate;

    public SalesTransactionMachine(String TxnId, String TxnDate, String TxnAmount, String SalesAmount, String paymentStatus, String benificalPayoutDate) {
        this.TxnId = TxnId;
        this.TxnAmount = TxnAmount;
        this.SalesAmount = SalesAmount;

        this.paymentStatus = paymentStatus;
        this.TxnDate = TxnDate;
        this.benificalPayoutDate = benificalPayoutDate;

    }


}
