package com.aapkatrade.buyer.home.navigation.entity;


import android.support.v7.widget.CardView;

import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;

import java.util.ArrayList;

/**
 * Created by PPC09 on 20-Jan-17.
 */

public class Category
{
    private String categoryId;
    private String categoryName;
    private String categoryIconName;
    private String categoryIconPath;
    private ArrayList<SubCategory> subCategoryList;
    private String basePath = AndroidUtils.BaseUrl+"/public/appicon/";
    private String iconExtention = ".png";
    public Category(String categoryId, String categoryName, String categoryIconName, ArrayList<SubCategory> subCategoryList) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryIconName = categoryIconName;
        this.subCategoryList=subCategoryList;
        setCategoryIconPath(createIconPath(categoryName));
    }

    private String createIconPath(String iconName){
        return Validation.isNonEmptyStr(iconName)? basePath+iconName.replaceAll(" |/|&","")+iconExtention: "";
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryIconName() {
        return categoryIconName;
    }

    public String getCategoryIconPath() {
        return categoryIconPath;
    }

    public void setCategoryIconPath(String categoryIconPath) {
        this.categoryIconPath = categoryIconPath;
    }

    public ArrayList<SubCategory> getSubCategoryList() {
        return this.subCategoryList;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        else if(this.categoryName.equalsIgnoreCase(((Category) obj).getCategoryName())) return true;
        return false;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryIconName='" + categoryIconName + '\'' +
                ", categoryIconPath='" + categoryIconPath + '\'' +
                ", subCategoryList=" + subCategoryList +
                ", basePath='" + basePath + '\'' +
                ", iconExtention='" + iconExtention + '\'' +
                '}';
    }
}
