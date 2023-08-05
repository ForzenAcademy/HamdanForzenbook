package com.hamdan.forzenbook.legacy.login

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.EntryError
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.legacy.core.view.utils.DialogUtils.standardAlertDialog
import com.hamdan.forzenbook.legacy.core.view.utils.KeyboardUtils
import com.hamdan.forzenbook.legacy.core.viewmodels.LegacyLoginViewModel
import com.hamdan.forzenbook.login.core.viewmodel.BaseLoginViewModel
import com.hamdan.forzenbook.login.core.viewmodel.getContent
import com.hamdan.forzenbook.ui.core.R
import com.hamdan.forzenbook.ui.core.databinding.ActivityLegacyLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LegacyLoginActivity : ComponentActivity() {

    private lateinit var binding: ActivityLegacyLoginBinding
    private val loginModel: LegacyLoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLegacyLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.apply {
            var loginSubmittable = false
            loginSubmitButton.isEnabled = false
            val context = this@LegacyLoginActivity
            loginClickBlocker.setOnClickListener { } // only has an on click to actually block clicks

            loginSubmitButton.setOnClickListener {
                loginModel.loginClicked()
            }

            loginCreateAccountLink.setOnClickListener {
                loginModel.createAccountLinkPressed(context)
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

            loginModel.checkLoggedIn()

            loginModel.viewModelScope.launch(Dispatchers.IO) {
                loginModel.state.collect { state ->
                    withContext(Dispatchers.Main) {
                        when (state) {
                            is BaseLoginViewModel.LoginState.Content -> {
                                loginClickBlocker.isVisible = false
                                loginSubmitText.isVisible = true
                                loginSubmitProgressIndicator.isVisible = false
                                if (state.content is BaseLoginViewModel.LoginContent.Email) {
                                    (state.content as BaseLoginViewModel.LoginContent.Email).let { loginContent ->
                                        loginSubmittable = loginContent.email.error.isValid()
                                        loginSubmitButton.isEnabled = loginSubmittable

                                        codeErrorText.isVisible = false
                                        inputCodeBody.isVisible = false

                                        emailErrorText.apply {
                                            if (!loginContent.email.error.isValid() && loginContent.email.text.isNotEmpty()) {
                                                if (loginContent.email.error == EntryError.EmailError.Length) {
                                                    text =
                                                        getString(R.string.login_email_error_length)
                                                    isVisible = true
                                                } else if (loginContent.email.error == EntryError.EmailError.InvalidFormat) {
                                                    text =
                                                        getString(R.string.login_email_error_format)
                                                    isVisible = true
                                                }
                                            } else {
                                                isVisible = false
                                            }
                                        }
                                    }
                                } else {
                                    (state.content as BaseLoginViewModel.LoginContent.Code).let { loginContent ->
                                        loginSubmittable = loginContent.code.error.isValid()
                                        loginSubmitButton.isEnabled = loginSubmittable

                                        inputEmailBody.isVisible = false
                                        emailErrorText.isVisible = false
                                        inputCodeBody.isVisible = true
                                        codeErrorText.isVisible =
                                            if (loginContent.code.text.isNotEmpty() && !loginContent.code.error.isValid())
                                                loginContent.code.error == EntryError.CodeError.Length
                                            else false
                                        if (loginContent.showInfoDialog) {
                                            standardAlertDialog(
                                                context,
                                                getString(R.string.login_info_title),
                                                getString(R.string.login_info),
                                                getString(R.string.generic_dialog_confirm)
                                            ) {
                                                loginModel.loginDismissInfoClicked()
                                            }
                                        }
                                    }
                                }
                            }

                            is BaseLoginViewModel.LoginState.Error -> {
                                standardAlertDialog(
                                    context,
                                    getString(R.string.login_error_title),
                                    getString(R.string.login_error),
                                    getString(R.string.generic_dialog_confirm)
                                ) {
                                    loginModel.loginDismissErrorClicked()
                                }
                            }

                            is BaseLoginViewModel.LoginState.Loading -> {
                                loginClickBlocker.isVisible = true
                                loginSubmitText.isVisible = false
                                loginSubmitProgressIndicator.isVisible = true
                            }

                            BaseLoginViewModel.LoginState.LoggedIn -> {
                                loginModel.login(context)
                            }

                            else -> throw StateException()
                        }
                    }
                }
            }
            inputEmailText.addTextChangedListener {
                if (loginModel.state.value.getContent() is BaseLoginViewModel.LoginContent.Email) {
                    loginModel.updateText(
                        entry = (loginModel.state.value.getContent() as BaseLoginViewModel.LoginContent.Email).email.copy(
                            text = it.toString()
                        ),
                    )
                }
            }
            inputCodeText.addTextChangedListener {
                if (loginModel.state.value.getContent() is BaseLoginViewModel.LoginContent.Code) {
                    loginModel.updateText(
                        entry = (loginModel.state.value.getContent() as BaseLoginViewModel.LoginContent.Code).code.copy(
                            text = it.toString()
                        ),
                    )
                }
            }
        }
    }
}
