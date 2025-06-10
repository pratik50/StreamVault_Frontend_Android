package com.pratik.streamvault.data.remote.dto

data class ShareResponse(
    val link: String,
    val message: String,
    val expiresAt: String
)