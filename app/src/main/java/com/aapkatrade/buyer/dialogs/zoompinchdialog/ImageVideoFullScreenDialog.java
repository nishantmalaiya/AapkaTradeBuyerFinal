package com.aapkatrade.buyer.dialogs.zoompinchdialog;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.general.progressbar.ProgressBarHandler;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.addcompanyshop.AddCompanyShopActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.editcompanyshop.EditCompanyShopActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.addproduct.AddProductActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.productmanagement.editproduct.EditProductActivity;
import com.aapkatrade.buyer.uicomponent.customimageview.ZoomableImageView;
import com.koushikdutta.ion.Ion;

/**
 * Created by PPC15 on 9/15/2017.
 */

public class ImageVideoFullScreenDialog extends Fragment {
    private Context context;
    private ProgressBarHandler progressBarHandler;
    private String url;
    private boolean isVideo;
    private ZoomableImageView imageView;
    private VideoView videoView;
    private Uri uri;
    private MediaController mediacontroller;


    public ImageVideoFullScreenDialog(Context context, String url, boolean isVideo) {
        this.context = context;
        this.url = url;
        this.isVideo = isVideo;
        progressBarHandler = new ProgressBarHandler(this.context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fullscreen_image_video, container, false);
        if(getActivity() instanceof EditCompanyShopActivity){
            ((EditCompanyShopActivity) getActivity()).isFragment = true;
        } else if(getActivity() instanceof EditProductActivity){
            ((EditProductActivity) getActivity()).isFragment = true;
        } else if(getActivity() instanceof AddCompanyShopActivity){
            ((AddCompanyShopActivity) getActivity()).isFragment = true;
        } else if(getActivity() instanceof AddProductActivity){
            ((AddProductActivity) getActivity()).isFragment = true;
        }
        AndroidUtils.showErrorLog(context, url);
        initView(view);
       execute();
        return view;
    }

    private void execute() {
        if(isVideo) {
            processVideo();


        }else {
            processImage();


        }
    }

    private void processVideo() {
        imageView.setVisibility(View.GONE);


        progressBarHandler.show();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(context);
            mediacontroller.setAnchorView(videoView);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(url);
            videoView.setMediaController(mediacontroller);
            videoView.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                progressBarHandler.hide();
                videoView.start();
            }
        });


/*
        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(context);
            mediacontroller.setAnchorView(videoView);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(url);
            videoView.setMediaController(mediacontroller);
            videoView.setVideoURI(video);
            videoView.start();

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {

                videoView.start();
            }
        });*/
    }

    private void  processImage() {
        videoView.setVisibility(View.GONE);
        if(Validation.isNonEmptyStr(url)){
            Ion.with(imageView)
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                    .load(url);
        }
    }

    private void initView(View view) {
        imageView = view.findViewById(R.id.imageView);
        videoView = view.findViewById(R.id.videoView);
    }



}
