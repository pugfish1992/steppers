package com.f_candy_d.verticalsteppers.component;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by daichi on 10/19/17.
 */

public class TwoFacedImageViewLayout extends TwoFacedViewLayoutBase<ImageView> {

    public TwoFacedImageViewLayout(Context context) {
        super(context);
    }

    public TwoFacedImageViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoFacedImageViewLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * ATTRIBUTES THAT CAN BE DIFFERENT
     * ----------------------------------------------------------------------------- */

    public void setImageResourceTo(@DrawableRes int resId, int viewId) {
        ImageView imageView = getViewById(viewId);
        if (imageView != null) {
            imageView.setImageResource(resId);
        }
    }

    public void setTintTo(@ColorInt int color, int viewId) {
        ImageView imageView = getViewById(viewId);
        if (imageView != null) {
            imageView.setColorFilter(color);
        }
    }
}
