package com.f_candy_d.verticalsteppers;

/**
 * Created by daichi on 10/18/17.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.f_candy_d.verticalsteppers.component.TwoFacedImageViewLayout;
import com.f_candy_d.verticalsteppers.component.TwoFacedTextViewLayout;
import com.f_candy_d.verticalsteppers.component.TwoFacedViewLayout;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class VerticalStepper extends RelativeLayout {

    // Title
    private static final int VIEW_FACE_ACTIVE_TITLE = 0;
    private static final int VIEW_FACE_INACTIVE_TITLE = 1;
    private TwoFacedTextViewLayout mTitleView;

    // Sub Title
    private static final int VIEW_FACE_ACTIVE_SUB_TITLE = 0;
    private static final int VIEW_FACE_INACTIVE_SUB_TITLE = 1;
    private TwoFacedTextViewLayout mSubTitleView;

    // Circle
    private ViewGroup mCircleLayout;

    // Circle (bg)
    private static final int VIEW_FACE_CIRCLE_ACTIVE_BG = 0;
    private static final int VIEW_FACE_CIRCLE_INACTIVE_BG = 1;
    private TwoFacedImageViewLayout mCircleBgView;

    // Circle (text label & icon label)
    private static final int VIEW_FACE_CIRCLE_LABEL_TEXT = 0;
    private static final int VIEW_FACE_CIRCLE_LABEL_ICON = 1;
    private TwoFacedViewLayout mCircleLabelView;

    // Content View
    private FrameLayout mContentViewContainer;

    // Connection Line
    private Paint mConnectionLinePaint;
    private int mConnectionLineTopSpace;
    private int mConnectionLineBottomSpace;

    public VerticalStepper(Context context) {
        super(context);
        init(null, 0);
    }

    public VerticalStepper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public VerticalStepper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load the layout file & attributes
        inflate(getContext(), R.layout.vertical_stepper, this);
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.VerticalStepperView, defStyle, 0);

        // # Title

        mTitleView = findViewById(R.id.title);
        mTitleView.setFirstViewId(VIEW_FACE_ACTIVE_TITLE);
        mTitleView.setSecondViewId(VIEW_FACE_INACTIVE_TITLE);
        setTitle(a.getString(R.styleable.VerticalStepperView_title));
        setActiveTitleColor(a.getColor(R.styleable.VerticalStepperView_activeTitleColor,
                ContextCompat.getColor(getContext(), R.color.vertical_stepper_active_title)));
        setInactiveTitleColor(a.getColor(R.styleable.VerticalStepperView_inactiveTitleColor,
                ContextCompat.getColor(getContext(), R.color.vertical_stepper_inactive_title)));

        // # Sub Title

        mSubTitleView = findViewById(R.id.sub_title);
        mSubTitleView.setFirstViewId(VIEW_FACE_ACTIVE_SUB_TITLE);
        mSubTitleView.setSecondViewId(VIEW_FACE_INACTIVE_SUB_TITLE);
        setSubTitle(a.getString(R.styleable.VerticalStepperView_subTitle));
        setActiveSubTitleColor(a.getColor(R.styleable.VerticalStepperView_activeSubTitleColor,
                ContextCompat.getColor(getContext(), R.color.vertical_stepper_active_sub_title)));
        setInactiveSubTitleColor(a.getColor(R.styleable.VerticalStepperView_inactiveSubTitleColor,
                ContextCompat.getColor(getContext(), R.color.vertical_stepper_inactive_sub_title)));

        // # Circle (bg)

        mCircleBgView = findViewById(R.id.circle_bg);
        mCircleBgView.setFirstViewId(VIEW_FACE_CIRCLE_ACTIVE_BG);
        mCircleBgView.setSecondViewId(VIEW_FACE_CIRCLE_INACTIVE_BG);
        setActiveCircleBackgroundColor(a.getColor(R.styleable.VerticalStepperView_activeCircleBackgroundColor,
                ContextCompat.getColor(getContext(), R.color.vertical_stepper_active_circle_background)));
        setInactiveCircleBackgroundColor(a.getColor(R.styleable.VerticalStepperView_inactiveCircleBackgroundColor,
                ContextCompat.getColor(getContext(), R.color.vertical_stepper_inactive_circle_background)));

        // # Circle (text label & icon label)

        mCircleLabelView = findViewById(R.id.circle_label);
        mCircleLabelView.setFirstViewId(VIEW_FACE_CIRCLE_LABEL_TEXT);
        mCircleLabelView.setSecondViewId(VIEW_FACE_CIRCLE_LABEL_ICON);
        setCircleTextLabel(a.getString(R.styleable.VerticalStepperView_circleTextLabel));
        setCircleTextLabelTint(a.getColor(R.styleable.VerticalStepperView_circleTextLabelTint,
                ContextCompat.getColor(getContext(), R.color.vertical_stepper_circle_text_label_tint)));
        setCircleIconLabel(a.getResourceId(R.styleable.VerticalStepperView_circleIconLabel, R.drawable.ic_check));
        setCircleIconLabelTint(a.getColor(R.styleable.VerticalStepperView_circleIconLabelTint,
                ContextCompat.getColor(getContext(), R.color.vertical_stepper_circle_icon_label_tint)));

        // # Circle

        mCircleLayout = findViewById(R.id.circle_layout);
        switch (a.getInt(R.styleable.VerticalStepperView_circleSize, CIRCLE_SIZE_SMALL)) {
            case CIRCLE_SIZE_REGULAR: setCircleSize(CIRCLE_SIZE_REGULAR); break;
            case CIRCLE_SIZE_SMALL: setCircleSize(CIRCLE_SIZE_SMALL); break;
        }

        // # Content View

        mContentViewContainer = findViewById(R.id.content_view_container);
        addContentView(a.getResourceId(R.styleable.VerticalStepperView_contentViewLayout, 0));

        // # Connection Line

        mConnectionLineTopSpace = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_top_space);
        mConnectionLineBottomSpace = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_bottom_space);
        mConnectionLinePaint = new Paint();
        mConnectionLinePaint.setColor(a.getColor(R.styleable.VerticalStepperView_connectionLineColor,
                ContextCompat.getColor(getContext(), R.color.vertical_stepper_connection_line)));
        mConnectionLinePaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.vertical_stepper_connection_line_width));

        // # Default Step Mode

        if (a.getBoolean(R.styleable.VerticalStepperView_activateStepByDefault, false)) {
            activateStep();
        } else {
            inactivateStep();
        }

        if (a.getBoolean(R.styleable.VerticalStepperView_checkStepByDefault, false)) {
            checkStep();
        } else {
            uncheckStep();
        }

        a.recycle();

        // # Add ripple-effect to myself
        // # See -> https://stackoverflow.com/questions/37987732/programatically-set-selectableitembackground-on-android-view

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        this.setBackgroundResource(outValue.resourceId);
        this.setClickable(true);
    }

    /**
     * TITLE
     * ---------- */

    public void setTitle(String title) {
        mTitleView.setText(title);
        invalidateTitleVisibility();
    }

    public void setActiveTitleColor(@ColorInt int color) {
        mTitleView.setTextColorTo(color, VIEW_FACE_ACTIVE_TITLE);
    }

    public void setInactiveTitleColor(@ColorInt int color) {
        mTitleView.setTextColorTo(color, VIEW_FACE_INACTIVE_TITLE);
    }

    private void invalidateTitleVisibility() {
        String text = mTitleView.getText();
        if (text == null || text.length() == 0) {
            mTitleView.setVisibility(GONE);
        } else {
            mTitleView.setVisibility(VISIBLE);
        }
    }

    /**
     * SUB TITLE
     * ---------- */

    public void setSubTitle(String subTitle) {
        mSubTitleView.setText(subTitle);
        invalidateSubTitleVisibility();
    }

    public void setActiveSubTitleColor(@ColorInt int color) {
        mTitleView.setTextColorTo(color, VIEW_FACE_ACTIVE_SUB_TITLE);
    }

    public void setInactiveSubTitleColor(@ColorInt int color) {
        mTitleView.setTextColorTo(color, VIEW_FACE_INACTIVE_SUB_TITLE);
    }

    private void invalidateSubTitleVisibility() {
        String text = mSubTitleView.getText();
        if (text == null || text.length() == 0) {
            mSubTitleView.setVisibility(GONE);
        } else {
            mSubTitleView.setVisibility(VISIBLE);
        }
    }

    /**
     * CIRCLE
     * ---------- */

    /**
     * The following constants are defined in res/values/attrs.xml with enum tag
     */
    @Retention(SOURCE)
    @IntDef({CIRCLE_SIZE_SMALL, CIRCLE_SIZE_REGULAR})
    public @interface CircleSize {}
    public static final int CIRCLE_SIZE_SMALL = 0;
    public static final int CIRCLE_SIZE_REGULAR = 1;

    public void setCircleSize(@CircleSize int size) {
        ViewGroup.LayoutParams params = mCircleLayout.getLayoutParams();
        int textSize;
        int iconPadding;

        if (size == CIRCLE_SIZE_REGULAR) {
            params.width = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_circle_regular_size);
            params.height = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_circle_regular_size);
            textSize = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_circle_regular_text_size);
            iconPadding = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_circle_regular_icon_padding);

        } else if (size == CIRCLE_SIZE_SMALL) {
            params.width = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_circle_small_size);
            params.height = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_circle_small_size);
            textSize = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_circle_small_text_size);
            iconPadding = getResources().getDimensionPixelSize(R.dimen.vertical_stepper_circle_small_icon_padding);

        } else {
            throw new IllegalArgumentException();
        }

        mCircleLayout.setLayoutParams(params);
        ((TextView) mCircleLabelView.getViewById(VIEW_FACE_CIRCLE_LABEL_TEXT)).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mCircleLabelView.getViewById(VIEW_FACE_CIRCLE_LABEL_ICON).setPadding(iconPadding, iconPadding, iconPadding, iconPadding);
    }

    /**
     * CIRCLE (BG)
     * ---------- */

    public void setActiveCircleBackgroundColor(@ColorInt int color) {
        mCircleBgView.setTintTo(color, VIEW_FACE_CIRCLE_ACTIVE_BG);
    }

    public void setInactiveCircleBackgroundColor(@ColorInt int color) {
        mCircleBgView.setTintTo(color, VIEW_FACE_CIRCLE_INACTIVE_BG);
    }

    /**
     * CIRCLE (TEXT LABEL)
     * ---------- */

    public void setCircleTextLabel(String label) {
        ((TextView) mCircleLabelView.getViewById(VIEW_FACE_CIRCLE_LABEL_TEXT)).setText(label);
    }

    public void setCircleTextLabel(int label) {
        setCircleTextLabel(String.valueOf(label));
    }

    public void setCircleTextLabelTint(@ColorInt int color) {
        ((TextView) mCircleLabelView.getViewById(VIEW_FACE_CIRCLE_LABEL_TEXT)).setTextColor(color);
    }

    /**
     * CIRCLE (ICON LABEL)
     * ---------- */

    public void setCircleIconLabel(@DrawableRes int resId) {
        if (resId != 0) {
            ((ImageView) mCircleLabelView.getViewById(VIEW_FACE_CIRCLE_LABEL_ICON)).setImageResource(resId);
        }
    }

    public void setCircleIconLabelTint(@ColorInt int color) {
        ((ImageView) mCircleLabelView.getViewById(VIEW_FACE_CIRCLE_LABEL_ICON)).setColorFilter(color);
    }

    /**
     * CONNECTION LINE
     * ---------- */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // # Connection Line

        int connectionLineX = mCircleLayout.getLeft() + mCircleLayout.getWidth() / 2;
        int connectionLineStartY = mCircleLayout.getBottom() + mConnectionLineTopSpace;
        int contentHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int connectionLineEndY = contentHeight - mConnectionLineBottomSpace;

        canvas.drawLine(connectionLineX, connectionLineStartY, connectionLineX, connectionLineEndY, mConnectionLinePaint);
    }

    public void setConnectionLineColor(@ColorInt int color) {
        mConnectionLinePaint.setColor(color);
        invalidate();
    }

    /**
     * CONTENT VIEW
     * ---------- */

    public View addContentView(@LayoutRes int resId) {
        if (resId != 0) {
            View view = inflate(getContext(), resId, mContentViewContainer);
            invalidateContentViewContainerVisibility();
            return view;
        }
        return null;
    }

    public void addContentView(View view) {
        if (view != null) {
            mContentViewContainer.addView(view);
        }
        invalidateContentViewContainerVisibility();
    }

    public void removeContentView(View view) {
        if (view != null) {
            mContentViewContainer.removeView(view);
        }
        invalidateContentViewContainerVisibility();
    }

    public void removeAllContentViews() {
        mContentViewContainer.removeAllViews();
        invalidateContentViewContainerVisibility();
    }

    private void invalidateContentViewContainerVisibility() {
        if (0 < mContentViewContainer.getChildCount()) {
            mContentViewContainer.setVisibility(VISIBLE);
        } else {
            mContentViewContainer.setVisibility(GONE);
        }
    }

    /* Intentional package-private */
    ViewGroup getContentViewContainer() {
        return mContentViewContainer;
    }

    /**
     * STEPPER'S STATE MODE
     * ---------- */

    @Retention(SOURCE)
    @IntDef(
            flag = true,
            value = {STEP_MODE_ACTIVE, STEP_MODE_INACTIVE, STEP_MODE_CHECKED, STEP_MODE_UNCHECKED})
    public @interface StepMode {}
    public static final int STEP_MODE_ACTIVE = 1;
    public static final int STEP_MODE_INACTIVE = 1 << 1;
    public static final int STEP_MODE_CHECKED = 1 << 2;
    public static final int STEP_MODE_UNCHECKED = 1 << 3;

    private @StepMode int mCurrentStepModeFlags = 0;

    public void setStepMode(@StepMode int flags) {
        if ((flags & STEP_MODE_ACTIVE) != 0) {
            activateStep();
        }
        if ((flags & STEP_MODE_INACTIVE) != 0) {
            inactivateStep();
        }
        if ((flags & STEP_MODE_CHECKED) != 0) {
            checkStep();
        }
        if ((flags & STEP_MODE_UNCHECKED) != 0) {
            uncheckStep();
        }

        mCurrentStepModeFlags = flags;
    }

    public void activateStep() {
        mTitleView.setCurrentView(VIEW_FACE_ACTIVE_TITLE);
        mSubTitleView.setCurrentView(VIEW_FACE_ACTIVE_SUB_TITLE);
        mCircleBgView.setCurrentView(VIEW_FACE_CIRCLE_ACTIVE_BG);

        mCurrentStepModeFlags |= STEP_MODE_ACTIVE;
        // Remove the opposite state flag from the current flags
        mCurrentStepModeFlags &= ~STEP_MODE_INACTIVE;
    }

    public void inactivateStep() {
        mTitleView.setCurrentView(VIEW_FACE_INACTIVE_TITLE);
        mSubTitleView.setCurrentView(VIEW_FACE_INACTIVE_SUB_TITLE);
        mCircleBgView.setCurrentView(VIEW_FACE_CIRCLE_INACTIVE_BG);

        mCurrentStepModeFlags |= STEP_MODE_INACTIVE;
        mCurrentStepModeFlags &= ~STEP_MODE_ACTIVE;
    }

    public void checkStep() {
        mCircleLabelView.setCurrentView(VIEW_FACE_CIRCLE_LABEL_ICON);

        mCurrentStepModeFlags |= STEP_MODE_CHECKED;
        mCurrentStepModeFlags &= ~STEP_MODE_UNCHECKED;
    }

    public void uncheckStep() {
        mCircleLabelView.setCurrentView(VIEW_FACE_CIRCLE_LABEL_TEXT);

        mCurrentStepModeFlags |= STEP_MODE_UNCHECKED;
        mCurrentStepModeFlags &= ~STEP_MODE_CHECKED;
    }

    public boolean isStepActive() {
        return ((mCurrentStepModeFlags & STEP_MODE_ACTIVE) != 0);
    }

    public boolean isStepInactive() {
        return ((mCurrentStepModeFlags & STEP_MODE_INACTIVE) != 0);
    }

    public boolean isStepChecked() {
        return ((mCurrentStepModeFlags & STEP_MODE_CHECKED) != 0);
    }

    public boolean isStepUnchecked() {
        return ((mCurrentStepModeFlags & STEP_MODE_UNCHECKED) != 0);
    }

    public int getCurrentStepModeFlags() {
        return mCurrentStepModeFlags;
    }

    public void toggleStepActiveMode() {
        if (isStepActive()) {
            inactivateStep();
        } else {
            activateStep();
        }
    }

    public void toggleStepCheckMode() {
        if (isStepChecked()) {
            uncheckStep();
        } else {
            checkStep();
        }
    }

    public void toggleStepModes() {
        setStepMode(~mCurrentStepModeFlags);
    }
}