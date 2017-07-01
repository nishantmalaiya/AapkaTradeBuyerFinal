package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductMediaData)) return false;

        ProductMediaData that = (ProductMediaData) o;

        if (isVideo != that.isVideo) return false;
        return false;

    }

    @Override
    public int hashCode() {
        int result = imagePath != null ? imagePath.hashCode() : 0;
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (videoThumbnail != null ? videoThumbnail.hashCode() : 0);
        result = 31 * result + (videoFile != null ? videoFile.hashCode() : 0);
        result = 31 * result + (isVideo ? 1 : 0);
        return result;
    }
}
