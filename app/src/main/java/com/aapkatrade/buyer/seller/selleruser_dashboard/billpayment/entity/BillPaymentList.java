package com.aapkatrade.buyer.seller.selleruser_dashboard.billpayment.entity;

/**
 * Created by PPC16 on 16-Jan-17.
 */

public class BillPaymentList {

    private String machineNo;
    private String machineType;
    private String machineCost;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    private String fromDate;
    private String toDate;
    private boolean isSelected;
    private int backgroundColor;

    public BillPaymentList(String machineNo, String machineType, String machineCost, boolean isSelected, int backgroundColor,String fromDate,String toDate) {
        this.machineNo = machineNo;
        this.machineType = machineType;
        this.machineCost = machineCost;
        this.isSelected = isSelected;
        this.backgroundColor = backgroundColor;
        this.fromDate=fromDate;
        this.toDate=toDate;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getMachineCost() {
        return machineCost;
    }

    public void setMachineCost(String machineCost) {
        this.machineCost = machineCost;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public String toString() {
        return "BillPaymentList{" +
                "machineNo='" + machineNo + '\'' +
                ", machineType='" + machineType + '\'' +
                ", machineCost='" + machineCost + '\'' +
                ", isSelected=" + isSelected +
                ", backgroundColor=" + backgroundColor +
                '}';
    }
}

