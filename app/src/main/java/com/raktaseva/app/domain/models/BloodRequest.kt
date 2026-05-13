package com.raktaseva.app.domain.models

data class BloodRequest(
    val requestId: String = "",
    val requesterId: String = "",
    val bloodGroup: String = "",
    val unitsRequired: Int = 1,
    val hospitalName: String = "",
    val contactNumber: String = "",
    val urgencyLevel: String = "Normal", // Normal, Urgent, Critical
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val status: String = "Pending", // Pending, Accepted, Fulfilled
    val acceptedByDonorId: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
