package com.pratik.streamvault.model

data class FileItem(
    val id: String,
    val name: String,
    val type: String,
    val size: Int,
    val url: String
)