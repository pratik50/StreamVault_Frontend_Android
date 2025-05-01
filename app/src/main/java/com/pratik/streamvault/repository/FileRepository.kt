package com.pratik.streamvault.repository

import com.pratik.streamvault.data.remote.FileApi
import com.pratik.streamvault.data.remote.dto.FilesResponse
import jakarta.inject.Inject

class FileRepository @Inject constructor(
    val fileApi: FileApi
) {

    suspend fun getFiles(): FilesResponse {
        return fileApi.getFiles()
    }

}