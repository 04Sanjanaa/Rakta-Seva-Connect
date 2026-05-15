package com.raktaseva.app.presentation.ui.screens.request

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
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for the Blood Request screen, handling the creation of new emergency requests.
 * 
 * @property authRepository Repository for fetching current user information.
 * @property donorRepository Repository for persisting blood requests.
 */
@HiltViewModel
class RequestBloodViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val donorRepository: DonorRepository
) : ViewModel() {

    private val _requestState = MutableStateFlow<Resource<Boolean>>(Resource.Success(false))
    /**
     * State of the blood request submission process.
     */
    val requestState: StateFlow<Resource<Boolean>> = _requestState

    /**
     * Submits a new blood request to Firestore.
     * 
     * @param bloodGroup The requested blood group (e.g., "O+").
     * @param hospitalName The name of the hospital where blood is needed.
     * @param unitsRequired Number of blood units required.
     * @param contactNumber Contact phone number for the request.
     * @param urgencyLevel Level of urgency (Normal, Urgent, Critical).
     */
    fun submitRequest(
        bloodGroup: String,
        hospitalName: String,
        unitsRequired: Int,
        contactNumber: String,
        urgencyLevel: String
    ) {
        val uid = authRepository.getCurrentUserId() ?: return
        
        viewModelScope.launch {
            val request = BloodRequest(
                requestId = UUID.randomUUID().toString(),
                requesterId = uid,
                bloodGroup = bloodGroup,
                hospitalName = hospitalName,
                unitsRequired = unitsRequired,
                contactNumber = contactNumber,
                urgencyLevel = urgencyLevel,
                status = "Pending",
                timestamp = System.currentTimeMillis()
            )
            
            donorRepository.createBloodRequest(request).collect { result ->
                _requestState.value = result
            }
        }
    }
    
    fun resetState() {
        _requestState.value = Resource.Success(false)
    }
}
