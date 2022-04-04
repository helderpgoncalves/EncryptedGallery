package com.bitdev.encryptedgallery

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        return formatter.format(now)
    }

    fun getImageUri(context: Context, image: Bitmap, title: String): Uri? {
        val bytes = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver,image,title,null)
        return Uri.parse(path)
    }
}