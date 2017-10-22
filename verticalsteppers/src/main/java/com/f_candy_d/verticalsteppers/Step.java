package com.f_candy_d.verticalsteppers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by daichi on 10/19/17.
 */

abstract public class Step implements Constants {

    private final int mUid;
    private StepManager mParentManager;
    @NonNull private StepViewStatus mStepViewStatus;

    public Step(int uid) {
        this(uid, null);
    }

    public Step(int uid, StepViewStatus status) {
        mUid = uid;
        if (status != null) {
            mStepViewStatus = new StepViewStatus(status);
        } else {
            mStepViewStatus = new StepViewStatus();
        }
    }

    /**
     * GETTER & SETTER
     * ---------- */

    /* Intentional package-private */
    void setParentManager(StepManager parentManager) {
        mParentManager = parentManager;
    }

    public int getUid() {
        return mUid;
    }

    /**
     * STEP DATA
     * ---------- */

    abstract public String getTitle();
    abstract public String getSubTitle();
    abstract public int getNumberLabel();

    /**
     * Override this method and return TRUE if you want to
     * use text as a stepper's circular label instead of number.
     */
    protected boolean useTextLabel() { return false; }

    /**
     * Override this method if you override {@link Step#useTextLabel()}
     * method and return TRUE in it.
     */
    public String getTextLabel() { return null; }

    /**
     * STEP STATE
     * ---------- */

    @NonNull
    public StepViewStatus getStepStatus() {
        return mStepViewStatus;
    }

    public void toggleExpandedState() {
        mStepViewStatus.setExpanded(!mStepViewStatus.isExpanded());
    }

    public void toggleActivatedState() {
        mStepViewStatus.setActivated(!mStepViewStatus.isActivated());
    }

    public void toggleCheckedState() {
        mStepViewStatus.setChecked(!mStepViewStatus.isChecked());
    }

    public void toggleAllStatus() {
        toggleExpandedState();
        toggleActivatedState();
        toggleCheckedState();
    }

    public void notifyStepStatusChanged() {
        if (mParentManager != null) {
            mParentManager.notifyStepStatusChanged(this);
        }
    }

    /**
     * EXPANDED (COLLAPSED) CONTENT VIEW
     * ---------- */

    abstract public static class ContentViewHolder {

        public View contentRoot;

        public ContentViewHolder(View view) {
            if (view == null) {
                throw new NullPointerException(
                        "The first parameter can not be a null");
            }
            contentRoot = view;
        }
    }

    /**
     * Do not attach a inflated view to 'parent' in this method.
     */
    protected ContentViewHolder onCreateExpandedContentViewHolder(ViewGroup parent) {
        return null;
    }

    /**
     * Do not attach a inflated view to 'parent' in this method.
     */
    protected ContentViewHolder onCreateCollapsedContentViewHolder(ViewGroup parent) {
        return null;
    }

    /**
     * TOUCH EVENT
     * ---------- */

    /**
     * Override this method if you want to handle a stepper's click event.
     */
    protected void onStepClick() {}

    /**
     * Override this method if you want to handle a stepper's long click event.
     *
     * @return {@link Step#onStepClick()} will be not called if return TRUE.
     */
    protected boolean onStepLongClick() { return false; }

    /**
     * ACTIVITY
     * ---------- */

    /**
     * Move to the next step from this step.
     */
    public void moveToNextStep(StepViewStatus nextStepStatus) {

        if (mParentManager != null) {
            mParentManager.moveToNextStep(this, nextStepStatus);
        }
    }

    /**
     * Move to the previous step from this step.
     */
    public void moveToPreviousStep(StepViewStatus previousStepStatus) {

        if (mParentManager != null) {
            mParentManager.moveToPreviousStep(this, previousStepStatus);
        }
    }

    /**
     * UTILS
     * ---------- */

    @Nullable
    public StepViewStatus getNextStepStatus() {
        if (mParentManager == null) return null;
        int position = mParentManager.getStepPositionForUid(getUid());
        if (position == INVALID_POSITION) return null;
        position += 1;
        if (0 <= position && position < mParentManager.getStepCount()) {
            StepViewStatus status = mParentManager.getStepViewStatusAt(position);
            return new StepViewStatus(status);
        }

        return null;
    }

    @Nullable
    public StepViewStatus getPreviousStepStatus() {
        if (mParentManager == null) return null;
        int position = mParentManager.getStepPositionForUid(getUid());
        if (position == INVALID_POSITION) return null;
        position -= 1;
        if (0 <= position && position < mParentManager.getStepCount()) {
            StepViewStatus status = mParentManager.getStepViewStatusAt(position);
            return new StepViewStatus(status);
        }

        return null;
    }

    public int getPosition() {
        if (mParentManager == null) return INVALID_POSITION;
        return mParentManager.getStepPositionForUid(getUid());
    }

    public boolean isFirstStep() {
        int position = getPosition();
        return (position != INVALID_POSITION && position == 0);
    }

    public boolean isLastStep() {
        int position = getPosition();
        return (position != INVALID_POSITION &&
                position == mParentManager.getStepCount() - 1);
    }
}
