package com.pratik.streamvault.data.remote.dto

import com.pratik.streamvault.model.FileItem

data class FilesResponse(
    val folders: List<Any>, // or a Folder data class if needed
    val unfolderedFiles: List<FileItem>
)
