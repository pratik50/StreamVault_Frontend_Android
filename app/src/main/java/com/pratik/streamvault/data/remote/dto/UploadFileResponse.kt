package com.pratik.streamvault.data.remote.dto

import com.pratik.streamvault.model.FileItem

data class UploadFileResponse(
    val message: String,
    val file: FileItem
)
