package com.pratik.streamvault.utils

import android.content.Intent

object FileFolderPickerUtils {
    fun getImagePickerIntent(): Intent {
        return Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
    }
}