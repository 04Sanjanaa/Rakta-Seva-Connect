package com.raktaseva.app.presentation.ui.screens.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raktaseva.app.domain.repository.AuthRepository
import com.raktaseva.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val donorRepository: com.raktaseva.app.domain.repository.DonorRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private var currentVerificationId: String? = null

    fun verifyPhoneNumber(phoneNumber: String, activity: Activity) {
        viewModelScope.launch {
            authRepository.verifyPhoneNumber(phoneNumber, activity).collect { result ->
                when (result) {
                    is Resource.Loading -> _authState.value = AuthState.Loading
                    is Resource.Success -> {
                        currentVerificationId = result.data
                        _authState.value = AuthState.CodeSent
                    }
                    is Resource.Error -> _authState.value = AuthState.Error(result.message ?: "Error")
                }
            }
        }
    }

    fun verifyCode(code: String) {
        val verificationId = currentVerificationId ?: return
        viewModelScope.launch {
            authRepository.signInWithCredential(verificationId, code).collect { result ->
                when (result) {
                    is Resource.Loading -> _authState.value = AuthState.Loading
                    is Resource.Success -> {
                        checkUserProfile()
                    }
                    is Resource.Error -> _authState.value = AuthState.Error(result.message ?: "Error")
                }
            }
        }
    }

    private fun checkUserProfile() {
        val uid = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            donorRepository.getUserProfile(uid).collect { result ->
                when (result) {
                    is Resource.Loading -> _authState.value = AuthState.Loading
                    is Resource.Success -> _authState.value = AuthState.Authenticated
                    is Resource.Error -> _authState.value = AuthState.NeedsProfileSetup
                }
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object CodeSent : AuthState()
    object Authenticated : AuthState()
    object NeedsProfileSetup : AuthState()
    data class Error(val message: String) : AuthState()
}
