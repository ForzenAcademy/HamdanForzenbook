package com.hamdan.forzenbook.legacy.search

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.legacy.core.view.utils.DialogUtils
import com.hamdan.forzenbook.legacy.core.viewmodels.LegacySearchViewModel
import com.hamdan.forzenbook.search.core.viewmodel.BaseSearchViewModel
import com.hamdan.forzenbook.ui.core.R
import com.hamdan.forzenbook.ui.core.databinding.ActivityLegacySearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LegacySearchActivity : ComponentActivity() {
    private lateinit var binding: ActivityLegacySearchBinding
    private val searchViewModel: LegacySearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLegacySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {

            searchViewModel.viewModelScope.launch(Dispatchers.IO) {
                searchViewModel.state.collect { state ->
                    when (state) {
                        BaseSearchViewModel.SearchState.Error -> {
                            inputSearchText.setText("")
                            DialogUtils.standardAlertDialog(
                                this@LegacySearchActivity,
                                getString(R.string.generic_error_title),
                                getString(R.string.search_error_body),
                                getString(R.string.generic_dialog_confirm)
                            ) {
                                searchViewModel.onErrorDismiss()
                            }
                        }

                        BaseSearchViewModel.SearchState.InvalidLogin -> {
                            searchViewModel.kickToLogin(this@LegacySearchActivity)
                        }

                        is BaseSearchViewModel.SearchState.Searching -> {
                        }
                    }
                }
            }

            inputSearchText.setOnEditorActionListener { _, action, _ ->
                if (action == EditorInfo.IME_ACTION_DONE) {
                    searchViewModel.onSearchSubmit(
                        onSuccess = {
                            searchViewModel.sendToResultSuccessQuery(this@LegacySearchActivity)
                        },
                        onError = {
                            searchViewModel.sendToResultFailureQuery(this@LegacySearchActivity)
                        },
                    )
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            inputSearchText.addTextChangedListener {
                if (searchViewModel.state.value is BaseSearchViewModel.SearchState.Searching) {
                    searchViewModel.onUpdateSearch(it.toString())
                }
            }
        }
    }
}
