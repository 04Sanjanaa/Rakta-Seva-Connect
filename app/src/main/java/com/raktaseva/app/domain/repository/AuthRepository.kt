package com.raktaseva.app.domain.repository

import com.raktaseva.app.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining authentication operations for the application.
 */
interface AuthRepository {
    suspend fun verifyPhoneNumber(phoneNumber: String, activity: android.app.Activity): Flow<Resource<String>>
    suspend fun signInWithCredential(verificationId: String, code: String): Flow<Resource<Boolean>>
    fun signOut()
    fun isUserLoggedIn(): Boolean
    fun getCurrentUserId(): String?
}
