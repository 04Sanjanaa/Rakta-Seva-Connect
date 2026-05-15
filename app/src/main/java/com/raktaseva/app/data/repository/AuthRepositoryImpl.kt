package com.raktaseva.app.data.repository

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.raktaseva.app.domain.repository.AuthRepository
import com.raktaseva.app.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Implementation of [AuthRepository] using Firebase Authentication.
 */
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun verifyPhoneNumber(phoneNumber: String, activity: Activity): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading())

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Auto retrieval or instant validation
            }

            override fun onVerificationFailed(e: FirebaseException) {
                trySend(Resource.Error(e.message ?: "Verification failed"))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                trySend(Resource.Success(verificationId))
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        awaitClose { /* No cleanup needed for now */ }
    }

    override suspend fun signInWithCredential(verificationId: String, code: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            auth.signInWithCredential(credential).await()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Sign in failed"))
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
