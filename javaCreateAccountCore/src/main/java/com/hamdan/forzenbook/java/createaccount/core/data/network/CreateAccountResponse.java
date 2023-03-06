package com.hamdan.forzenbook.java.createaccount.core.data.network;

import androidx.annotation.Nullable;

public class CreateAccountResponse {
    @Nullable
    final private String reason;

    public CreateAccountResponse(@Nullable String reason) {
        this.reason = reason;
    }

    @Nullable
    public String getReason() {
        return reason;
    }
}
