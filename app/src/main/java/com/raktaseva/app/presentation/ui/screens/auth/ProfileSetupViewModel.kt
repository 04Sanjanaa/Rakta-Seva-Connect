package com.raktaseva.app.presentation.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raktaseva.app.domain.models.User
import com.raktaseva.app.domain.repository.AuthRepository
import com.raktaseva.app.domain.repository.DonorRepository
import com.raktaseva.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val donorRepository: DonorRepository
) : ViewModel() {

    private val _setupState = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val setupState: StateFlow<Resource<Boolean>> = _setupState

    init {
        _setupState.value = Resource.Success(false) // Initial idle state
    }

    fun saveProfile(name: String, bloodGroup: String, location: String) {
        val uid = authRepository.getCurrentUserId() ?: return
        val phoneNumber = authRepository.getCurrentUserId() // Placeholder if not directly available, but usually we'd get it from Firebase User
        
        viewModelScope.launch {
            val user = User(
                uid = uid,
                name = name,
                phoneNumber = "", // Will be updated if we can get it
                bloodGroup = bloodGroup,
                isAvailable = true
            )
            donorRepository.saveUserProfile(user).collect { result ->
                _setupState.value = result
            }
        }
    }
    
    fun getPhoneNumber(): String {
        // Implementation to get phone number from FirebaseUser
        return "" 
    }
}
