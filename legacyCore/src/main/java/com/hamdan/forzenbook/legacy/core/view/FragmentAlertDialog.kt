package com.hamdan.forzenbook.legacy.core.view

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class FragmentAlertDialog(
    private val title: String,
    private val body: String,
    private val buttonText: String,
    private val onDismiss: () -> Unit,
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext()).apply {
            setTitle(title)
            setMessage(body)
            setPositiveButton(buttonText) { dialog, _ ->
                dialog.dismiss()
            }
        }.create().apply {
            onDismiss()
        }
        return dialog
    }
}
