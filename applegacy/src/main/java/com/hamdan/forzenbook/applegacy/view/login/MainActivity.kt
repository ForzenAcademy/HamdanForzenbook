package com.hamdan.forzenbook.applegacy.view.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.hamdan.forzenbook.applegacy.R
import com.hamdan.forzenbook.applegacy.viewmodels.LegacyCreateAccountViewModel
import com.hamdan.forzenbook.applegacy.viewmodels.LegacyLoginViewModel
import dagger.hilt.android.AndroidEntryPoint

//TODO REMOVE THIS ACTIVITY IN FA-104 OR FA-102
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginModel: LegacyLoginViewModel by viewModels()
    private val createAccountViewModel: LegacyCreateAccountViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO WHEN WORKING ON RESPECTIVE TICKETS FA-100 and 102 respectively
        loginModel.test()
        createAccountViewModel.test()
    }
}
