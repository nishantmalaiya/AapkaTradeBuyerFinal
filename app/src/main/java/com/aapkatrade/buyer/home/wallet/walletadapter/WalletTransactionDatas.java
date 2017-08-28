package com.aapkatrade.buyer.home.wallet.walletadapter;

/**
 * Created by PPC17 on 17-Aug-17.
 */

public class WalletTransactionDatas {

    String transaction_id,transaction_date,transaction_status,transaction_amount,transaction_type,transaction_image;

    public WalletTransactionDatas(String transaction_id, String transaction_date, String transaction_status, String transaction_amount, String transaction_type, String transaction_image)
    {
        this.transaction_id = transaction_id;
        this.transaction_date = transaction_date;
        this.transaction_status = transaction_status;
        this.transaction_amount = transaction_amount;
        this.transaction_type = transaction_type;
        this.transaction_image = transaction_image;
    }





}
