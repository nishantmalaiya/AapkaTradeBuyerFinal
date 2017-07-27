package com.aapkatrade.buyer.general.progressbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.aapkatrade.buyer.R;
import com.aapkatrade.buyer.animation.ProgressBarAnimation;

/**
 * Created by PPC21 on 01-Feb-17.
 */

public class ProgressBarHandler {
    private Dialog mDialog;
    private View mouse;

    public ProgressBarHandler(Context context) {
        if (mDialog == null) {
            mDialog = new Dialog(context, R.style.progressDialogStyle);
            //noinspection ConstantConditions
            mDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

            mDialog.setContentView(R.layout.progessdialoglayout);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.getWindow().setGravity(Gravity.CENTER);


            View view = mDialog.getWindow().getDecorView();

            mouse = view.findViewById(R.id.mouse);


            hide();
        }
    }

    public void show() {
        if (mDialog != null) {
            mDialog.show();
        }
        Animation operatingAnim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        operatingAnim.setRepeatCount(Animation.INFINITE);
        operatingAnim.setDuration(2000);
        operatingAnim.setInterpolator(new LinearInterpolator());
        mouse.setAnimation(operatingAnim);
    }

    public void hide() {
        if (mDialog != null) {
            mDialog.hide();
        }
        mouse.clearAnimation();
    }


}
