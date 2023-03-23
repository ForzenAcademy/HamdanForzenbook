package com.hamdan.forzenbook.legacy.fragment.app.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hamdan.forzenbook.legacy.fragment.app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LegacyFragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legacy_fragment)
    }
}
