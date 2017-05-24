package com.aapkatrade.buyer.welcome;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aapkatrade.buyer.Home.HomeActivity;
import com.aapkatrade.buyer.R;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;


public class WelcomeActivity extends AppCompatActivity {

    private viewpageradapter_welcome viewpageradapter;
    ViewPager vp;
    CircleIndicator circleIndicator;
    private int[] layouts = new int[]{
            R.layout.welcome_slide1,
            R.layout.welcome_slide1,
            R.layout.welcome_slide1,
            R.layout.welcome_slide1,
            R.layout.welcome_slide1};
    ;
    private Button btnSkip, btnNext;
    Context context;
    int imagepaths[] = new int[]{R.drawable.slide1image, R.drawable.slide2image, R.drawable.slide3image, R.drawable.slide4image, R.drawable.slide5image};


    String[] slider_header, slider_footer;
    ArrayList<GradientParameters> gradientParametersArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = this;
        vp = (ViewPager) findViewById(R.id.viewpagerWelcome);
        circleIndicator = (CircleIndicator) findViewById(R.id.indicator_welcome);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);


        slider_header = getResources().getStringArray(R.array.welcomeSliderHeaders);
        slider_footer = getResources().getStringArray(R.array.welcomeSliderFooter);


        gradientParametersArrayList.add(new GradientParameters(android.graphics.drawable.GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.Welcome_screen1_gradient_TopColor), ContextCompat.getColor(context, R.color.Welcome_screen1_gradient_BottomColor), 0, android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM));
        gradientParametersArrayList.add(new GradientParameters(android.graphics.drawable.GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.Welcome_screen2_gradient_TopColor), ContextCompat.getColor(context, R.color.Welcome_screen2_gradient_BottomColor), 0, android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM));
        gradientParametersArrayList.add(new GradientParameters(android.graphics.drawable.GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.Welcome_screen3_gradient_TopColor), ContextCompat.getColor(context, R.color.Welcome_screen3_gradient_BottomColor), 0, android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM));
        gradientParametersArrayList.add(new GradientParameters(android.graphics.drawable.GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.Welcome_screen4_gradient_TopColor), ContextCompat.getColor(context, R.color.Welcome_screen4_gradient_BottomColor), 0, android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM));
        gradientParametersArrayList.add(new GradientParameters(android.graphics.drawable.GradientDrawable.RECTANGLE, ContextCompat.getColor(context, R.color.Welcome_screen5_gradient_TopColor), ContextCompat.getColor(context, R.color.Welcome_screen5_gradient_BottomColor), 0, android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM));


        setupviewpager();
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    vp.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });

    }

    private int getItem(int i) {
        return vp.getCurrentItem() + i;
    }


    private void launchHomeScreen() {

        Intent i = new Intent(WelcomeActivity.this, HomeActivity.class);
        startActivity(i);
        finish();

    }


    private void setupviewpager() {

        viewpageradapter = new viewpageradapter_welcome(WelcomeActivity.this, layouts, imagepaths, slider_header, slider_footer, gradientParametersArrayList);
        vp.setAdapter(viewpageradapter);
        vp.setCurrentItem(0);

        circleIndicator.setViewPager(vp);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == layouts.length - 1) {
                    // last page. make button text to GOT IT
                    btnNext.setText(getString(R.string.start));
                    btnSkip.setVisibility(View.GONE);
                } else {
                    // still pages are left
                    btnNext.setText(getString(R.string.next));
                    btnSkip.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


}
