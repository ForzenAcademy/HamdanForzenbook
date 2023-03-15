package com.hamdan.forzenbook.java.login.core.viewmodel;

import androidx.annotation.NonNull;

import com.hamdan.forzenbook.java.core.Entry;
import com.hamdan.forzenbook.java.core.ErrorOutcomes;

public class JavaLoginState {
    final private Entry email;
    final private Entry code;
    final private boolean shouldShowInfoDialog;
    final private boolean inputtingCode;
    final private boolean isLoading;
    final private boolean hasError;

    public JavaLoginState() {
        email = new Entry("", ErrorOutcomes.NONE);
        code = new Entry("", ErrorOutcomes.NONE);
        shouldShowInfoDialog = false;
        inputtingCode = false;
        isLoading = false;
        hasError = false;
    }

    public JavaLoginState(Entry inEmail, Entry inCode, Boolean dialogState, Boolean codeState, Boolean loading, Boolean errorState) {
        email = inEmail;
        code = inCode;
        shouldShowInfoDialog = dialogState;
        inputtingCode = codeState;
        isLoading = loading;
        hasError = errorState;
    }

    @NonNull
    public Entry getEmail() {
        return email;
    }

    @NonNull
    public Entry getCode() {
        return code;
    }

    public boolean shouldShowInfoDialog() {
        return shouldShowInfoDialog;
    }

    public boolean getInputtingCode() {
        return inputtingCode;
    }

    public boolean getLoading() {
        return isLoading;
    }

    public boolean getHasError() {
        return hasError;
    }
}
