package com.example.data.model

data class RickshawDriverRegistration(
    val id: String = java.util.UUID.randomUUID().toString(),
    val fullName: String,
    val phone: String,
    val nidNumber: String,
    val rccTokenNumber: String,
    val vehicleType: String, // e.g. "সিসিক ব্যাটারি অটো", "চার্জার রিকশা", "প্যাডেল রিকশা"
    val assignedStand: String, // e.g. "সাহেব বাজার জিরো পয়েন্ট", "কাজলা গেট"
    val emergencyContactPhone: String = "",
    val garageArea: String = "",
    val experienceYears: Int = 3,
    val submittedAtMillis: Long = System.currentTimeMillis(),
    val isVerified: Boolean = true,
    val notes: String = ""
)
