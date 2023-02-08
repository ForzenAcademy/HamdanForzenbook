package com.hamdan.forzenbook.createaccount.core.domain

import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.GlobalConstants.EMAIL_LENGTH_LIMIT
import com.hamdan.forzenbook.core.LoginError
import com.hamdan.forzenbook.core.validateEmail
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class CreateAccountValidationUseCaseImpl : CreateAccountValidationUseCase {
    override fun invoke(state: CreateAccountEntrys): CreateAccountEntrys {
        val firstNameError = if (state.firstName.text.length > NAME_LENGTH_LIMIT) {
            LoginError.NameError.Length
        } else if (!state.firstName.text.all { name -> name.isLetter() }) {
            LoginError.NameError.InvalidCharacters
        } else LoginError.NameError.Valid
        val lastNameError = if (state.lastName.text.length > NAME_LENGTH_LIMIT) {
            LoginError.NameError.Length
        } else if (!state.lastName.text.all { name -> name.isLetter() }) {
            LoginError.NameError.InvalidCharacters
        } else LoginError.NameError.Valid
        val emailError = if (state.email.text.length > EMAIL_LENGTH_LIMIT) {
            LoginError.EmailError.Length
        } else if (!validateEmail(state.email.text)) {
            LoginError.EmailError.InvalidFormat
        } else LoginError.EmailError.Valid
        val locationError = if (state.location.text.length > LOCATION_LENGTH_LIMIT) {
            LoginError.LocationError.Length
        } else LoginError.LocationError.Valid
        val birthError = if (state.birthDay.text.isNotEmpty()) {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH) + 1
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
            val minDate =
                LocalDateTime.of(currentYear - AGE_MINIMUM, currentMonth, currentDay, 0, 0)
            val selectedDate =
                LocalDate.parse(state.birthDay.text, DateTimeFormatter.ofPattern("MM-dd-yyyy"))
            if (selectedDate.isAfter(minDate.toLocalDate())) {
                LoginError.BirthDateError.TooYoung
            } else LoginError.BirthDateError.Valid
        } else {
            state.birthDay.error
        }
        return state.copy(
            firstName = Entry(text = state.firstName.text, error = firstNameError),
            lastName = Entry(text = state.lastName.text, error = lastNameError),
            birthDay = Entry(text = state.birthDay.text, error = birthError),
            email = Entry(text = state.email.text, error = emailError),
            location = Entry(text = state.location.text, error = locationError),
        )
    }

    companion object {
        private const val AGE_MINIMUM = 13
        private const val NAME_LENGTH_LIMIT = 20
        private const val LOCATION_LENGTH_LIMIT = 64
    }
}
