package com.aapkatrade.buyer.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

import com.aapkatrade.buyer.general.Utils.AndroidUtils;

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


    public static void expand(final View v, int duration) {
        if (v != null)

        {
            v.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setDuration(duration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            v.setVisibility(View.VISIBLE);
                        }


                    });

        }


        Log.e("Animation_error_collap", "view null");

    }

    public static void collapse(final View v, int duration) {

        if (v != null)

        {
            v.animate()
                    .translationY(v.getHeight())
                    .alpha(0.0f)
                    .setDuration(duration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            v.setVisibility(View.GONE);
                        }


                    });
        }
        Log.e("Animation_error_extend", "view null");


    }


}
