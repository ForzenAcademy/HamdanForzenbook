package com.hamdan.forzenbook.java.login.core.domain.usecase;

import static com.hamdan.forzenbook.java.core.GlobalConstants.EMAIL_LENGTH_LIMIT;

import androidx.annotation.NonNull;

import com.hamdan.forzenbook.java.core.Entry;
import com.hamdan.forzenbook.java.core.ErrorOutcomes;
import com.hamdan.forzenbook.java.core.GeneralUtilityFunctions;
import com.hamdan.forzenbook.java.login.core.domain.LoginEntrys;

public class LoginStringValidationUseCaseImpl implements LoginStringValidationUseCase {
    final private int CODE_LENGTH_MAX = 6;

    @Override
    @NonNull
    public LoginEntrys invoke(LoginEntrys state) {
        ErrorOutcomes emailError;
        ErrorOutcomes codeError;
        if (state.getEmail().getText().length() > EMAIL_LENGTH_LIMIT) {
            emailError = ErrorOutcomes.LENGTH;
        } else if (GeneralUtilityFunctions.validateEmail(state.getEmail().getText())) {
            emailError = ErrorOutcomes.INVALID_FORMAT;
        } else emailError = ErrorOutcomes.VALID;

        if (state.getCode().getText().length() > CODE_LENGTH_MAX) {
            codeError = ErrorOutcomes.LENGTH;
        } else codeError = ErrorOutcomes.VALID;

        return new LoginEntrys(
                new Entry(state.getEmail().getText(), emailError),
                new Entry(state.getCode().getText(), codeError)
        );
    }
}
