package com.raktaseva.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.raktaseva.app.domain.models.BloodRequest
import com.raktaseva.app.domain.models.User
import com.raktaseva.app.domain.repository.DonorRepository
import com.raktaseva.app.utils.Constants
import com.raktaseva.app.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DonorRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : DonorRepository {

    override suspend fun saveUserProfile(user: User): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            firestore.collection(Constants.USERS_COLLECTION)
                .document(user.uid)
                .set(user)
                .await()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to save user profile"))
        }
    }

    override suspend fun getUserProfile(uid: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val snapshot = firestore.collection(Constants.USERS_COLLECTION).document(uid).get().await()
            val user = snapshot.toObject(User::class.java)
            if (user != null) {
                emit(Resource.Success(user))
            } else {
                emit(Resource.Error("User not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch user profile"))
        }
    }

    override suspend fun toggleAvailability(uid: String, isAvailable: Boolean): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            firestore.collection(Constants.USERS_COLLECTION)
                .document(uid)
                .update("isAvailable", isAvailable)
                .await()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to update availability"))
        }
    }

    override suspend fun createBloodRequest(request: BloodRequest): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            firestore.collection(Constants.REQUESTS_COLLECTION)
                .document(request.requestId)
                .set(request)
                .await()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to create request"))
        }
    }

    override suspend fun getNearbyDonors(
        bloodGroup: String,
        lat: Double,
        lng: Double,
        radiusKm: Double
    ): Flow<Resource<List<User>>> = flow {
        emit(Resource.Loading())
        try {
            // Simplified geofencing query (in production, use GeoHash or GeoFirestore)
            // Filtering by bloodGroup and availability first
            val snapshot = firestore.collection(Constants.USERS_COLLECTION)
                .whereEqualTo("bloodGroup", bloodGroup)
                .whereEqualTo("isAvailable", true)
                .get()
                .await()

            val donors = snapshot.toObjects(User::class.java).filter { user ->
                calculateDistance(lat, lng, user.latitude, user.longitude) <= radiusKm
            }

            emit(Resource.Success(donors))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch nearby donors"))
        }
    }

    override suspend fun getActiveRequests(): Flow<Resource<List<BloodRequest>>> = flow {
        emit(Resource.Loading())
        try {
            val snapshot = firestore.collection(Constants.REQUESTS_COLLECTION)
                .whereIn("status", listOf("Pending", "Accepted"))
                .get()
                .await()
            
            val requests = snapshot.toObjects(BloodRequest::class.java)
            emit(Resource.Success(requests))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch requests"))
        }
    }

    override suspend fun acceptRequest(requestId: String, donorId: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            firestore.collection(Constants.REQUESTS_COLLECTION)
                .document(requestId)
                .update(
                    mapOf(
                        "status" to "Accepted",
                        "acceptedByDonorId" to donorId
                    )
                )
                .await()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to accept request"))
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371 // Earth radius in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }
}
