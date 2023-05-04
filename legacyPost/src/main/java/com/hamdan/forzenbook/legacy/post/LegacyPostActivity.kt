package com.hamdan.forzenbook.legacy.post

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.hamdan.forzenbook.ui.core.R
import com.hamdan.forzenbook.ui.core.databinding.ActivityLegacyPostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LegacyPostActivity : ComponentActivity() {
    private lateinit var binding: ActivityLegacyPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLegacyPostBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        binding.postLayoutToolBar.inflateMenu(R.menu.post_menu)
        binding.postLayoutToolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.postSendIcon -> {
                    Log.v("Hamdan", "CLICKED IT")
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
}
