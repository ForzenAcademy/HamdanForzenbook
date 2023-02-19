package com.hamdan.forzenbook.applegacy.view.login

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hamdan.forzenbook.applegacy.R
import com.hamdan.forzenbook.applegacy.viewmodels.LegacyCreateAccountViewModel
import com.hamdan.forzenbook.applegacy.viewmodels.LegacyLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// TODO REMOVE THIS ACTIVITY IN FA-104 OR FA-102
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginModel: LegacyLoginViewModel by viewModels()
    private val createAccountViewModel: LegacyCreateAccountViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val testText = findViewById<TextView>(R.id.testText)
        testText.text = loginModel.testState.value.ex.toString()
        val testButton = findViewById<Button>(R.id.testButton).setOnClickListener {
            loginModel.testFunc()
        }
        // TODO WHEN WORKING ON FA-100 and 102 REMOVE
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginModel.testState.collect { state ->
                    testText.text = state.ex.toString()
                }
            }
        }
        // TODO REMOVE WHEN WORKING ON RESPECTIVE TICKETS FA-100 and 102 respectively
        loginModel.test()
    }
}
