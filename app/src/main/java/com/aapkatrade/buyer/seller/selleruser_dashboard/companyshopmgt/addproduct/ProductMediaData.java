package com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addproduct;

import com.aapkatrade.buyer.general.Validation;

import java.io.File;

/**
 * Created by PPC16 on 21-Feb-17.
 */

public class ProductMediaData {
    public String imagePath, imageUrl, videoThumbnail;
    public File videoFile;
    public boolean isVideo = false;

    public ProductMediaData(String imagePath, String imageUrl, File videoFile, String videoThumbnail) {
        this.imagePath = imagePath;
        this.imageUrl = imageUrl;
        this.videoFile = videoFile;
        this.videoThumbnail = videoThumbnail;
        if (videoFile!=null&& Validation.isNonEmptyStr(videoThumbnail)) {
            isVideo = true;
        }
    }


}
