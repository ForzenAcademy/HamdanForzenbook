package com.hamdan.forzenbook.java.legacy.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.hamdan.forzenbook.java.createaccount.core.viewmodel.JavaCreateAccountViewModel;
import com.hamdan.forzenbook.java.login.core.viewmodel.JavaLoginViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private JavaLoginViewModel loginTest;
    private JavaCreateAccountViewModel createTest;

    // TODO in FA-114 / 116 remove this activity and these viewmodels from it

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginTest = new ViewModelProvider(this).get(JavaLoginViewModel.class);
        createTest = new ViewModelProvider(this).get(JavaCreateAccountViewModel.class);
    }
}