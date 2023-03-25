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
import com.hamdan.forzenbook.core.EntryError
import com.hamdan.forzenbook.legacy.core.view.utils.DialogUtils
import com.hamdan.forzenbook.legacy.core.view.utils.KeyboardUtils
import com.hamdan.forzenbook.legacy.core.viewmodels.LegacyLoginFragmentViewModel
import com.hamdan.forzenbook.login.core.viewmodel.BaseLoginViewModel
import com.hamdan.forzenbook.login.core.viewmodel.getContent
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
            var loginSubmittable = false
            loginSubmitButton.isEnabled = false
            val context = requireContext()
            loginClickBlocker.setOnClickListener { } // only has an on click to actually block clicks

            loginSubmitButton.setOnClickListener {
                loginModel.loginClicked(context)
            }

            loginCreateAccountLink.setOnClickListener {
                loginModel.createAccountLinkPressed(parentFragmentManager)
            }

            inputEmailText.setOnEditorActionListener { _, action, _ ->
                return@setOnEditorActionListener if (action == EditorInfo.IME_ACTION_DONE) {
                    KeyboardUtils.hideKeyboard(context, inputEmailText)
                    loginModel.loginClicked(context)
                    true
                } else false
            }

            inputCodeText.setOnEditorActionListener { _, action, _ ->
                if (action == EditorInfo.IME_ACTION_DONE) {
                    if (loginSubmittable) {
                        loginModel.loginClicked(context)
                        KeyboardUtils.hideKeyboard(context, inputCodeText)
                        return@setOnEditorActionListener true
                    }
                }
                return@setOnEditorActionListener false
            }

            loginModel.checkLoggedIn(context)

            loginModel.viewModelScope.launch(Dispatchers.IO) {
                loginModel.state.collect { state ->
                    withContext(Dispatchers.Main) {
                        when (state) {
                            is BaseLoginViewModel.LoginState.Content -> {
                                loginClickBlocker.isVisible = false
                                loginSubmitText.isVisible = true
                                loginSubmitProgressIndicator.isVisible = false

                                if (state.content is BaseLoginViewModel.LoginContent.Email) {
                                    (state.content as BaseLoginViewModel.LoginContent.Email).let { stateContent ->
                                        codeErrorText.isVisible = false
                                        inputCodeBody.isVisible = false

                                        loginSubmittable = stateContent.email.error.isValid()
                                        loginSubmitButton.isEnabled = loginSubmittable

                                        emailErrorText.apply {
                                            if (!stateContent.email.error.isValid() && stateContent.email.text.isNotEmpty()) {
                                                if (stateContent.email.error == EntryError.EmailError.Length) {
                                                    text =
                                                        getString(R.string.login_email_error_length)
                                                    isVisible = true
                                                } else if (stateContent.email.error == EntryError.EmailError.InvalidFormat) {
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
                                    (state.content as BaseLoginViewModel.LoginContent.Code).let { stateContent ->
                                        inputEmailBody.isVisible = false
                                        emailErrorText.isVisible = false
                                        loginSubmittable = stateContent.code.error.isValid()
                                        loginSubmitButton.isEnabled = loginSubmittable

                                        inputCodeBody.isVisible = true
                                        codeErrorText.isVisible =
                                            if (stateContent.code.text.isNotEmpty() && !stateContent.code.error.isValid())
                                                stateContent.code.error == EntryError.CodeError.Length
                                            else false

                                        if (stateContent.showInfoDialog) {
                                            DialogUtils.fragmentAlertDialog(
                                                getString(R.string.login_info_title),
                                                getString(R.string.login_info),
                                                getString(R.string.generic_dialog_confirm)
                                            ) {
                                                loginModel.loginDismissInfoClicked()
                                            }.show(parentFragmentManager, null)
                                        }
                                    }
                                }
                            }
                            is BaseLoginViewModel.LoginState.Error -> {
                                loginClickBlocker.isVisible = false
                                loginSubmitText.isVisible = true
                                loginSubmitProgressIndicator.isVisible = false

                                DialogUtils.fragmentAlertDialog(
                                    getString(R.string.login_error_title),
                                    getString(R.string.login_error),
                                    getString(R.string.generic_dialog_confirm)
                                ) {
                                    loginModel.loginDismissErrorClicked()
                                }.show(parentFragmentManager, null)
                            }
                            is BaseLoginViewModel.LoginState.Loading -> {
                                loginClickBlocker.isVisible = true
                                loginSubmitText.isVisible = false
                                loginSubmitProgressIndicator.isVisible = true
                            }
                            BaseLoginViewModel.LoginState.LoggedIn -> {
                                loginModel.login()
                                return@withContext
                            }
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
