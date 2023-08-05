package com.hamdan.forzenbook.legacy.createaccount

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.EntryError
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.core.datePickerDialog
import com.hamdan.forzenbook.createaccount.core.viewmodel.BaseCreateAccountViewModel
import com.hamdan.forzenbook.createaccount.core.viewmodel.getContent
import com.hamdan.forzenbook.legacy.core.view.utils.DialogUtils.standardAlertDialog
import com.hamdan.forzenbook.legacy.core.viewmodels.LegacyCreateAccountViewModel
import com.hamdan.forzenbook.ui.core.R
import com.hamdan.forzenbook.ui.core.databinding.ActivityLegacyCreateAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            var submitable = false

            createAccountSubmitButton.isEnabled = false
            createAccountClickBlocker.setOnClickListener { }

            createAccountSubmitButton.setOnClickListener {
                createAccountModel.createAccountButtonClicked(
                    this@LegacyCreateAccountActivity
                )
            }
            createAccountLayoutToolBar.setNavigationOnClickListener {
                finish()
            }

            inputLocationText.setOnEditorActionListener { _, action, _ ->
                if (action == EditorInfo.IME_ACTION_DONE) {
                    if (submitable) {
                        createAccountModel.createAccountButtonClicked(this@LegacyCreateAccountActivity)
                        return@setOnEditorActionListener true
                    }
                }
                return@setOnEditorActionListener false
            }

            createAccountModel.viewModelScope.launch(Dispatchers.IO) {
                createAccountModel.state.collect { state ->
                    withContext(Dispatchers.Main) {
                        state.apply {
                            when (state) {
                                BaseCreateAccountViewModel.CreateAccountState.AccountCreated -> {
                                    createAccountModel.navigateUpPressed()
                                }

                                is BaseCreateAccountViewModel.CreateAccountState.Content -> {
                                    createAccountClickBlocker.isVisible = false
                                    createAccountSubmitText.isVisible = true
                                    createAccountSubmitProgressIndicator.isVisible = false
                                    state.createAccountContent.let { accountContent ->
                                        firstNameValue = accountContent.firstName.text
                                        lastNameValue = accountContent.lastName.text
                                        birthDateValue = accountContent.birthDay.text
                                        emailValue = accountContent.email.text
                                        locationValue = accountContent.location.text
                                        submitable =
                                            (accountContent.email.error.isValid() && accountContent.birthDay.error.isValid() && accountContent.location.error.isValid() && accountContent.lastName.error.isValid() && accountContent.firstName.error.isValid())
                                        if (accountContent.isDateDialogOpen) {
                                            datePickerDialog(
                                                context = this@LegacyCreateAccountActivity,
                                                birthDate = accountContent.birthDay.text,
                                                onTextChange = {
                                                    inputBirthDateText.setText(it)
                                                    createAccountModel.updateCreateAccountTextAndErrors(
                                                        firstName = accountContent.firstName.copy(
                                                            text = firstNameValue
                                                        ),
                                                        lastName = accountContent.firstName.copy(
                                                            text = lastNameValue
                                                        ),
                                                        birthDay = accountContent.firstName.copy(
                                                            text = it
                                                        ),
                                                        email = accountContent.firstName.copy(text = emailValue),
                                                        location = accountContent.firstName.copy(
                                                            text = locationValue
                                                        ),
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
                                        createAccountSubmitButton.isEnabled = submitable
                                        firstNameErrorText.apply {
                                            if (accountContent.firstName.text.isNotEmpty() && !accountContent.firstName.error.isValid()) {
                                                if (accountContent.firstName.error == EntryError.NameError.Length) {
                                                    text =
                                                        getString(R.string.create_account_first_name_error_length)
                                                    isVisible = true
                                                } else if (accountContent.firstName.error == EntryError.NameError.InvalidCharacters) {
                                                    text =
                                                        getString(R.string.create_account_first_name_error_invalid_characters)
                                                    isVisible = true
                                                }
                                            } else {
                                                isVisible = false
                                            }
                                        }

                                        lastNameErrorText.apply {
                                            if (accountContent.lastName.text.isNotEmpty() && !accountContent.lastName.error.isValid()) {
                                                if (accountContent.lastName.error == EntryError.NameError.Length) {
                                                    text =
                                                        getString(R.string.create_account_last_name_error_length)
                                                    isVisible = true
                                                } else if (accountContent.lastName.error == EntryError.NameError.InvalidCharacters) {
                                                    text =
                                                        getString(R.string.create_account_last_name_error_invalid_characters)
                                                    isVisible = true
                                                }
                                            } else {
                                                isVisible = false
                                            }
                                        }

                                        birthDateErrorText.isVisible =
                                            accountContent.birthDay.error == EntryError.BirthDateError.TooYoung && accountContent.birthDay.text.isNotEmpty()

                                        emailErrorText.apply {
                                            if (!accountContent.email.error.isValid() && accountContent.email.text.isNotEmpty()) {
                                                if (accountContent.email.error == EntryError.EmailError.Length) {
                                                    text =
                                                        getString(R.string.login_email_error_length)
                                                    isVisible = true
                                                } else if (accountContent.email.error == EntryError.EmailError.InvalidFormat) {
                                                    text =
                                                        getString(R.string.login_email_error_format)
                                                    isVisible = true
                                                }
                                            } else {
                                                isVisible = false
                                            }
                                        }
                                        locationErrorText.isVisible =
                                            accountContent.location.error == EntryError.LocationError.Length
                                    }
                                }

                                is BaseCreateAccountViewModel.CreateAccountState.Error -> {
                                    createAccountClickBlocker.isVisible = false
                                    createAccountSubmitText.isVisible = true
                                    createAccountSubmitProgressIndicator.isVisible = false
                                    standardAlertDialog(
                                        context = this@LegacyCreateAccountActivity,
                                        title = getString(R.string.create_account_error_title),
                                        body = getString(state.errorId),
                                        buttonText = getString(R.string.generic_dialog_confirm),
                                        onDismiss = { createAccountModel.createAccountDismissErrorClicked() }
                                    )
                                }

                                BaseCreateAccountViewModel.CreateAccountState.Loading -> {
                                    createAccountClickBlocker.isVisible = true
                                    createAccountSubmitText.isVisible = false
                                    createAccountSubmitProgressIndicator.isVisible = true
                                }

                                else -> throw StateException()
                            }
                        }
                    }
                }
            }
            createAccountModel.state.value.getContent().createAccountContent.apply {
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
