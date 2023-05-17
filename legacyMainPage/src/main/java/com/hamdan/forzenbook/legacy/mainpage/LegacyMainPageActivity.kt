package com.hamdan.forzenbook.legacy.mainpage

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.hamdan.forzenbook.ui.core.R
import com.hamdan.forzenbook.ui.core.databinding.ActivityLegacyFeedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LegacyMainPageActivity : ComponentActivity() {
    private lateinit var binding: ActivityLegacyFeedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLegacyFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.apply {
            feedLayoutToolBar.inflateMenu(R.menu.feed_menu)
            feedLayoutToolBar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.feedNewPost) {
                    true
                } else {
                    false
                }
            }
        }
    }
}
