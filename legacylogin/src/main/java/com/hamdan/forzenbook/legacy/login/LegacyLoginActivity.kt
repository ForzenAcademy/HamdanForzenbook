package com.hamdan.forzenbook.legacy.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.LoginError
import com.hamdan.forzenbook.legacy.core.view.utils.DialogUtils.standardDialog
import com.hamdan.forzenbook.legacy.core.viewmodels.LegacyLoginViewModel
import com.hamdan.forzenbook.legacy.login.databinding.ActivityLegacyLoginBinding
import com.hamdan.forzenbook.ui.core.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LegacyLoginActivity : ComponentActivity() {
    // TODO FA-104 add a click listener to send to Create Account on the createAccount text
    private lateinit var binding: ActivityLegacyLoginBinding
    private val loginModel: LegacyLoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLegacyLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.apply {
            var emailInputTextValue = ""
            var codeInputTextValue = ""
            loginSubmitButton.isEnabled = false
            loginClickBlocker.setOnClickListener { } // only has an on click to actually block clicks

            emailErrorText.text = getString(R.string.login_email_error_format)
            codeErrorText.text = getString(R.string.login_code_error)

            loginSubmitButton.setOnClickListener {
                loginModel.loginClicked()
            }

            loginModel.viewModelScope.launch(Dispatchers.IO) {
                loginModel.state.collect { state ->
                    withContext(Dispatchers.Main) {
                        state.apply {

                            emailInputTextValue = email.text
                            codeInputTextValue = code.text

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
                                inputCodeBody.isVisible = true
                                codeErrorText.isVisible =
                                    if (code.text.isNotEmpty() && !code.error.isValid()) {
                                        code.error == LoginError.CodeError.Length
                                    } else {
                                        false
                                    }
                            } else {
                                codeErrorText.isVisible = false
                                inputCodeBody.isVisible = false
                            }

                            if (isLoading) {
                                loginClickBlocker.isVisible = true
                                loginSubmitText.isVisible = false
                                loginSubmitProgressIndicator.isVisible =
                                    true
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
                                standardDialog(
                                    this@LegacyLoginActivity,
                                    getString(R.string.login_info_title),
                                    getString(R.string.login_info),
                                    getString(R.string.login_confirm_info)
                                ) {
                                    loginModel.loginDismissInfoClicked()
                                }
                            }

                            if (hasError) {
                                standardDialog(
                                    this@LegacyLoginActivity,
                                    getString(R.string.login_error_title),
                                    getString(R.string.login_error),
                                    getString(R.string.login_confirm_error)
                                ) {
                                    loginModel.loginDismissErrorClicked()
                                }
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
