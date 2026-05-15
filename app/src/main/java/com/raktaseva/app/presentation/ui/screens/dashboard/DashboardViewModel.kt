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

/**
 * ViewModel for the Dashboard screen, responsible for fetching and managing blood requests.
 * 
 * @property authRepository Repository for authentication-related data.
 * @property donorRepository Repository for blood request and donor-related operations.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val donorRepository: DonorRepository
) : ViewModel() {

    private val _requestsState = MutableStateFlow<Resource<List<BloodRequest>>>(Resource.Loading())
    /**
     * State of the active blood requests list.
     */
    val requestsState: StateFlow<Resource<List<BloodRequest>>> = _requestsState

    private val _acceptState = MutableStateFlow<Resource<Boolean>>(Resource.Success(false))
    /**
     * State of the "accept request" operation.
     */
    val acceptState: StateFlow<Resource<Boolean>> = _acceptState

    /**
     * The ID of the currently logged-in user.
     */
    val currentUserId: String? get() = authRepository.getCurrentUserId()

    init {
        fetchActiveRequests()
    }

    /**
     * Fetches all active blood requests from the repository.
     */
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
