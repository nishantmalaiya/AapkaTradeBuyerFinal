package com.aapkatrade.buyer.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by PPC17 on 23-May-17.
 */

public class Animations {


    public void rotateAnimationStart(View v) {
        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());
        v.startAnimation(rotate);


    }

    public void rotateAnimationCancel(View v) {
        v.clearAnimation();

        v.animate().cancel();

    }


}
