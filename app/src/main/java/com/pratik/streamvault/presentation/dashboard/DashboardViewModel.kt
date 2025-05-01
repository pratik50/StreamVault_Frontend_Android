package com.pratik.streamvault.presentation.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratik.streamvault.data.local.UserPreferences
import com.pratik.streamvault.data.remote.dto.FilesResponse
import com.pratik.streamvault.repository.FileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class DashboardViewModel @Inject constructor(
    val fileRepository: FileRepository
): ViewModel() {

    private val _files = MutableLiveData<FilesResponse>()
    val files: LiveData<FilesResponse> = _files

    private val _navigateToLogin = MutableSharedFlow<Unit>()
    val navigateToLogin = _navigateToLogin

    fun loadFiles() {
        viewModelScope.launch {
            try {
                val response = fileRepository.getFiles()
                _files.value = response
            } catch (e: Exception) {
                Timber.e(e, "Error loading files")
            }
        }
    }

    //  Logout by clearing token
    fun logout(userPrefs: UserPreferences) {
        viewModelScope.launch {
            userPrefs.clearToken()
            _navigateToLogin.emit(Unit)
        }
    }
}