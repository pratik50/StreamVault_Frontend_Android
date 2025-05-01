package com.pratik.streamvault.data.remote

import com.pratik.streamvault.data.remote.dto.AuthResponse
import com.pratik.streamvault.data.remote.dto.FilesResponse
import com.pratik.streamvault.data.remote.dto.LoginRequest
import com.pratik.streamvault.data.remote.dto.SignupRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth/signup")
    suspend fun signup(@Body request: SignupRequest): AuthResponse

    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

}


interface FileApi {
    @GET("/api/files")
    suspend fun getFiles(): FilesResponse
}