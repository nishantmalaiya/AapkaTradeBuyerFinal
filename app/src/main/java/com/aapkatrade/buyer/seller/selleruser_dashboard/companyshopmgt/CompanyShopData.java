package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt;

import java.util.List;

/**
 * Created by PPC15 on 09-06-2017.
 */

public class CompanyShopData
{
    private String companyId;
    private String name;
    private String address;
    private String description;
    private String created;
    private String productCount;
    private String company_image;

    public CompanyShopData(String companyId, String name, String address, String description, String created, String productCount,String company_image)
    {
        this.companyId = companyId;
        this.name = name;
        this.address = address;
        this.description = description;
        this.created = created;
        this.productCount = productCount;
        this.company_image = company_image;
    }


    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getProductCount() {
        return productCount;
    }

    public void  setCompany_image(String company_image){
        this.company_image =company_image;
    }

    public String getCompany_image() {
        return company_image;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof CompanyShopData && this.getName().equals(((CompanyShopData) obj).getName());
    }

    @Override
    public String toString() {
        return "CompanyShopData{" +
                "companyId='" + companyId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", created='" + created + '\'' +
                ", productCount='" + productCount + '\'' +
                ", company_image='" + company_image + '\'' +
                '}';
    }
}
