package com.hamdan.forzenbook.legacy.core.view.utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import com.hamdan.forzenbook.core.stringDate
import com.hamdan.forzenbook.ui.core.R
import java.util.Calendar

object DialogUtils {
    fun standardAlertDialog(
        context: Context,
        title: String,
        body: String,
        buttonText: String,
        onDismiss: () -> Unit,
    ): AlertDialog {
        val dialog = AlertDialog.Builder(context).apply {
            setTitle(title)
            setMessage(body)
            setPositiveButton(buttonText) { dialog, _ ->
                dialog.dismiss()
            }
        }.create().apply {
            setOnDismissListener {
                onDismiss()
            }
        }
        dialog.show()
        return dialog
    }

    // TODO After FA-104 Make a cleanup ticket and move this object up to Core, implement a call to this function in createAccountCompose
    fun datePickerDialog(
        context: Context,
        birthDate: String,
        onTextChange: (String) -> Unit,
        onDateSubmission: () -> Unit,
        onDateDismiss: () -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        var selectedYear: Int = currentYear
        var selectedMonth: Int = currentMonth
        var selectedDay: Int = currentDay
        if (birthDate.isNotEmpty()) {
            val split = birthDate.split("-")
            selectedYear = split[2].toInt()
            selectedMonth = split[0].toInt() - 1
            selectedDay = split[1].toInt()
        }
        DatePickerDialog(
            context,
            R.style.MySpinnerDatePickerStyle,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                val birthDay = stringDate(month + 1, day, year, context)
                onTextChange(birthDay)
                onDateSubmission()
            },
            selectedYear,
            selectedMonth,
            selectedDay
        ).apply {
            setOnDismissListener { onDateDismiss() }
        }.show()
    }
}
