package com.aapkatrade.buyer.uicomponent.customimageview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

public class MyImageView extends ImageView {
    private boolean a;

    public void setFillSpace(boolean z) {
        this.a = z;
    }

    public MyImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.a = false;
    }

    public MyImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MyImageView(Context context) {
        this(context, null);
    }

    protected void onMeasure(int i, int i2) {
        if (!this.a || getDrawable() == null) {
            super.onMeasure(i, i2);
            return;
        }
        int size = MeasureSpec.getSize(i);
        setMeasuredDimension(size, size);
    }
}
