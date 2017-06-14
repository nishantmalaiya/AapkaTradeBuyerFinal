package com.aapkatrade.buyer.home.navigation.entity;

import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;

/**
 * Created by PPC09 on 20-Jan-17.
 */

public class CategoryHome {
    private String categoryId;
    private String categoryName;
    private String categoryIconName;
    private String categoryIconPath;
    private String basePath = AndroidUtils.BaseUrl + "/public/appicon/";
    private String iconExtention = ".png";

    public CategoryHome(String categoryId, String categoryName, String categoryIconName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryIconName = categoryIconName;
        setCategoryIconPath(createIconPath(categoryName));
    }

    private String createIconPath(String iconName) {
        return Validation.isNonEmptyStr(iconName) ? basePath + iconName.replaceAll(" |/|&", "") + iconExtention : "";
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


    @Override
    public String toString() {
        return "CategoryHome{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryIconName='" + categoryIconName + '\'' +
                ", categoryIconPath='" + categoryIconPath + '\'' +
                ", basePath='" + basePath + '\'' +
                ", iconExtention='" + iconExtention + '\'' +
                '}';
    }

}
