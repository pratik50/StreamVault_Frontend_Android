package com.pratik.streamvault.repository

import com.pratik.streamvault.data.remote.AuthApi
import com.pratik.streamvault.data.remote.dto.AuthResponse
import com.pratik.streamvault.data.remote.dto.LoginRequest
import com.pratik.streamvault.data.remote.dto.SignupRequest
import jakarta.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi
) {

    suspend fun login(request: LoginRequest): AuthResponse {
        return authApi.login(request)
    }

    suspend fun signup(request: SignupRequest): AuthResponse {
        return authApi.signup(request)
    }

}