package com.f_candy_d.verticalsteppers;

import com.f_candy_d.verticalsteppers.VerticalStepperAdapter;

/**
 * Created by daichi on 10/20/17.
 */

/* Intentional package-private */
interface StepClickListener {
    void onStepClicked(VerticalStepperAdapter.StepViewHolder holder);

    /**
     * {@link StepClickListener#onStepClicked(VerticalStepperAdapter.StepViewHolder)}
     * will not be called if return true.
     */
    boolean onStepLongClicked(VerticalStepperAdapter.StepViewHolder holder);
}
