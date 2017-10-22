package com.f_candy_d.verticalsteppers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 10/20/17.
 */

public class StepManager implements Constants {

    private VerticalStepperListView mStepperListView;
    private VerticalStepperAdapter mStepperAdapter;
    private List<Step> mSteps;

    public StepManager() {
        mSteps = new ArrayList<>();
    }

    private boolean mIsBuildAlreadyFinished = false;

    /**
     * Call this method to setup vertical-steppers.
     * Steps must be added to StepManager before call this method.
     */
    public void build(Context context, VerticalStepperListView stepperListView) {
        if (mIsBuildAlreadyFinished) return;

        for (Step step : mSteps) {
            step.setParentManager(this);
        }

        mStepperListView = stepperListView;
        mStepperAdapter = new VerticalStepperAdapter(this);
        mStepperListView.setLayoutManager(new LinearLayoutManager(context));
        mStepperListView.setAdapter(mStepperAdapter);

        mIsBuildAlreadyFinished = true;
    }

    /**
     * GETTER & SETTER
     * ---------- */

    public VerticalStepperListView getStepperListView() {
        return mStepperListView;
    }

    public VerticalStepperAdapter getStepperAdapter() {
        return mStepperAdapter;
    }

    public boolean isBuildAlreadyFinished() {
        return mIsBuildAlreadyFinished;
    }

    /**
     * ADD STEP
     * ---------- */

    public void addStep(@NonNull Step step) {
        addStep(mSteps.size(), step);
    }

    public void addStep(int position, @NonNull Step step) {
        if (isBuildAlreadyFinished()) {
            throw new IllegalStateException(
                    "Steps must be added to StepManager before call #build() method");
        }
        mSteps.add(position, step);
    }

    /**
     * MANAGE STEP'S STATE CHANGE
     * ---------- */

    /**
     * Call this method when status of a step change to apply updates to step view.
     */
    public void notifyStepStatusChanged(Step step) {
        onStepStatusChanged(step);
    }

    public void notifyStepStatusChanged(int position) {
        onStepStatusChanged(position);
    }

    public void notifyManyStepsStatusChanged(int... positions) {
        onManyStepsStatusChanged(positions);
    }

    private void onStepStatusChanged(Step step) {
        int position = getStepPositionForUid(step.getUid());
        if (position != INVALID_POSITION) {
            onManyStepsStatusChanged(position);
        }
    }

    private void onStepStatusChanged(int position) {
        onManyStepsStatusChanged(position);
    }

    private void onManyStepsStatusChanged(int... positions) {
        mStepperListView.beginPartialItemTransition();
        for (int position : positions) {
            mStepperAdapter.onUpdateStepViewStatus(position);
        }
    }

    /**
     * ACTIVITY
     * ---------- */

    public void moveToNextStep(@NonNull Step fromStep, StepViewStatus nextStepStatus) {
        int nextPos = getStepPositionForUid(fromStep.getUid()) + 1;
        if (0 <= nextPos && nextPos < getStepCount()) {
            Step nextStep = getStepAt(nextPos);
            if (nextStepStatus != null) {
                nextStep.getStepStatus().set(nextStepStatus);
            }
            onManyStepsStatusChanged(nextPos - 1, nextPos);

        } else {
            onStepStatusChanged(nextPos - 1);
        }
    }

    public void moveToPreviousStep(@NonNull Step fromStep, StepViewStatus previousStepStatus) {
        int prevPos = getStepPositionForUid(fromStep.getUid()) - 1;
        if (0 <= prevPos && prevPos < getStepCount()) {
            Step prevStep = getStepAt(prevPos);
            if (previousStepStatus != null) {
                prevStep.getStepStatus().set(previousStepStatus);
            }
            onManyStepsStatusChanged(prevPos, prevPos + 1);

        } else {
            onStepStatusChanged(prevPos + 1);
        }
    }

    /**
     * UTILS
     * ---------- */

    public Step getStepAt(int position) {
        return mSteps.get(position);
    }

    @Nullable
    public Step getStepForUid(int uid) {
        int position = getStepPositionForUid(uid);
        if (position != INVALID_POSITION) {
            return getStepAt(position);
        } else {
            return null;
        }
    }

    public int getStepPositionForUid(int uid) {
        int position = 0;
        for (Step step : mSteps) {
            if (step.getUid() == uid) {
                return position;
            }
            ++position;
        }

        return INVALID_POSITION;
    }

    public int getStepCount() {
        return mSteps.size();
    }

    /* Intentional package-private */
    StepViewStatus getStepViewStatusAt(int position) {
        return mSteps.get(position).getStepStatus();
    }

    @Nullable
    private StepViewStatus getStepViewStatusForUid(int uid) {
        for (Step step : mSteps) {
            if (step.getUid() == uid) {
                return step.getStepStatus();
            }
        }

        return null;
    }
}
