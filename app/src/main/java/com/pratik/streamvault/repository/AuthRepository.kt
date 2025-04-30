package com.pratik.streamvault.repository

import com.pratik.streamvault.data.remote.ApiService
import com.pratik.streamvault.data.remote.dto.AuthResponse
import com.pratik.streamvault.data.remote.dto.LoginRequest
import com.pratik.streamvault.data.remote.dto.SignupRequest
import jakarta.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun login(request: LoginRequest): AuthResponse {
        return apiService.login(request)
    }

    suspend fun signup(request: SignupRequest): AuthResponse {
        return apiService.signup(request)
    }

}