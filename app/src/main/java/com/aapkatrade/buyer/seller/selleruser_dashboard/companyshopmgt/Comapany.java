package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt;

/**
 * Created by PPC16 on 6/20/2017.
 */

public class Comapany {
    public String companyId;
    public String companyName;
    public boolean isChecked;

    public Comapany(String companyId, String companyName) {
        this.companyId = companyId;
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyId='" + companyId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }

    public Comapany(String companyId, String companyName, boolean isChecked) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.isChecked = isChecked;
    }
}
