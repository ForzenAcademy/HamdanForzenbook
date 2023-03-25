package com.hamdan.forzenbook.java.createaccount;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.hamdan.forzenbook.java.core.ErrorOutcomes;
import com.hamdan.forzenbook.java.core.utils.DialogUtils;
import com.hamdan.forzenbook.java.createaccount.core.viewmodel.JavaCreateAccountViewModel;
import com.hamdan.forzenbook.ui.core.R;
import com.hamdan.forzenbook.ui.core.databinding.ActivityLegacyCreateAccountBinding;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

@AndroidEntryPoint
public class JavaCreateAccountActivity extends ComponentActivity {
    private JavaCreateAccountViewModel model;
    private ActivityLegacyCreateAccountBinding binding;
    private String firstNameValue = "";
    private String lastNameValue = "";
    private String birthDateValue = "";
    private String emailValue = "";
    private String locationValue = "";
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(JavaCreateAccountViewModel.class);
        binding = ActivityLegacyCreateAccountBinding.inflate(getLayoutInflater());
        FrameLayout view = binding.getRoot();
        setContentView(view);

        binding.createAccountSubmitButton.setEnabled(false);
        binding.createAccountClickBlocker.setOnClickListener(blank -> {
        });  //purposely empty to prevent clicks when its enabled

        binding.createAccountLayoutToolBar.setNavigationOnClickListener(listener -> model.backButtonPressed(this));

        binding.createAccountSubmitButton.setOnClickListener(listener -> model.createAccountClicked(this));

        disposable = model.getState()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        state -> {
                            firstNameValue = state.getFirstName().getText();
                            lastNameValue = state.getLastName().getText();
                            birthDateValue = state.getBirthDay().getText();
                            emailValue = state.getEmail().getText();
                            locationValue = state.getLocation().getText();
                            if (state.getOpenDateDialog() && !state.getIsDateDialogOpen()) {
                                DialogUtils.datePickerDialog(
                                        this,
                                        state.getBirthDay().getText(),
                                        text -> {
                                            binding.inputBirthDateText.setText(text);
                                            model.updateCreateAccountTextAndErrors(
                                                    firstNameValue,
                                                    lastNameValue,
                                                    text,
                                                    emailValue,
                                                    locationValue
                                            );
                                        },
                                        () -> model.createAccountDateDialogSubmitClicked(),
                                        () -> {
                                            model.createAccountDateDialogDismiss();
                                        }
                                );
                                model.onDateDialogCreate();
                            }

                            if (state.getLoading()) {
                                binding.createAccountClickBlocker.setVisibility(View.VISIBLE);
                                binding.createAccountSubmitText.setVisibility(View.GONE);
                                binding.createAccountSubmitProgressIndicator.setVisibility(View.VISIBLE);
                            } else {
                                binding.createAccountClickBlocker.setVisibility(View.GONE);
                                binding.createAccountSubmitText.setVisibility(View.VISIBLE);
                                binding.createAccountSubmitProgressIndicator.setVisibility(View.GONE);
                            }

                            if (state.getErrorId() != null) {
                                DialogUtils.standardAlertDialog(this, getString(R.string.create_account_error_title), getString(state.getErrorId()),
                                        getString(R.string.generic_dialog_confirm), () -> model.createAccountDismissErrorClicked());
                            }

                            binding.createAccountSubmitButton.setEnabled(
                                    state.getEmail().isValid() && state.getBirthDay().isValid()
                                            && state.getFirstName().isValid() && state.getLastName().isValid()
                                            && state.getLocation().isValid()
                            );
                            if (!state.getFirstName().getText().isEmpty() && !state.getFirstName().getError().isValid()) {
                                if (state.getFirstName().getError().getState() == ErrorOutcomes.LENGTH) {
                                    binding.firstNameErrorText.setText(com.hamdan.forzenbook.ui.core.R.string.create_account_first_name_error_length);
                                    binding.firstNameErrorText.setVisibility(View.VISIBLE);
                                } else if (state.getFirstName().getError().getState() == ErrorOutcomes.INVALID_CHARACTERS) {
                                    binding.firstNameErrorText.setText(com.hamdan.forzenbook.ui.core.R.string.create_account_first_name_error_invalid_characters);
                                    binding.firstNameErrorText.setVisibility(View.VISIBLE);
                                }
                            } else {
                                binding.firstNameErrorText.setVisibility(View.GONE);
                            }

                            if (!state.getLastName().getText().isEmpty() && !state.getLastName().getError().isValid()) {
                                if (state.getLastName().getError().getState() == ErrorOutcomes.LENGTH) {
                                    binding.lastNameErrorText.setText(com.hamdan.forzenbook.ui.core.R.string.create_account_last_name_error_length);
                                    binding.lastNameErrorText.setVisibility(View.VISIBLE);
                                } else if (state.getLastName().getError().getState() == ErrorOutcomes.INVALID_CHARACTERS) {
                                    binding.lastNameErrorText.setText(com.hamdan.forzenbook.ui.core.R.string.create_account_last_name_error_invalid_characters);
                                    binding.lastNameErrorText.setVisibility(View.VISIBLE);
                                }
                            } else {
                                binding.lastNameErrorText.setVisibility(View.GONE);
                            }

                            if (state.getBirthDay().getError().getState() == ErrorOutcomes.VALID && !state.getBirthDay().getText().isEmpty() || state.getBirthDay().getError().getState() == ErrorOutcomes.NONE) {
                                binding.birthDateErrorText.setVisibility(View.GONE);
                            } else {
                                binding.birthDateErrorText.setVisibility(View.VISIBLE);
                            }

                            if (!state.getEmail().isValid() && !state.getEmail().getText().isEmpty()) {
                                if (state.getEmail().getError().getState() == ErrorOutcomes.LENGTH) {
                                    binding.emailErrorText.setText(com.hamdan.forzenbook.ui.core.R.string.login_email_error_length);
                                    binding.emailErrorText.setVisibility(View.VISIBLE);
                                } else if (state.getEmail().getError().getState() == ErrorOutcomes.INVALID_FORMAT) {
                                    binding.emailErrorText.setText(com.hamdan.forzenbook.ui.core.R.string.login_email_error_format);
                                    binding.emailErrorText.setVisibility(View.VISIBLE);
                                }
                            } else {
                                binding.emailErrorText.setVisibility(View.GONE);
                            }

                            if (state.getLocation().getError().getState() == ErrorOutcomes.LENGTH) {
                                binding.locationErrorText.setVisibility(View.VISIBLE);
                            } else {
                                binding.locationErrorText.setVisibility(View.GONE);
                            }

                        }
                );

        binding.inputFirstNameText.addTextChangedListener(
                generateTextWatcher(inText -> simpleUpdate(inText, null, null, null))
        );
        binding.inputLastNameText.addTextChangedListener(
                generateTextWatcher(inText -> simpleUpdate(null, inText, null, null))
        );
        binding.inputEmailText.addTextChangedListener(
                generateTextWatcher(inText -> simpleUpdate(null, null, inText, null))
        );
        binding.inputLocationText.addTextChangedListener(
                generateTextWatcher(inText -> simpleUpdate(null, null, null, inText))
        );
        binding.inputBirthDateText.setOnClickListener(watcher -> model.createAccountDateDialogClicked());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            disposable.dispose();
        } catch (Exception e) {
            Log.v("Exception", e.toString());
        }
    }

    // following two functions are specificaly made for this activity + view model implementation
    private TextWatcher generateTextWatcher(OnTextChange onTextChange) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onTextChange.run(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private void simpleUpdate(String first, String last, String email, String location) {
        if (first != null) firstNameValue = first;
        else if (last != null) lastNameValue = last;
        else if (email != null) emailValue = email;
        else if (location != null) locationValue = location;
        model.updateCreateAccountTextAndErrors(
                firstNameValue,
                lastNameValue,
                birthDateValue,
                emailValue,
                locationValue
        );
    }
}