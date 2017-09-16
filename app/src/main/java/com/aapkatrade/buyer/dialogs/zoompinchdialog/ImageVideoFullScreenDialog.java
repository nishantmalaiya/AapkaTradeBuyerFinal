package com.aapkatrade.buyer.dialogs.zoompinchdialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.general.Utils.AndroidUtils;
import com.aapkatrade.buyer.general.Validation;
import com.aapkatrade.buyer.home.HomeActivity;
import com.aapkatrade.buyer.seller.selleruser_dashboard.companyshopmgt.editcompanyshop.EditCompanyShopActivity;
import com.aapkatrade.buyer.uicomponent.customimageview.ZoomableImageView;
import com.afollestad.materialcamera.internal.VideoStreamView;
import com.koushikdutta.ion.Ion;

/**
 * Created by PPC15 on 9/15/2017.
 */

public class ImageVideoFullScreenDialog extends Fragment {
    private Context context;
    private String url;
    private boolean isVideo;
    private ZoomableImageView imageView;
    private VideoStreamView videoView;

    public ImageVideoFullScreenDialog(Context context, String url, boolean isVideo) {
        this.context = context;
        this.url = url;
        this.isVideo = isVideo;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fullscreen_image_video, container, false);
        if(getActivity() instanceof EditCompanyShopActivity){
            ((EditCompanyShopActivity) getActivity()).isFragment = true;
        }
//        setUpToolBar(view);
        initView(view);
        if(isVideo){
            imageView.setVisibility(View.GONE);

        }else {
            videoView.setVisibility(View.GONE);
            if(Validation.isNonEmptyStr(url)){
                Ion.with(imageView)
                        .error(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_applogo1))
                        .load(url);

            }
        }
        return view;
    }

    private void initView(View view) {
        imageView = view.findViewById(R.id.imageView);
        videoView = view.findViewById(R.id.videoView);
    }


    private void setUpToolBar(View view) {
        AppCompatImageView back_imagview = view.findViewById(R.id.back_imagview);
        AppCompatImageView homeIcon = view.findViewById(R.id.logoWord);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        back_imagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

    }


}
