package com.hamdan.forzenbook.java.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.hamdan.forzenbook.java.core.ErrorOutcomes;
import com.hamdan.forzenbook.java.core.utils.DialogUtils;
import com.hamdan.forzenbook.java.login.core.viewmodel.JavaLoginViewModel;
import com.hamdan.forzenbook.ui.core.R;
import com.hamdan.forzenbook.ui.core.databinding.ActivityLegacyLoginBinding;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

@AndroidEntryPoint
public class JavaLoginActivity extends ComponentActivity {
    private JavaLoginViewModel model;
    private ActivityLegacyLoginBinding binding;

    private String emailValue = "";
    private String codeValue = "";

    private Disposable disposable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(JavaLoginViewModel.class);
        binding = ActivityLegacyLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.loginSubmitButton.setEnabled(false);
        binding.loginClickBlocker.setOnClickListener(blank -> {
        });  //purposely empty to prevent clicks when its enabled

        binding.loginCreateAccountLink.setOnClickListener(listener -> model.createAccountLinkPressed(this));

        binding.loginSubmitButton.setOnClickListener(listener -> model.loginClicked());

        disposable = model.getState()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        state -> {

                            emailValue = state.getEmail().getText();
                            codeValue = state.getCode().getText();

                            binding.loginSubmitButton.setEnabled(
                                    state.getEmail().isValid() && !state.getInputtingCode() || state.getEmail().isValid() && state.getCode().isValid()
                            );

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

                            if (state.getInputtingCode()) {
                                binding.inputEmailBody.setVisibility(View.GONE);
                                binding.emailErrorText.setVisibility(View.GONE);
                                binding.inputCodeBody.setVisibility(View.VISIBLE);
                                if (state.getCode().isValid() && !state.getCode().getText().isEmpty() && state.getCode().getError().getState() == ErrorOutcomes.LENGTH) {
                                    binding.codeErrorText.setVisibility(View.VISIBLE);
                                } else {
                                    binding.codeErrorText.setVisibility(View.GONE);
                                }
                            } else {
                                binding.codeErrorText.setVisibility(View.GONE);
                                binding.inputCodeBody.setVisibility(View.GONE);
                            }

                            if (state.getLoading()) {
                                binding.loginClickBlocker.setVisibility(View.VISIBLE);
                                binding.loginSubmitText.setVisibility(View.GONE);
                                binding.loginSubmitProgressIndicator.setVisibility(View.VISIBLE);
                            } else {
                                binding.loginClickBlocker.setVisibility(View.GONE);
                                binding.loginSubmitText.setVisibility(View.VISIBLE);
                                binding.loginSubmitProgressIndicator.setVisibility(View.GONE);
                            }

                            if (state.shouldShowInfoDialog()) {
                                DialogUtils.standardAlertDialog(
                                        this,
                                        getString(R.string.login_info_title),
                                        getString(R.string.login_info),
                                        getString(R.string.generic_dialog_confirm),
                                        () -> {
                                            model.loginDismissInfoClicked();
                                        }
                                );
                            }

                            if (state.getHasError()) {
                                DialogUtils.standardAlertDialog(
                                        this,
                                        getString(R.string.login_error_title),
                                        getString(R.string.login_error),
                                        getString(R.string.generic_dialog_confirm),
                                        () -> {
                                            model.loginDismissErrorClicked();
                                        }
                                );
                            }
                        }
                );
        binding.inputEmailText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        model.updateLoginTexts(
                                charSequence.toString(),
                                codeValue
                        );
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                }
        );
        binding.inputCodeText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        model.updateLoginTexts(
                                emailValue,
                                charSequence.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
