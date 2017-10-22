package com.f_candy_d.verticalsteppers.component;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.StyleRes;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by daichi on 10/19/17.
 */

public class TwoFacedTextViewLayout extends TwoFacedViewLayoutBase<TextView> {

    public TwoFacedTextViewLayout(Context context) {
        super(context);
    }

    public TwoFacedTextViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoFacedTextViewLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * SAME ATTRIBUTES
     * ----------------------------------------------------------------------------- */

    public void setText(CharSequence text) {
        TextView textView = getFirstView();
        if (textView != null) {
            getFirstView().setText(text);
        }
        
        textView = getSecondView();
        if (textView != null) {
            getSecondView().setText(text);
        }
    }

    public String getText() {
        TextView textView = getFirstView();
        if (textView != null) {
            return textView.getText().toString();
        }

        textView = getSecondView();
        if (textView != null) {
            return textView.getText().toString();
        }

        return null;
    }

    public void setTextSize(float size) {
        TextView textView = getFirstView();
        if (textView != null) {
            getFirstView().setTextSize(size);
        }

        textView = getSecondView();
        if (textView != null) {
            getSecondView().setTextSize(size);
        }
    }

    /**
     * ATTRIBUTES THAT CAN BE DIFFERENT
     * ----------------------------------------------------------------------------- */

    public void setTextColorTo(@ColorInt int color, int viewId) {
        TextView textView = getViewById(viewId);
        if (textView != null) {
            textView.setTextColor(color);
        }
    }

    public void setTextAppearanceTo(@StyleRes int resId, int viewId) {
        TextView textView = getViewById(viewId);
        if (textView != null) {
            TextViewCompat.setTextAppearance(textView, resId);
        }
    }
}
