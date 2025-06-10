package com.pratik.streamvault.presentation.dashboard.state

interface FileItemClickListener {
    fun onDelete(fileId: String)
    fun onDownload(fileId: String)
    fun onShare(fileId: String)
}