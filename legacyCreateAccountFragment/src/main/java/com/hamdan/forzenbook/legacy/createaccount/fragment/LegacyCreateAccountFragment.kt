package com.hamdan.forzenbook.legacy.createaccount.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.EntryError
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.createaccount.core.viewmodel.BaseCreateAccountViewModel
import com.hamdan.forzenbook.createaccount.core.viewmodel.getContent
import com.hamdan.forzenbook.legacy.core.view.utils.DialogUtils
import com.hamdan.forzenbook.legacy.core.viewmodels.LegacyCreateAccountFragmentViewModel
import com.hamdan.forzenbook.ui.core.R
import com.hamdan.forzenbook.ui.core.databinding.ActivityLegacyCreateAccountBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LegacyCreateAccountFragment : Fragment() {
    private val createAccountModel: LegacyCreateAccountFragmentViewModel by activityViewModels()
    private lateinit var binding: ActivityLegacyCreateAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityLegacyCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                createAccountModel.createAccountButtonClicked(parentFragmentManager)
            }
            createAccountLayoutToolBar.setNavigationOnClickListener {
                createAccountModel.backIconPressed(parentFragmentManager)
            }

            inputLocationText.setOnEditorActionListener { _, action, _ ->
                if (action == EditorInfo.IME_ACTION_DONE) {
                    if (submitable) {
                        createAccountModel.createAccountButtonClicked(parentFragmentManager)
                        return@setOnEditorActionListener true
                    }
                }
                return@setOnEditorActionListener false
            }

            createAccountModel.viewModelScope.launch(Dispatchers.IO) {
                createAccountModel.state.collect { state ->
                    withContext(Dispatchers.Main) {
                        when (state) {
                            BaseCreateAccountViewModel.CreateAccountState.AccountCreated -> {
                                createAccountModel.navigateUpPressed()
                            }
                            is BaseCreateAccountViewModel.CreateAccountState.Content -> {
                                createAccountClickBlocker.isVisible = false
                                createAccountSubmitText.isVisible = true
                                createAccountSubmitProgressIndicator.isVisible = false

                                state.createAccountData.let { stateContent ->
                                    firstNameValue = stateContent.firstName.text
                                    lastNameValue = stateContent.lastName.text
                                    birthDateValue = stateContent.birthDay.text
                                    emailValue = stateContent.email.text
                                    locationValue = stateContent.location.text

                                    submitable =
                                        (stateContent.email.error.isValid() && stateContent.birthDay.error.isValid() && stateContent.location.error.isValid() && stateContent.lastName.error.isValid() && stateContent.firstName.error.isValid())

                                    createAccountSubmitButton.isEnabled = submitable
                                    if (stateContent.isDateDialogOpen) {
                                        DialogUtils.fragmentDatePicker(
                                            birthDate = stateContent.birthDay.text,
                                            onTextChange = {
                                                inputBirthDateText.setText(it)
                                                createAccountModel.updateCreateAccountTextAndErrors(
                                                    firstName = stateContent.firstName.copy(text = firstNameValue),
                                                    lastName = stateContent.firstName.copy(text = lastNameValue),
                                                    birthDay = stateContent.firstName.copy(text = it),
                                                    email = stateContent.firstName.copy(text = emailValue),
                                                    location = stateContent.firstName.copy(text = locationValue),
                                                )
                                            },
                                            onDateSubmission = {
                                                createAccountModel.createAccountDateDialogSubmitClicked()
                                            },
                                            onDateDismiss = {
                                                createAccountModel.createAccountDateDialogDismiss()
                                            }
                                        ).show(parentFragmentManager, null)
                                    }

                                    firstNameErrorText.apply {
                                        if (stateContent.firstName.text.isNotEmpty() && !stateContent.firstName.error.isValid()) {
                                            if (stateContent.firstName.error == EntryError.NameError.Length) {
                                                text =
                                                    getString(R.string.create_account_first_name_error_length)
                                                isVisible = true
                                            } else if (stateContent.firstName.error == EntryError.NameError.InvalidCharacters) {
                                                text =
                                                    getString(R.string.create_account_first_name_error_invalid_characters)
                                                isVisible = true
                                            }
                                        } else {
                                            isVisible = false
                                        }
                                    }

                                    lastNameErrorText.apply {
                                        if (stateContent.lastName.text.isNotEmpty() && !stateContent.lastName.error.isValid()) {
                                            if (stateContent.lastName.error == EntryError.NameError.Length) {
                                                text =
                                                    getString(R.string.create_account_last_name_error_length)
                                                isVisible = true
                                            } else if (stateContent.lastName.error == EntryError.NameError.InvalidCharacters) {
                                                text =
                                                    getString(R.string.create_account_last_name_error_invalid_characters)
                                                isVisible = true
                                            }
                                        } else {
                                            isVisible = false
                                        }
                                    }

                                    birthDateErrorText.isVisible =
                                        stateContent.birthDay.error == EntryError.BirthDateError.TooYoung && stateContent.birthDay.text.isNotEmpty()

                                    emailErrorText.apply {
                                        if (!stateContent.email.error.isValid() && stateContent.email.text.isNotEmpty()) {
                                            if (stateContent.email.error == EntryError.EmailError.Length) {
                                                text = getString(R.string.login_email_error_length)
                                                isVisible = true
                                            } else if (stateContent.email.error == EntryError.EmailError.InvalidFormat) {
                                                text = getString(R.string.login_email_error_format)
                                                isVisible = true
                                            }
                                        } else {
                                            isVisible = false
                                        }
                                    }

                                    locationErrorText.isVisible =
                                        stateContent.location.error == EntryError.LocationError.Length
                                }
                            }
                            is BaseCreateAccountViewModel.CreateAccountState.Error -> {
                                createAccountClickBlocker.isVisible = false
                                createAccountSubmitText.isVisible = true
                                createAccountSubmitProgressIndicator.isVisible = false

                                DialogUtils.fragmentAlertDialog(
                                    title = getString(R.string.create_account_error_title),
                                    body = getString(state.errorId),
                                    buttonText = getString(R.string.generic_dialog_confirm),
                                    onDismiss = { createAccountModel.createAccountDismissErrorClicked() }
                                ).show(parentFragmentManager, null)
                            }
                            BaseCreateAccountViewModel.CreateAccountState.Loading -> {
                                createAccountClickBlocker.isVisible = true
                                createAccountSubmitText.isVisible = false
                                createAccountSubmitProgressIndicator.isVisible = true
                            }
                            else -> {
                                throw StateException()
                            }
                        }
                    }
                }
            }
            createAccountModel.state.value.getContent().createAccountData.apply {
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
