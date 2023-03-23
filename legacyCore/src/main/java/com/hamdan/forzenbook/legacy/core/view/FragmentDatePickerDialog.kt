package com.hamdan.forzenbook.legacy.core.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.hamdan.forzenbook.core.stringDate
import com.hamdan.forzenbook.ui.core.R
import java.util.Calendar

class FragmentDatePickerDialog(
    val birthDate: String,
    val onTextChange: (String) -> Unit,
    val onDateSubmission: () -> Unit,
    val onDateDismiss: () -> Unit
) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
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
        val dialog = DatePickerDialog(
            requireContext(),
            R.style.MySpinnerDatePickerStyle,
            this,
            selectedYear,
            selectedMonth,
            selectedDay
        )
        // Create a new instance of DatePickerDialog and return it
        return dialog
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        val birthDay = stringDate(month + 1, day, year, requireContext())
        onTextChange(birthDay)
        onDateSubmission()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDateDismiss()
    }
}
