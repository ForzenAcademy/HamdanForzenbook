package com.hamdan.forzenbook.java.core;

import androidx.annotation.NonNull;

public class Entry {
    private String text;
    private UserEntryError error;

    public Entry(String initText, UserEntryError initError) {
        text = initText;
        error = initError;
    }
    public Entry(){
        text = "";
        error = new UserEntryError();
    }

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @NonNull
    public UserEntryError getError() {
        return error;
    }

    public void setError(UserEntryError error) {
        this.error = error;
    }

    public Entry(String initText, ErrorOutcomes initError){
        text = initText;
        error = new UserEntryError(initError);
    }
}
