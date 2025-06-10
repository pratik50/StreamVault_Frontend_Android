package com.pratik.streamvault.presentation.dashboard.state

import com.pratik.streamvault.data.remote.dto.UploadFileResponse

sealed class UploadState {
    object Loading : UploadState()
    data class Success(val uploadFileResponse: UploadFileResponse) : UploadState()
    data class Error(val message: String) : UploadState()
}