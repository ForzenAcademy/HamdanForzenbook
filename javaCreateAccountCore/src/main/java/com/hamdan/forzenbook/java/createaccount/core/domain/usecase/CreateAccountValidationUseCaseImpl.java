package com.hamdan.forzenbook.java.createaccount.core.domain.usecase;

import androidx.annotation.NonNull;

import com.hamdan.forzenbook.java.core.Entry;
import com.hamdan.forzenbook.java.core.ErrorOutcomes;
import com.hamdan.forzenbook.java.core.GeneralUtilityFunctions;
import com.hamdan.forzenbook.java.core.GlobalConstants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class CreateAccountValidationUseCaseImpl implements CreateAccountValidationUseCase {
    private final int LOCATION_LENGTH_LIMIT = 64;
    private final int NAME_LENGTH_LIMIT = 20;
    private final int AGE_MINIMUM = 13;

    private boolean checkIsAllLetters(String text) {
        for (char ch : text.toCharArray()) {
            if (!Character.isLetter(ch)) {
                return true;
            }
        }
        return false;
    }

    @Override
    @NonNull
    public CreateAccountEntrys invoke(CreateAccountEntrys state) {
        ErrorOutcomes firstNameError;
        ErrorOutcomes lastNameError;
        ErrorOutcomes emailError;
        ErrorOutcomes locationError;
        ErrorOutcomes birthError;
        if (state.getFirstName().getText().length() > NAME_LENGTH_LIMIT) {
            firstNameError = ErrorOutcomes.LENGTH;
        } else if (checkIsAllLetters(state.getFirstName().getText())) {
            firstNameError = ErrorOutcomes.INVALID_CHARACTERS;
        } else {
            firstNameError = ErrorOutcomes.VALID;
        }
        if (state.getLastName().getText().length() > NAME_LENGTH_LIMIT) {
            lastNameError = ErrorOutcomes.LENGTH;
        } else if (checkIsAllLetters(state.getLastName().getText())) {
            lastNameError = ErrorOutcomes.INVALID_CHARACTERS;
        } else {
            lastNameError = ErrorOutcomes.VALID;
        }
        if (state.getEmail().getText().length() > GlobalConstants.EMAIL_LENGTH_LIMIT) {
            emailError = ErrorOutcomes.LENGTH;
        } else if (GeneralUtilityFunctions.validateEmail(state.getEmail().getText())) {
            emailError = ErrorOutcomes.INVALID_FORMAT;
        } else {
            emailError = ErrorOutcomes.VALID;
        }
        if (state.getLocation().getText().length() > LOCATION_LENGTH_LIMIT) {
            locationError = ErrorOutcomes.LENGTH;
        } else {
            locationError = ErrorOutcomes.VALID;
        }
        if (!state.getBirthDay().getText().isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            LocalDateTime minDate = LocalDateTime.of(currentYear - AGE_MINIMUM, currentMonth, currentDay, 0, 0);
            LocalDate selectedDate = LocalDate.parse(state.getBirthDay().getText(), DateTimeFormatter.ofPattern("MM-dd-yyyy"));
            if (selectedDate.isAfter(minDate.toLocalDate())) {
                birthError = ErrorOutcomes.TOO_YOUNG;
            } else {
                birthError = ErrorOutcomes.VALID;
            }
        } else {
            birthError = ErrorOutcomes.NONE;
        }
        return new CreateAccountEntrys(
                new Entry(state.getFirstName().getText(), firstNameError),
                new Entry(state.getLastName().getText(), lastNameError),
                new Entry(state.getBirthDay().getText(), birthError),
                new Entry(state.getEmail().getText(), emailError),
                new Entry(state.getLocation().getText(), locationError)
        );
    }
}
