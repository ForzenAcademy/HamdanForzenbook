package com.hamdan.forzenbook.java.core;

import androidx.annotation.NonNull;

public class UserEntryError {
    private ErrorOutcomes state = ErrorOutcomes.NONE;

    public UserEntryError() {}

    public UserEntryError(ErrorOutcomes errorState) {
        state = errorState;
    }

    public boolean isValid() {
        return state == ErrorOutcomes.VALID;
    }

    public boolean equals(UserEntryError o) {
        return o.state == this.state;
    }

    public void setState(ErrorOutcomes state) {
        this.state = state;
    }

    @NonNull
    public ErrorOutcomes getState() {
        return state;
    }
}
