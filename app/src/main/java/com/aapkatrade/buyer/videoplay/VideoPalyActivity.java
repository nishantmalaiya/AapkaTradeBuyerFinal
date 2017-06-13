package com.aapkatrade.buyer.videoplay;

import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import com.aapkatrade.buyer.R;
import com.github.rtoshiro.view.video.FullscreenVideoLayout;

import java.io.IOException;

public class VideoPalyActivity extends AppCompatActivity {

    VideoView videoView;
    FullscreenVideoLayout videoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_paly);

        Bundle bundle = getIntent().getExtras();
        String video_url = bundle.getString("video_url");

        System.out.println("video_url--------------"+video_url);

        videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
        videoLayout.setActivity(this);

        Uri videoUri = Uri.parse(video_url);
        try {
            videoLayout.setVideoURI(videoUri);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
