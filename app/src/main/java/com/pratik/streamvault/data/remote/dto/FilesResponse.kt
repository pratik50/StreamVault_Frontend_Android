package com.pratik.streamvault.data.remote.dto

import com.pratik.streamvault.model.FileItem

data class FilesResponse(
    val message: String,
    val files: List<FileItem>
)
