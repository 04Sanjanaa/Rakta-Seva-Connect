package com.raktaseva.app.presentation.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raktaseva.app.domain.models.BloodRequest
import com.raktaseva.app.domain.repository.AuthRepository
import com.raktaseva.app.domain.repository.DonorRepository
import com.raktaseva.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val donorRepository: DonorRepository
) : ViewModel() {

    private val _requestsState = MutableStateFlow<Resource<List<BloodRequest>>>(Resource.Loading())
    val requestsState: StateFlow<Resource<List<BloodRequest>>> = _requestsState

    private val _acceptState = MutableStateFlow<Resource<Boolean>>(Resource.Success(false))
    val acceptState: StateFlow<Resource<Boolean>> = _acceptState

    val currentUserId: String? get() = authRepository.getCurrentUserId()

    init {
        fetchActiveRequests()
    }

    fun fetchActiveRequests() {
        viewModelScope.launch {
            donorRepository.getActiveRequests().collect { result ->
                _requestsState.value = result
            }
        }
    }

    fun acceptRequest(requestId: String) {
        val uid = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            donorRepository.acceptRequest(requestId, uid).collect { result ->
                _acceptState.value = result
                if (result is Resource.Success && result.data == true) {
                    fetchActiveRequests() // Refresh list
                }
            }
        }
    }
}
