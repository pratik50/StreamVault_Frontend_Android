package com.pratik.streamvault.presentation.dashboard.state

interface FileItemClickListener {
    fun onMoreClick(fileId: String)
    fun onFileClick(fileUrl: String, mimeType: String)
}