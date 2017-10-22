package com.f_candy_d.steppers;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.f_candy_d.verticalsteppers.Step;
import com.f_candy_d.verticalsteppers.StepManager;
import com.f_candy_d.verticalsteppers.StepViewStatus;
import com.f_candy_d.verticalsteppers.VerticalStepperListView;

public class VerticalSteppersDemoActivity extends AppCompatActivity implements StepInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_steppers_demo);

        VerticalStepperListView stepperListView = (VerticalStepperListView) findViewById(R.id.vertical_steppers);
        StepManager manager = new StepManager();

        manager.addStep(new InputUserNameStep(this));
        manager.addStep(new SelectPaymentMethodsStep(this));
        manager.addStep(new AcceptPrivacyPolicyStep(this));

        manager.build(this, stepperListView);

        // Initial status of the first step
        Step first = manager.getStepAt(0);
        first.getStepStatus().setExpanded(true);
        first.getStepStatus().setActivated(true);
        first.getStepStatus().setChecked(false);
        manager.notifyStepStatusChanged(0);
    }

    /**
     * INTERFACE IMPL -> STEP_INTERACTION_LISTENER
     * ---------- */

    @Override
    public void onInputFormCanceled() {
        finish();
    }

    @Override
    public void onInputFormFinished() {
        Toast.makeText(this, "User data was saved!", Toast.LENGTH_SHORT).show();
        finish();
    }

    // UIDs
    private static final int INPUT_USER_NAME_STEP = 1;
    private static final int SELECT_PAYMENT_METHODS_STEP = 2;
    private static final int ACCEPT_PRIVACY_POLICY_STEP = 3;

    private static class BaseContentVh extends Step.ContentViewHolder {

        private StepInteractionListener mListener;

        BaseContentVh(View view, StepInteractionListener listener) {
            super(view);
            mListener = listener;
        }

        void goToNextStep(Step step) {
            if (!step.isLastStep()) {
                step.getStepStatus().setExpanded(false);
                step.getStepStatus().setActivated(true);
                step.getStepStatus().setChecked(true);
                StepViewStatus next = step.getNextStepStatus();
                if (next != null) {
                    next.setExpanded(true);
                    next.setActivated(true);
                    next.setChecked(false);
                }

                /**
                 * No need to call {@link Step#notifyStepStatusChanged()}
                 * when call {@link Step#moveToNextStep(StepViewStatus)} or
                 * {@link Step#moveToPreviousStep(StepViewStatus)}.
                 */
                step.moveToNextStep(next);

            } else {
                mListener.onInputFormFinished();
            }
        }

        void backToPreviousStep(Step step) {
            if (!step.isFirstStep()) {
                step.getStepStatus().setExpanded(false);
                step.getStepStatus().setActivated(false);
                step.getStepStatus().setChecked(false);
                StepViewStatus prev = step.getPreviousStepStatus();
                if (prev != null) {
                    prev.setExpanded(true);
                    prev.setActivated(true);
                    prev.setChecked(false);
                }
                step.moveToPreviousStep(prev);

            } else {
                mListener.onInputFormCanceled();
            }
        }
    }

    private static class InputUserNameStep extends Step {

        private StepInteractionListener mListener;

        InputUserNameStep(StepInteractionListener listener) {
            super(INPUT_USER_NAME_STEP);
            mListener = listener;
        }

        @Override
        public String getTitle() {
            return "USER NAME";
        }

        @Override
        public String getSubTitle() {
            return "Input user name.";
        }

        @Override
        public int getNumberLabel() {
            return getUid();
        }

        @Override
        protected ContentViewHolder onCreateExpandedContentViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_user_name, parent, false);
            return new EditUserNameVh(view, this, mListener);
        }

        static class EditUserNameVh extends BaseContentVh {

            EditUserNameVh(final View view, final InputUserNameStep step, StepInteractionListener listener) {
                super(view, listener);
                view.findViewById(R.id.continue_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextInputEditText editText = view.findViewById(R.id.edit_text);
                        TextInputLayout inputLayout = view.findViewById(R.id.text_input_layout);
                        if (editText.getText().length() < 10) {
                            inputLayout.setError("Enter 10 or more characters");
                        } else {
                            inputLayout.setError(null);
                            goToNextStep(step);
                        }
                    }
                });

                view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        backToPreviousStep(step);
                    }
                });
            }
        }
    }

    private static class SelectPaymentMethodsStep extends Step {

        private StepInteractionListener mListener;

        SelectPaymentMethodsStep(StepInteractionListener listener) {
            super(SELECT_PAYMENT_METHODS_STEP);
            mListener = listener;
        }

        @Override
        public String getTitle() {
            return "PAYMENT METHODS";
        }

        @Override
        public String getSubTitle() {
            return "Select your payment methods from the following selections.";
        }

        @Override
        public int getNumberLabel() {
            return getUid();
        }

        @Override
        protected ContentViewHolder onCreateExpandedContentViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_select_payment_methods, parent, false);
            return new SelectPaymentMethodVh(view, this, mListener);
        }

        static class SelectPaymentMethodVh extends BaseContentVh {

            SelectPaymentMethodVh(final View view, final SelectPaymentMethodsStep step, StepInteractionListener listener) {
                super(view, listener);

                final CheckBox box = view.findViewById(R.id.box_credit_debit_card);
                final CheckBox box1 = view.findViewById(R.id.box_google_play);
                final CheckBox box2 = view.findViewById(R.id.box_paypol);
                final CheckBox box3 = view.findViewById(R.id.box_bitcoin);

                view.findViewById(R.id.continue_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!(box.isChecked() || box1.isChecked() || box2.isChecked() || box3.isChecked())) {
                            Toast.makeText(view.getContext(), "Select at least one method", Toast.LENGTH_SHORT).show();
                        } else {
                            goToNextStep(step);
                        }
                    }
                });

                view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        backToPreviousStep(step);
                    }
                });
            }
        }
    }

    private static class AcceptPrivacyPolicyStep extends Step {

        private StepInteractionListener mListener;

        AcceptPrivacyPolicyStep(StepInteractionListener listener) {
            super(ACCEPT_PRIVACY_POLICY_STEP);
            mListener = listener;
        }

        @Override
        public String getTitle() {
            return "PRIVACY POLICY";
        }

        @Override
        public String getSubTitle() {
            return "Read and click accept & save button if you accept to the policy.";
        }

        @Override
        public int getNumberLabel() {
            return getUid();
        }

        @Override
        protected ContentViewHolder onCreateExpandedContentViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_accept_to_privacy_policy, parent, false);
            return new AcceptPrivacyPolicyVh(view, this, mListener);
        }

        static class AcceptPrivacyPolicyVh extends BaseContentVh {

            AcceptPrivacyPolicyVh(View view, final AcceptPrivacyPolicyStep step, StepInteractionListener listener) {
                super(view, listener);
                view.findViewById(R.id.accept_and_save_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToNextStep(step);
                    }
                });

                view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        backToPreviousStep(step);
                    }
                });
            }
        }
    }
}
