package com.hamdan.forzenbook.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.hamdan.forzenbook.ui.core.R
import java.util.regex.Pattern

fun validateEmail(email: String): Boolean {
    val regex =
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
    val pattern = Pattern.compile(regex)
    return pattern.matcher(email).matches()
}

fun stringDate(month: Int, day: Int, year: Int, context: Context): String {
    val date =
        context.getString(R.string.create_account_date, month, day, year).split("-")
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
    onBitmapLoaded: (Bitmap) -> Unit
) {
    Glide
        .with(context)
        .asBitmap()
        .error(R.drawable.baseline_square_24)
        .load(uri)
        .into(BitmapGetter(onError = onError) { onBitmapLoaded(it) })
}

class BitmapGetter(val onError: () -> Unit, val onGetBitmap: (Bitmap) -> Unit) :
    CustomTarget<Bitmap>() {
    override fun onLoadCleared(placeholder: Drawable?) {}
    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
        try {
            onGetBitmap(resource)
        } catch (e: Exception) {
            onError()
        }
    }
}
