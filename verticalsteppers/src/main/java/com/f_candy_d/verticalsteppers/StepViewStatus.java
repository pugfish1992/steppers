package com.f_candy_d.verticalsteppers;

import android.support.annotation.NonNull;

/**
 * Created by daichi on 10/21/17.
 */

public class StepViewStatus {

    private static class Status {

        private boolean isExpanded;
        private boolean isActivated;
        private boolean isChecked;

        Status() {}

        Status(Status source) {
            set(source);
        }

        void set(Status source) {
            isExpanded = source.isExpanded;
            isActivated = source.isActivated;
            isChecked = source.isChecked;
        }
    }

    @NonNull private Status mPrevStatus;
    @NonNull private Status mStatus;

    public StepViewStatus() {
        mStatus = new Status();
        mStatus.isExpanded = false;
        mStatus.isActivated = false;
        mStatus.isChecked = false;
        mPrevStatus = new Status(mStatus);
    }

    public StepViewStatus(StepViewStatus source) {
        mStatus = new Status(source.mStatus);
        mPrevStatus = new Status(source.mPrevStatus);
    }

    /* Intentional package-private */
    boolean isDirty() {
        return isExpandedStateChanged() ||
                isCheckedStateChanged() ||
                isActivatedStateChanged();
    }

    /* Intentional package-private */
    void refresh() {
        mPrevStatus.set(mStatus);
    }

    public void set(StepViewStatus source) {
        setExpanded(source.isExpanded());
        setActivated(source.isActivated());
        setChecked(source.isChecked());
    }

    public boolean isExpanded() {
        return mStatus.isExpanded;
    }

    public void setExpanded(boolean expanded) {
        mStatus.isExpanded = expanded;
    }

    public boolean isActivated() {
        return mStatus.isActivated;
    }

    public void setActivated(boolean activated) {
        mStatus.isActivated = activated;
    }

    public boolean isChecked() {
        return mStatus.isChecked;
    }

    public void setChecked(boolean checked) {
        mStatus.isChecked = checked;
    }

    public boolean isExpandedStateChanged() {
        return mPrevStatus.isExpanded != mStatus.isExpanded;
    }

    public boolean isActivatedStateChanged() {
        return mPrevStatus.isActivated != mStatus.isActivated;
    }

    public boolean isCheckedStateChanged() {
        return mPrevStatus.isChecked != mStatus.isChecked;
    }
}
