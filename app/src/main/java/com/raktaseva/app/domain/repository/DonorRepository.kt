package com.raktaseva.app.domain.repository

import com.raktaseva.app.domain.models.BloodRequest
import com.raktaseva.app.domain.models.User
import com.raktaseva.app.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining data operations for donors and blood requests.
 */
interface DonorRepository {
    suspend fun saveUserProfile(user: User): Flow<Resource<Boolean>>
    suspend fun getUserProfile(uid: String): Flow<Resource<User>>
    suspend fun toggleAvailability(uid: String, isAvailable: Boolean): Flow<Resource<Boolean>>
    
    suspend fun createBloodRequest(request: BloodRequest): Flow<Resource<Boolean>>
    suspend fun getNearbyDonors(bloodGroup: String, lat: Double, lng: Double, radiusKm: Double): Flow<Resource<List<User>>>
    
    suspend fun getActiveRequests(): Flow<Resource<List<BloodRequest>>>
    suspend fun acceptRequest(requestId: String, donorId: String): Flow<Resource<Boolean>>
}
