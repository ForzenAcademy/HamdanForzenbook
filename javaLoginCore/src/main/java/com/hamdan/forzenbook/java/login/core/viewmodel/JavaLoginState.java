package com.hamdan.forzenbook.java.login.core.viewmodel;

import androidx.annotation.NonNull;

import com.hamdan.forzenbook.java.core.Entry;
import com.hamdan.forzenbook.java.core.ErrorOutcomes;

public class JavaLoginState {
    final private Entry email;
    final private Entry code;
    final private boolean showInfoDialog;
    final private boolean inputtingCode;
    final private boolean isLoading;
    final private boolean hasError;

    public JavaLoginState() {
        email = new Entry("", ErrorOutcomes.NONE);
        code = new Entry("", ErrorOutcomes.NONE);
        showInfoDialog = false;
        inputtingCode = false;
        isLoading = false;
        hasError = false;
    }

    public JavaLoginState(Entry inEmail, Entry inCode, Boolean dialogState, Boolean codeState, Boolean loading, Boolean errorState) {
        email = inEmail;
        code = inCode;
        showInfoDialog = dialogState;
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

    public boolean getShowInfoDialog() {
        return showInfoDialog;
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
