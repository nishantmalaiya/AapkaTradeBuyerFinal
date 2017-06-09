package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt;

/**
 * Created by PPC15 on 09-06-2017.
 */

public class CompanyShopData {
    private String companyId;
    private String name;
    private String address;
    private String description;
    private String created;

    public CompanyShopData(String companyId, String name, String address, String description, String created) {
        this.companyId = companyId;
        this.name = name;
        this.address = address;
        this.description = description;
        this.created = created;
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

    @Override
    public String toString() {
        return "CompanyShopData{" +
                "companyId='" + companyId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", created='" + created + '\'' +
                '}';
    }
}
