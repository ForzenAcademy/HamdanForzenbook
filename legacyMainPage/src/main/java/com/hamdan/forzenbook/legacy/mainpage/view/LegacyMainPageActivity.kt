package com.hamdan.forzenbook.legacy.mainpage.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hamdan.forzenbook.legacy.core.view.utils.DialogUtils
import com.hamdan.forzenbook.legacy.core.viewmodels.LegacyFeedViewModel
import com.hamdan.forzenbook.mainpage.core.viewmodel.BaseFeedViewModel
import com.hamdan.forzenbook.ui.core.R
import com.hamdan.forzenbook.ui.core.databinding.ActivityLegacyFeedBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LegacyMainPageActivity : ComponentActivity() {

    private lateinit var binding: ActivityLegacyFeedBinding
    private val feedViewModel: LegacyFeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLegacyFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.apply {
            feedLayoutToolBar.inflateMenu(R.menu.feed_menu)
            feedLayoutToolBar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.feedNewPost) {
                    feedViewModel.newPostClicked(this@LegacyMainPageActivity)
                    true
                } else {
                    false
                }
            }

            feedPostsRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = feedViewModel.adapter
            }
            feedHomeButton.setOnClickListener {
                // nothing needs to happen as this is the page it would go to
            }
            feedSearchButton.setOnClickListener {
                feedViewModel.onSearchClicked(this@LegacyMainPageActivity)
            }

            feedViewModel.viewModelScope.launch(Dispatchers.IO) {
                feedViewModel.state.collect { state ->
                    withContext(Dispatchers.Main) {
                        when (state) {
                            is BaseFeedViewModel.FeedState.Content -> {
                                // nothing happens, no real actions need to be taken
                                // as the vm and adapter handle pretty much everything in this specific case
                                binding.feedPostsRecycler.isVisible = true
                            }

                            is BaseFeedViewModel.FeedState.Error -> {
                                binding.feedPostsRecycler.isVisible = false
                                DialogUtils.standardAlertDialog(
                                    this@LegacyMainPageActivity,
                                    getString(R.string.login_error_title),
                                    getString(R.string.feed_error_body),
                                    getString(R.string.generic_dialog_confirm)
                                ) {
                                    feedViewModel.onErrorDismiss()
                                }
                            }

                            is BaseFeedViewModel.FeedState.InvalidLogin -> {
                                binding.feedPostsRecycler.isVisible = false
                                feedViewModel.legacyKickToLogin(this@LegacyMainPageActivity)
                            }
                        }
                    }
                }
            }
        }
    }
}
