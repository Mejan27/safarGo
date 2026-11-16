package com.example.data.model

enum class UserRole {
    PASSENGER,
    DRIVER,
    ADMIN
}

data class User(
    val id: String,
    val name: String,
    val phone: String,
    val role: UserRole,
    val email: String = "",
    val avatarUrl: String = "",
    val vehicleModel: String = "",
    val vehiclePlateNumber: String = "",
    val vehicleType: String = "",
    val driverTokenNumber: String = "",
    val assignedStand: String = "",
    val rating: Double = 4.9,
    val totalTrips: Int = 0,
    val isOnline: Boolean = true
)
