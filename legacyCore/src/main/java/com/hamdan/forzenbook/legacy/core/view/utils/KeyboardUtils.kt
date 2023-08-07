package com.hamdan.forzenbook.legacy.core.view.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {
    fun hideKeyboard(context: Context, view: View) {
        try {
            val inputManager =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            Log.v("Exception", e.stackTraceToString())
        }
    }
}
