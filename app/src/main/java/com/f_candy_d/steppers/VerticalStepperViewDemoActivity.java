package com.f_candy_d.steppers;

import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.f_candy_d.verticalsteppers.VerticalStepper;

public class VerticalStepperViewDemoActivity extends AppCompatActivity {

    private int mCurrentStepperCircleSize = VerticalStepper.CIRCLE_SIZE_SMALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_stepper_view_demo);

        final RelativeLayout rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        final VerticalStepper stepper = (VerticalStepper) findViewById(R.id.vertical_stepper);

        findViewById(R.id.toggle_active_mode_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(rlRoot);
                stepper.toggleStepActiveMode();
            }
        });

        findViewById(R.id.toggle_circle_size_mode_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentStepperCircleSize == VerticalStepper.CIRCLE_SIZE_REGULAR) {
                    stepper.setCircleSize(VerticalStepper.CIRCLE_SIZE_SMALL);
                    mCurrentStepperCircleSize = VerticalStepper.CIRCLE_SIZE_SMALL;
                } else {
                    stepper.setCircleSize(VerticalStepper.CIRCLE_SIZE_REGULAR);
                    mCurrentStepperCircleSize = VerticalStepper.CIRCLE_SIZE_REGULAR;
                }
            }
        });

        findViewById(R.id.toggle_check_mode_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(rlRoot);
                stepper.toggleStepCheckMode();
            }
        });

        findViewById(R.id.toggle_all_modes_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(rlRoot);
                stepper.toggleStepModes();
            }
        });
    }
}
