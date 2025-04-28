package com.pratik.streamvault.data.remote.dto

data class FilesResponse(
    val files: List<FileDto>
)

data class FileDto(
    val id: String,
    val name: String,
    val type: String,
    val size: Int,
    val url: String,
    val createdAt: String,
)
