package com.hamdan.forzenbook.legacy.core.view.utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import com.hamdan.forzenbook.core.stringDate
import com.hamdan.forzenbook.legacy.core.view.FragmentAlertDialog
import com.hamdan.forzenbook.legacy.core.view.FragmentDatePickerDialog
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

    fun fragmentAlertDialog(
        title: String,
        body: String,
        buttonText: String,
        onDismiss: () -> Unit,
    ): FragmentAlertDialog = FragmentAlertDialog(
        title, body, buttonText, onDismiss,
    )

    fun fragmentDatePicker(
        birthDate: String,
        onTextChange: (String) -> Unit,
        onDateSubmission: () -> Unit,
        onDateDismiss: () -> Unit
    ): FragmentDatePickerDialog = FragmentDatePickerDialog(
        birthDate, onTextChange, onDateSubmission, onDateDismiss
    )
}
