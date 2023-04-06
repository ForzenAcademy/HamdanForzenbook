package com.hamdan.forzenbook.legacy.login.fragment

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
import com.hamdan.forzenbook.core.LoginError
import com.hamdan.forzenbook.legacy.core.view.utils.DialogUtils
import com.hamdan.forzenbook.legacy.core.view.utils.KeyboardUtils
import com.hamdan.forzenbook.legacy.core.viewmodels.LegacyLoginFragmentViewModel
import com.hamdan.forzenbook.ui.core.R
import com.hamdan.forzenbook.ui.core.databinding.ActivityLegacyLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LegacyLoginFragment : Fragment() {
    private val loginModel: LegacyLoginFragmentViewModel by activityViewModels()
    private lateinit var binding: ActivityLegacyLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityLegacyLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            var emailInputTextValue = ""
            var codeInputTextValue = ""
            var loginSubmittable = false
            loginSubmitButton.isEnabled = false
            val context = requireContext()
            loginClickBlocker.setOnClickListener { } // only has an on click to actually block clicks

            loginSubmitButton.setOnClickListener {
                loginModel.loginClicked()
            }

            loginCreateAccountLink.setOnClickListener {
                loginModel.createAccountLinkPressed(parentFragmentManager)
            }

            inputEmailText.setOnEditorActionListener { _, action, _ ->
                return@setOnEditorActionListener if (action == EditorInfo.IME_ACTION_DONE) {
                    KeyboardUtils.hideKeyboard(context, inputEmailText)
                    loginModel.loginClicked()
                    true
                } else false
            }

            inputCodeText.setOnEditorActionListener { _, action, _ ->
                if (action == EditorInfo.IME_ACTION_DONE) {
                    if (loginSubmittable) {
                        loginModel.loginClicked()
                        KeyboardUtils.hideKeyboard(context, inputCodeText)
                        return@setOnEditorActionListener true
                    }
                }
                return@setOnEditorActionListener false
            }

            loginModel.viewModelScope.launch(Dispatchers.IO) {
                loginModel.state.collect { state ->
                    withContext(Dispatchers.Main) {
                        state.apply {
                            if (this.loggedIn) {
                                loginModel.login()
                                return@withContext
                            }
                            emailInputTextValue = email.text
                            codeInputTextValue = code.text
                            loginSubmittable = email.error.isValid() && code.error.isValid()

                            loginSubmitButton.isEnabled = if (!inputtingCode) {
                                email.error.isValid()
                            } else {
                                email.error.isValid() && code.error.isValid()
                            }

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
                            if (inputtingCode) {
                                inputEmailBody.isVisible = false
                                emailErrorText.isVisible = false
                                inputCodeBody.isVisible = true
                                codeErrorText.isVisible =
                                    if (code.text.isNotEmpty() && !code.error.isValid())
                                        code.error == LoginError.CodeError.Length
                                    else false
                            } else {
                                codeErrorText.isVisible = false
                                inputCodeBody.isVisible = false
                            }

                            if (isLoading) {
                                loginClickBlocker.isVisible = true
                                loginSubmitText.isVisible = false
                                loginSubmitProgressIndicator.isVisible = true
                            } else {
                                loginClickBlocker.isVisible = false
                                loginSubmitText.isVisible = true
                                loginSubmitProgressIndicator.isVisible = false
                                loginSubmitButton.isEnabled = if (!inputtingCode) {
                                    email.error.isValid()
                                } else {
                                    email.error.isValid() && code.error.isValid()
                                }
                            }
                            if (showInfoDialog) {
                                DialogUtils.fragmentAlertDialog(
                                    getString(R.string.login_info_title),
                                    getString(R.string.login_info),
                                    getString(R.string.generic_dialog_confirm)
                                ) {
                                    loginModel.loginDismissInfoClicked()
                                }.show(parentFragmentManager, null)
                            }
                            if (hasError) {
                                DialogUtils.fragmentAlertDialog(
                                    getString(R.string.login_error_title),
                                    getString(R.string.login_error),
                                    getString(R.string.generic_dialog_confirm)
                                ) {
                                    loginModel.loginDismissErrorClicked()
                                }.show(parentFragmentManager, null)
                            }
                        }
                    }
                }
            }
            loginModel.state.value.apply {
                inputEmailText.addTextChangedListener {
                    loginModel.updateLoginTexts(
                        email = this.email.copy(text = it.toString()),
                        code = this.code.copy(text = codeInputTextValue),
                        isInputtingCode = this.inputtingCode
                    )
                }
                inputCodeText.addTextChangedListener {
                    loginModel.updateLoginTexts(
                        email = this.email.copy(text = emailInputTextValue),
                        code = this.code.copy(text = it.toString()),
                        isInputtingCode = this.inputtingCode
                    )
                }
            }
        }
    }
}
