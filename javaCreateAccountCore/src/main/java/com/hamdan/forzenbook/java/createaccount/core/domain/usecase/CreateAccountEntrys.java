package com.hamdan.forzenbook.java.createaccount.core.domain.usecase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hamdan.forzenbook.java.core.Entry;

public class CreateAccountEntrys {

    final private Entry firstName;
    final private Entry lastName;
    final private Entry birthDay;
    final private Entry email;
    final private Entry location;

    public CreateAccountEntrys(
            Entry first,
            Entry last,
            Entry date,
            Entry inEmail,
            Entry inLocation
    ) {
        firstName = first;
        lastName = last;
        birthDay = date;
        location = inLocation;
        email = inEmail;
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
}
