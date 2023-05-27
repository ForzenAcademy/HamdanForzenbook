package com.hamdan.forzenbook.core

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
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
