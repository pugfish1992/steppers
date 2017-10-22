package com.f_candy_d.verticalsteppers.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.f_candy_d.verticalsteppers.R;

/**
 * Created by daichi on 10/18/17.
 */

abstract public class TwoFacedViewLayoutBase<T extends View> extends FrameLayout {

    private static final int VIEW_INDEX_FIRST = 0;
    private static final int VIEW_INDEX_SECOND = 1;
    // This variable would be the one of VIEW_INDEX_FIRST, VIEW_INDEX_SECOND
    private int mCurrentViewIndex = VIEW_INDEX_FIRST;

    private T mFirstView;
    private T mSecondView;
    private int mFirstViewId = VIEW_INDEX_FIRST;
    private int mSecondViewId = VIEW_INDEX_SECOND;

    public TwoFacedViewLayoutBase(Context context) {
        super(context);
        init(null, 0);
    }

    public TwoFacedViewLayoutBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TwoFacedViewLayoutBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TwoFacedViewLayoutBase, defStyle, 0);

        int resId = a.getResourceId(R.styleable.TwoFacedViewLayoutBase_firstViewLayout, 0);
        if (resId != 0) {
            setFirstView((T) inflate(getContext(), resId, null));
        }

        resId = a.getResourceId(R.styleable.TwoFacedViewLayoutBase_secondViewLayout, 0);
        if (resId != 0) {
            setSecondView((T) inflate(getContext(), resId, null));
        }

        boolean showSecondViewByDefault = a.getBoolean(R.styleable.TwoFacedViewLayoutBase_showSecondViewByDefault, false);
        if (showSecondViewByDefault) {
            setCurrentView(mSecondViewId);
        } else {
            setCurrentView(mFirstViewId);
        }

        a.recycle();
    }

    public T getViewById(int viewId) {
        if (viewId == mFirstViewId) {
            return mFirstView;
        } else if (viewId == mSecondViewId) {
            return mSecondView;
        } else {
            // Should not reach here...
            throw new IllegalArgumentException();
        }
    }

    public T getFirstView() {
        return mFirstView;
    }

    public void setFirstView(@LayoutRes int layoutId) {
        setFirstView((T) inflate(getContext(), layoutId, null));
    }

    public void setFirstView(T firstView) {
        if (mFirstView != null) {
            this.removeView(mFirstView);
        }
        mFirstView = firstView;
        if (firstView != null) {
            this.addView(firstView);
        }
        invalidateState();
    }

    public T getSecondView() {
        return mSecondView;
    }

    public void setSecondView(@LayoutRes int layoutId) {
        setSecondView((T) inflate(getContext(), layoutId, null));
    }

    public void setSecondView(T secondView) {
        if (mSecondView != null) {
            this.removeView(mSecondView);
        }
        mSecondView = secondView;
        if (secondView != null) {
            this.addView(secondView);
        }
        invalidateState();
    }

    public void setFirstViewId(int firstViewId) {
        mFirstViewId = firstViewId;
    }

    public void setSecondViewId(int secondViewId) {
        mSecondViewId = secondViewId;
    }

    public void setCurrentView(int viewId) {
        mCurrentViewIndex = (viewId == mFirstViewId) ? VIEW_INDEX_FIRST : VIEW_INDEX_SECOND;
        int firstVisibility = (viewId == mFirstViewId) ? VISIBLE : GONE;
        int secondVisibility = (viewId == mSecondViewId) ? VISIBLE : GONE;

        if (mFirstView != null) {
            mFirstView.setVisibility(firstVisibility);
        }
        if (mSecondView != null) {
            mSecondView.setVisibility(secondVisibility);
        }
    }

    public int getCurrentViewId() {
        return (mCurrentViewIndex == VIEW_INDEX_FIRST) ? mFirstViewId : mSecondViewId;
    }

    private void invalidateState() {
        if (mCurrentViewIndex == VIEW_INDEX_FIRST) {
            setCurrentView(mFirstViewId);
        } else {
            setCurrentView(mSecondViewId);
        }
    }
}
