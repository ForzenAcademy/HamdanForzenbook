package com.hamdan.forzenbook.legacy.core.view.utils

import android.app.AlertDialog
import android.content.Context

object DialogUtils {
    fun standardDialog(
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
}
