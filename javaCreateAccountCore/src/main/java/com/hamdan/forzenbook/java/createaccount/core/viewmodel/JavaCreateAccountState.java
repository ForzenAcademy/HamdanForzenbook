package com.hamdan.forzenbook.java.createaccount.core.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hamdan.forzenbook.java.core.Entry;
import com.hamdan.forzenbook.java.core.UserEntryError;

public class JavaCreateAccountState {

    @Nullable
    final private Integer errorId;
    final private Entry firstName;
    final private Entry lastName;
    final private Entry birthDay;
    final private Entry email;
    final private Entry location;
    final private boolean isDateDialogOpen;
    final private boolean isLoading;

    public JavaCreateAccountState(
            @Nullable Integer id,
            Entry first,
            Entry last,
            Entry date,
            Entry inEmail,
            Entry inLocation,
            boolean dialogState,
            boolean loadingState
    ) {
        errorId = id;
        firstName = first;
        lastName = last;
        birthDay = date;
        location = inLocation;
        email = inEmail;
        isDateDialogOpen = dialogState;
        isLoading = loadingState;
    }

    public JavaCreateAccountState() {
        errorId = null;
        firstName = new Entry("", new UserEntryError());
        lastName = new Entry("", new UserEntryError());
        birthDay = new Entry("", new UserEntryError());
        location = new Entry("", new UserEntryError());
        email = new Entry("", new UserEntryError());
        isDateDialogOpen = false;
        isLoading = false;
    }

    @Nullable
    public Integer getErrorId() {
        return errorId;
    }

    @NonNull
    public Entry getFirstName() {
        return firstName;
    }

    @NonNull
    public Entry getLastName() {
        return lastName;
    }

    @NonNull
    public Entry getBirthDay() {
        return birthDay;
    }

    @NonNull
    public Entry getEmail() {
        return email;
    }

    @NonNull
    public Entry getLocation() {
        return location;
    }

    public boolean getDateDialogOpen() {
        return isDateDialogOpen;
    }

    public boolean getLoading() {
        return isLoading;
    }
}
