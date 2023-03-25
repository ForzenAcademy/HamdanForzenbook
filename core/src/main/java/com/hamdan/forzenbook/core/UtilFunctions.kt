package com.hamdan.forzenbook.core

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.hamdan.forzenbook.ui.core.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
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

fun getBitmapFromUri(
    uri: Uri,
    context: Context,
    onError: () -> Unit,
    onImageSaved: (File) -> Unit
) {
    Glide
        .with(context)
        .asBitmap()
        .error(R.drawable.baseline_square_24)
        .load(uri)
        .into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                // Save the downloaded bitmap to a temporary file
                val tempFile: File
                try {
                    tempFile = createTemporaryImageFile()
                    FileOutputStream(tempFile).use { fos ->
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    }
                    onImageSaved(tempFile)
                } catch (e: IOException) {
                    e.printStackTrace()
                    onError()
                }
            }
        })
}

private fun createTemporaryImageFile(): File =
    File.createTempFile(GlobalConstants.TEMPORARY_FILENAME, ".jpg")

fun launchGalleryImageGetter(contentLauncher: ActivityResultLauncher<String>) {
    contentLauncher.launch("image/*")
}
