package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct;

/**
 * Created by PPC17 on 10-Jul-17.
 */

public class CompanyDropdownDatas {

   public  String comapanyId,companyImageUrl,companyName,comapanyCategory;

    public CompanyDropdownDatas(String comapanyId, String companyImageUrl, String companyName, String comapanyCategory) {
        this.comapanyId = comapanyId;
        this.companyImageUrl = companyImageUrl;
        this.companyName = companyName;
        this.comapanyCategory = comapanyCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyDropdownDatas)) return false;

        CompanyDropdownDatas that = (CompanyDropdownDatas) o;

        if (!comapanyId.equals(that.comapanyId)) return false;
        if (!companyImageUrl.equals(that.companyImageUrl)) return false;
        if (!companyName.equals(that.companyName)) return false;
        return comapanyCategory.equals(that.comapanyCategory);

    }

    @Override
    public int hashCode() {
        int result = comapanyId.hashCode();
        result = 31 * result + companyImageUrl.hashCode();
        result = 31 * result + companyName.hashCode();
        result = 31 * result + comapanyCategory.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CompanyDropdownDatas{" +
                "comapanyId='" + comapanyId + '\'' +
                ", companyImageUrl='" + companyImageUrl + '\'' +
                ", companyName='" + companyName + '\'' +
                ", comapanyCategory='" + comapanyCategory + '\'' +
                '}';
    }
}





