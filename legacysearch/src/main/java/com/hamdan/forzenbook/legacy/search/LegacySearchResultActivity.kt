package com.hamdan.forzenbook.legacy.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hamdan.forzenbook.core.GlobalConstants
import com.hamdan.forzenbook.legacy.core.view.utils.DialogUtils
import com.hamdan.forzenbook.legacy.core.viewmodels.LegacySearchResultViewModel
import com.hamdan.forzenbook.search.core.viewmodel.BaseSearchResultViewModel
import com.hamdan.forzenbook.ui.core.R
import com.hamdan.forzenbook.ui.core.databinding.ActivityLegacySearchResultBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LegacySearchResultActivity : ComponentActivity() {
    private lateinit var binding: ActivityLegacySearchResultBinding
    private val searchResultViewModel: LegacySearchResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLegacySearchResultBinding.inflate(layoutInflater)
        val view = binding.root
        val context = this@LegacySearchResultActivity
        setContentView(view)
        val extras = intent.extras
        val query =
            extras?.getString(INTENT_KEY_BASE + GlobalConstants.NAVIGATION_QUERY)
        val userId =
            extras?.getInt(INTENT_KEY_BASE + GlobalConstants.NAVIGATION_USERID)
        val pageError = extras?.getBoolean(INTENT_KEY_BASE + GlobalConstants.NAVIGATION_ERROR)

        pageError?.let {
            if (!pageError) {
                if (userId == null || userId == -1) {
                    if (!query.isNullOrEmpty()) {
                        searchResultViewModel.onReceiveQuery(
                            query,
                        )
                    } else {
                        searchResultViewModel.errorDuringSearch()
                    }
                } else {
                    searchResultViewModel.onReceiveId(
                        userId,
                    )
                }
            } else {
                searchResultViewModel.errorDuringSearch()
            }
        }

        binding.apply {

            searchResultLayoutToolBar.setNavigationOnClickListener {
                finish()
            }

            searchResultPostsRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = searchResultViewModel.adapter
            }
            searchResultHomeButton.setOnClickListener {
                searchResultViewModel.onHomeClicked(context)
            }
            searchResultSearchButton.setOnClickListener {
                searchResultViewModel.onSearchClicked(context)
            }

            searchResultViewModel.viewModelScope.launch(Dispatchers.IO) {
                searchResultViewModel.state.collect { state ->
                    withContext(Dispatchers.Main) {
                        when (state) {
                            is BaseSearchResultViewModel.SearchResultState.Content -> {
                                // nothing happens, no real actions need to be taken
                                // as the vm and adapter handle pretty much everything in this specific case
                                binding.searchResultPostsRecycler.isVisible = true
                            }

                            is BaseSearchResultViewModel.SearchResultState.Error -> {
                                binding.searchResultPostsRecycler.isVisible = false
                                DialogUtils.standardAlertDialog(
                                    context,
                                    getString(R.string.login_error_title),
                                    getString(R.string.feed_error_body),
                                    getString(R.string.generic_dialog_confirm)
                                ) {
                                    searchResultViewModel.onErrorDismiss()
                                }
                            }

                            is BaseSearchResultViewModel.SearchResultState.InvalidLogin -> {
                                binding.searchResultPostsRecycler.isVisible = false
                                searchResultViewModel.legacyKickToLogin(context)
                            }

                            is BaseSearchResultViewModel.SearchResultState.Loading -> {
                                binding.searchResultPostsRecycler.isVisible = false
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val INTENT_KEY_BASE: String = "com.hamdan.forzenbook.legacy.search."
    }
}
