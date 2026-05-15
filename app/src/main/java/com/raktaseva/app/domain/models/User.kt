package com.raktaseva.app.domain.models

/**
 * Represents a donor/user in the system.
 */
data class User(
    val uid: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val bloodGroup: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val lastDonationDate: Long = 0L,
    val isAvailable: Boolean = true,
    val fcmToken: String = ""
)
