package com.hamdan.forzenbook.legacy.createaccount

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.LoginError
import com.hamdan.forzenbook.legacy.core.view.utils.DialogUtils.datePickerDialog
import com.hamdan.forzenbook.legacy.core.viewmodels.LegacyCreateAccountViewModel
import com.hamdan.forzenbook.legacy.createaccount.databinding.ActivityLegacyCreateAccountBinding
import com.hamdan.forzenbook.ui.core.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO add IME actions in FA-104
// TODO in FA-104 add a navigator call to go to login as the account create success lambda in the viewmodel
@AndroidEntryPoint
class LegacyCreateAccountActivity : ComponentActivity() {
    private val createAccountModel: LegacyCreateAccountViewModel by viewModels()
    private lateinit var binding: ActivityLegacyCreateAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLegacyCreateAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.apply {

            var firstNameValue = ""
            var lastNameValue = ""
            var birthDateValue = ""
            var emailValue = ""
            var locationValue = ""

            createAccountSubmitButton.isEnabled = false
            createAccountClickBlocker.setOnClickListener { }

            createAccountSubmitButton.setOnClickListener { createAccountModel.createAccount() }

            createAccountModel.viewModelScope.launch(Dispatchers.IO) {
                createAccountModel.state.collect { state ->
                    withContext(Dispatchers.Main) {
                        state.apply {
                            firstNameValue = firstName.text
                            lastNameValue = lastName.text
                            birthDateValue = birthDay.text
                            emailValue = email.text
                            locationValue = location.text

                            if (isLoading) {
                                createAccountClickBlocker.isVisible = true
                                createAccountSubmitText.isVisible = false
                                createAccountSubmitProgressIndicator.isVisible = true
                            } else {
                                createAccountClickBlocker.isVisible = false
                                createAccountSubmitText.isVisible = true
                                createAccountSubmitProgressIndicator.isVisible = false
                            }

                            if (isDateDialogOpen) {
                                datePickerDialog(
                                    context = this@LegacyCreateAccountActivity,
                                    birthDate = state.birthDay.text,
                                    onTextChange = {
                                        inputBirthDateText.setText(it)
                                        createAccountModel.updateCreateAccountTextAndErrors(
                                            firstName = this.firstName.copy(text = firstNameValue),
                                            lastName = this.firstName.copy(text = lastNameValue),
                                            birthDay = this.firstName.copy(text = it),
                                            email = this.firstName.copy(text = emailValue),
                                            location = this.firstName.copy(text = locationValue),
                                        )
                                    },
                                    onDateSubmission = {
                                        createAccountModel.createAccountDateDialogSubmitClicked()
                                    },
                                    onDateDismiss = {
                                        createAccountModel.createAccountDateDialogDismiss()
                                    }
                                )
                            }

                            createAccountSubmitButton.isEnabled =
                                (email.error.isValid() && birthDay.error.isValid() && location.error.isValid() && lastName.error.isValid() && firstName.error.isValid())

                            firstNameErrorText.apply {
                                if (firstName.text.isNotEmpty() && !firstName.error.isValid()) {
                                    if (firstName.error == LoginError.NameError.Length) {
                                        text =
                                            getString(R.string.create_account_first_name_error_length)
                                        isVisible = true
                                    } else if (firstName.error == LoginError.NameError.InvalidCharacters) {
                                        text =
                                            getString(R.string.create_account_first_name_error_invalid_characters)
                                        isVisible = true
                                    }
                                } else {
                                    isVisible = false
                                }
                            }

                            lastNameErrorText.apply {
                                if (lastName.text.isNotEmpty() && !lastName.error.isValid()) {
                                    if (lastName.error == LoginError.NameError.Length) {
                                        text =
                                            getString(R.string.create_account_last_name_error_length)
                                        isVisible = true
                                    } else if (lastName.error == LoginError.NameError.InvalidCharacters) {
                                        text =
                                            getString(R.string.create_account_last_name_error_invalid_characters)
                                        isVisible = true
                                    }
                                } else {
                                    isVisible = false
                                }
                            }

                            birthDateErrorText.isVisible =
                                birthDay.error == LoginError.BirthDateError.TooYoung && birthDay.text.isNotEmpty()

                            emailErrorText.apply {
                                if (!email.error.isValid() && email.text.isNotEmpty()) {
                                    if (email.error == LoginError.EmailError.Length) {
                                        text = getString(R.string.login_email_error_length)
                                        isVisible = true
                                    } else if (email.error == LoginError.EmailError.InvalidFormat) {
                                        text = getString(R.string.login_email_error_format)
                                        isVisible = true
                                    }
                                } else {
                                    isVisible = false
                                }
                            }

                            locationErrorText.isVisible =
                                location.error == LoginError.LocationError.Length
                        }
                    }
                }
            }
            createAccountModel.state.value.apply {
                inputFirstNameText.addTextChangedListener {
                    createAccountModel.updateCreateAccountTextAndErrors(
                        firstName = this.firstName.copy(text = it.toString()),
                        lastName = this.firstName.copy(text = lastNameValue),
                        birthDay = this.firstName.copy(text = birthDateValue),
                        email = this.firstName.copy(text = emailValue),
                        location = this.firstName.copy(text = locationValue),
                    )
                }
                inputLastNameText.addTextChangedListener {
                    createAccountModel.updateCreateAccountTextAndErrors(
                        firstName = this.firstName.copy(text = firstNameValue),
                        lastName = this.firstName.copy(text = it.toString()),
                        birthDay = this.firstName.copy(text = birthDateValue),
                        email = this.firstName.copy(text = emailValue),
                        location = this.firstName.copy(text = locationValue),
                    )
                }
                inputEmailText.addTextChangedListener {
                    createAccountModel.updateCreateAccountTextAndErrors(
                        firstName = this.firstName.copy(text = firstNameValue),
                        lastName = this.firstName.copy(text = lastNameValue),
                        birthDay = this.firstName.copy(text = birthDateValue),
                        email = this.firstName.copy(text = it.toString()),
                        location = this.firstName.copy(text = locationValue),
                    )
                }
                inputLocationText.addTextChangedListener {
                    createAccountModel.updateCreateAccountTextAndErrors(
                        firstName = this.firstName.copy(text = firstNameValue),
                        lastName = this.firstName.copy(text = lastNameValue),
                        birthDay = this.firstName.copy(text = birthDateValue),
                        email = this.firstName.copy(text = emailValue),
                        location = this.firstName.copy(text = it.toString()),
                    )
                }
                inputBirthDateText.setOnClickListener {
                    createAccountModel.createAccountDateDialogClicked()
                }
            }
        }
    }
}
