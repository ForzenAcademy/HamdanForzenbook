package com.hamdan.forzenbook.java.legacy.app.view;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.hamdan.forzenbook.java.core.NavigatorJava;
import com.hamdan.forzenbook.java.createaccount.JavaCreateAccountActivity;
import com.hamdan.forzenbook.java.login.JavaLoginActivity;

public class NavigatorJavaImpl implements NavigatorJava {
    @Override
    public void navigateToLogin(@NonNull Context context) {
        Intent intent = new Intent(context, JavaLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void navigateToCreateAccount(@NonNull Context context) {
        Intent intent = new Intent(context, JavaCreateAccountActivity.class);
        context.startActivity(intent);
    }
}
