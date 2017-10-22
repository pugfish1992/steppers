package com.f_candy_d.verticalsteppers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.transition.AutoTransition;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 10/19/17.
 */

public class VerticalStepperListView extends RecyclerView {

    private final View.OnTouchListener TOUCH_EATER = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    };

    public VerticalStepperListView(Context context) {
        super(context);
    }

    public VerticalStepperListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalStepperListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Call this method just before we change view's status that TransitionManager can handle.
     * See more -> https://gist.github.com/yatatsu/6a60e1a0c384bb91ddec68951341f561
     */
    public void beginPartialItemTransition(@NonNull Transition transition) {
        final ItemAnimator attachedItemAnimator = getItemAnimator();
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(@NonNull Transition transition) {
                setOnTouchListener(TOUCH_EATER);
            }

            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                VerticalStepperListView.this.setItemAnimator(attachedItemAnimator);
                setOnTouchListener(null);
            }

            @Override
            public void onTransitionCancel(@NonNull Transition transition) {}

            @Override
            public void onTransitionPause(@NonNull Transition transition) {}

            @Override
            public void onTransitionResume(@NonNull Transition transition) {}
        });

        TransitionManager.beginDelayedTransition(this, transition);
        setItemAnimator(null);
    }

    public void beginPartialItemTransition() {
        beginPartialItemTransition(new AutoTransition());
    }
}
