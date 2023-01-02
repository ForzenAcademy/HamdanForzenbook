package com.example.forzenbook

import android.util.Log
import com.example.forzenbook.view.composeutils.validateEmail
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        Log.v("Hamdan", validateEmail("BMR123@Gmail.com").toString())
    }
}