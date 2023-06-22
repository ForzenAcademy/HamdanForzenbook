package com.hamdan.forzenbook.core

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.DatePicker
import androidx.activity.result.ActivityResultLauncher
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import com.hamdan.forzenbook.ui.core.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar
import java.util.regex.Pattern

fun validateEmail(email: String): Boolean {
    val regex =
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
    val pattern = Pattern.compile(regex)
    return pattern.matcher(email).matches()
}

fun stringDate(month: Int, day: Int, year: Int, context: Context): String {
    val date = context.getString(R.string.create_account_date, month, day, year).split("-")
    return date.joinToString("-") { it.leftPad() }
}

fun String.leftPad(): String {
    return if (this.length < 2) {
        "0$this"
    } else this
}

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

suspend fun saveBitmapFromUri(
    uri: Uri,
    context: Context,
    onError: () -> Unit,
    onImageSaved: (File) -> Unit,
) {
    val imageLoader = ImageLoader(context)
    imageLoader.execute(
        ImageRequest.Builder(context)
            .data(uri)
            .allowConversionToBitmap(true)
            .target {
                it.toBitmap().apply {
                    val tempFile: File
                    try {
                        tempFile = createTemporaryImageFile()
                        FileOutputStream(tempFile).use { fos ->
                            this.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                        }
                        onImageSaved(tempFile)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        onError()
                    }
                }
            }
            .build()
    )
}

private fun createTemporaryImageFile(): File =
    File.createTempFile(GlobalConstants.TEMPORARY_FILENAME, ".jpg")

fun launchGalleryImageGetter(contentLauncher: ActivityResultLauncher<String>) {
    contentLauncher.launch("image/*")
}

fun getToken(context: Context): String? {
    return context.getSharedPreferences(
        GlobalConstants.TOKEN_PREFERENCE_LOCATION,
        Context.MODE_PRIVATE
    ).getString(GlobalConstants.TOKEN_KEY, null)
}
