package com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct;

import com.aapkatrade.buyer.general.Validation;

import java.io.File;

/**
 * Created by PPC16 on 21-Feb-17.
 */

public class ProductMediaData {
    public String id;
    public String imagePath, imageUrl, videoThumbnail;
    public File videoFile;
    public boolean isVideo = false;

    public ProductMediaData(String imagePath, String imageUrl, File videoFile, String videoThumbnail) {
        this.imagePath = imagePath;
        this.imageUrl = imageUrl;
        this.videoFile = videoFile;
        this.videoThumbnail = videoThumbnail;
        if (videoFile != null && Validation.isNonEmptyStr(videoThumbnail)) {
            isVideo = true;
        }
    }

    public ProductMediaData() {
    }

    public ProductMediaData(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
        this.isVideo = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public File getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(File videoFile) {
        this.videoFile = videoFile;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductMediaData)) return false;

        ProductMediaData that = (ProductMediaData) o;

        return isVideo == that.isVideo && (id != null ? id.equals(that.id) : that.id == null && (imagePath != null ? imagePath.equals(that.imagePath) : that.imagePath == null && (imageUrl != null ? imageUrl.equals(that.imageUrl) : that.imageUrl == null && (videoThumbnail != null ? videoThumbnail.equals(that.videoThumbnail) : that.videoThumbnail == null && (videoFile != null ? videoFile.equals(that.videoFile) : that.videoFile == null)))));

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (imagePath != null ? imagePath.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (videoThumbnail != null ? videoThumbnail.hashCode() : 0);
        result = 31 * result + (videoFile != null ? videoFile.hashCode() : 0);
        result = 31 * result + (isVideo ? 1 : 0);
        return result;
    }
}
