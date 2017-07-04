package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.entity;

import java.util.ArrayList;

/**
 * Created by PPC15 on 03-07-2017.
 */

public class DynamicFormEntity {
    private String name, label, type;
    private boolean isMultiple;
    private ArrayList<FormValue> formValueArrayList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }

    public ArrayList<FormValue> getFormValueArrayList() {
        return formValueArrayList;
    }

    public void addToFormValueArrayList(FormValue formValue) {
        this.formValueArrayList.add(formValue);
    }

    public void addToFormValueArrayList(int position, FormValue formValue) {
        if(position >= 0){
            this.formValueArrayList.add(position, formValue);
        }
    }

    @Override
    public String toString() {
        return "DynamicFormEntity{" +
                "name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", type='" + type + '\'' +
                ", isMultiple=" + isMultiple +
                ", formValueArrayList=" + formValueArrayList +
                '}';
    }
}
