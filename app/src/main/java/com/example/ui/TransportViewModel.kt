package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.TransportRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppScreen {
    AUTH_LOGIN,
    PASSENGER_HOME,
    ADVANCE_BOOK,
    LIVE_TRACKING,
    DRIVER_MODULE,
    ADMIN_DASHBOARD,
    BOOKING_RECORDS
}

class TransportViewModel(
    private val repository: TransportRepository = TransportRepository.getInstance()
) : ViewModel() {

    val currentUser = repository.currentUser
    val bookings = repository.bookings
    val activeRideId = repository.activeRideId
    val driverLocation = repository.driverLocation
    val chatMessages = repository.chatMessages
    val notifications = repository.notifications
    val payments = repository.payments
    val isBengali = repository.isBengali
    val currentOperatingZone = repository.currentOperatingZone
    val availableOperatingZones = repository.availableOperatingZones
    val registeredDrivers = repository.registeredDrivers

    fun changeOperatingZone(zone: String) {
        repository.setOperatingZone(zone)
    }

    val activeRide: StateFlow<RideBooking?> = combine(bookings, activeRideId) { list, id ->
        list.firstOrNull { it.id == id } ?: list.firstOrNull { it.status == RideStatus.ACCEPTED || it.status == RideStatus.IN_TRANSIT }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _currentScreen = MutableStateFlow(AppScreen.PASSENGER_HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _selectedBookingForSlip = MutableStateFlow<RideBooking?>(null)
    val selectedBookingForSlip: StateFlow<RideBooking?> = _selectedBookingForSlip.asStateFlow()

    private val _paymentTargetBooking = MutableStateFlow<RideBooking?>(null)
    val paymentTargetBooking: StateFlow<RideBooking?> = _paymentTargetBooking.asStateFlow()

    private val _isChatOpen = MutableStateFlow(false)
    val isChatOpen: StateFlow<Boolean> = _isChatOpen.asStateFlow()

    private val _isNotificationSheetOpen = MutableStateFlow(false)
    val isNotificationSheetOpen: StateFlow<Boolean> = _isNotificationSheetOpen.asStateFlow()

    private val _isDriverRegistrationOpen = MutableStateFlow(false)
    val isDriverRegistrationOpen: StateFlow<Boolean> = _isDriverRegistrationOpen.asStateFlow()

    private val _trackedDriverByPhone = MutableStateFlow<DriverLocation?>(null)
    val trackedDriverByPhone: StateFlow<DriverLocation?> = _trackedDriverByPhone.asStateFlow()

    private val _bannerMessage = MutableStateFlow<String?>(null)
    val bannerMessage: StateFlow<String?> = _bannerMessage.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun openBookingSlip(booking: RideBooking) {
        _selectedBookingForSlip.value = booking
    }

    fun closeBookingSlip() {
        _selectedBookingForSlip.value = null
    }

    fun openPaymentModal(booking: RideBooking) {
        _paymentTargetBooking.value = booking
    }

    fun closePaymentModal() {
        _paymentTargetBooking.value = null
    }

    fun openChat(open: Boolean) {
        _isChatOpen.value = open
    }

    fun openNotifications(open: Boolean) {
        _isNotificationSheetOpen.value = open
    }

    fun clearBanner() {
        _bannerMessage.value = null
    }

    fun showBanner(msg: String) {
        _bannerMessage.value = msg
    }

    fun toggleLanguage() {
        repository.toggleLanguage()
    }

    fun switchRole(role: UserRole) {
        val user = when (role) {
            UserRole.PASSENGER -> repository.demoPassenger
            UserRole.DRIVER -> repository.demoDriver
            UserRole.ADMIN -> repository.demoAdmin
        }
        repository.switchUser(user)
        when (role) {
            UserRole.PASSENGER -> _currentScreen.value = AppScreen.PASSENGER_HOME
            UserRole.DRIVER -> _currentScreen.value = AppScreen.DRIVER_MODULE
            UserRole.ADMIN -> _currentScreen.value = AppScreen.ADMIN_DASHBOARD
        }
    }

    fun selectActiveRide(rideId: String) {
        repository.setActiveRide(rideId)
        _currentScreen.value = AppScreen.LIVE_TRACKING
    }

    fun bookAdvanceRide(
        pickup: String,
        dropoff: String,
        category: VehicleCategory,
        scheduledMillis: Long,
        passengers: Int,
        notes: String,
        isReserve: Boolean = false
    ) {
        val result = repository.bookRide(
            pickup = pickup,
            dropoff = dropoff,
            vehicleCategory = category,
            scheduledTimeMillis = scheduledMillis,
            passengerCount = passengers,
            specialNotes = notes,
            isReserve = isReserve
        )
        result.onSuccess {
            _selectedBookingForSlip.value = it
            repository.setActiveRide(it.id)
            _currentScreen.value = AppScreen.PASSENGER_HOME
            _bannerMessage.value = if (isBengali.value) 
                if (isReserve) "আপনার পুরো অটো রিজার্ভ বুকিং সম্পন্ন হয়েছে!" else "আপনার অগ্রিম বুকিং গ্রহণ করা হয়েছে! টিকিট দেখুন।" 
            else 
                if (isReserve) "Full Auto Reserve confirmed!" else "Advance booking confirmed! View your boarding ticket."
        }.onFailure {
            _bannerMessage.value = it.message ?: "Booking error"
        }
    }

    fun getEstimatedDistanceAndFare(pickup: String, dropoff: String, isReserve: Boolean = false): Pair<Double, Double> {
        return repository.getEstimatedDistanceAndFare(pickup, dropoff, isReserve)
    }

    fun openDriverRegistration(open: Boolean) {
        _isDriverRegistrationOpen.value = open
    }

    fun registerRickshawDriver(driver: RickshawDriverRegistration) {
        repository.registerRickshawDriver(driver)
        _isDriverRegistrationOpen.value = false
        _bannerMessage.value = if (isBengali.value) 
            "চালকের তথ্য সফলভাবে ডাটাবেজে সংরক্ষিত হয়েছে!" 
        else 
            "Driver data registered successfully!"
    }

    fun verifyDriver(driverId: String) {
        repository.verifyDriver(driverId)
    }

    fun deleteDriver(driverId: String) {
        repository.deleteDriver(driverId)
    }

    fun searchAndTrackPhone(phone: String) {
        val location = repository.trackDriverByPhone(phone)
        if (location != null) {
            _trackedDriverByPhone.value = location
            _bannerMessage.value = if (isBengali.value)
                "চালকের লোকেশন পাওয়া গেছে (${location.nearbyStand})!"
            else
                "Driver location found at ${location.nearbyStand}!"
        } else {
            _bannerMessage.value = if (isBengali.value)
                "এই নম্বরের কোনো রিকশাচালকের তথ্য পাওয়া যায়নি।"
            else
                "No driver found for this phone number."
        }
    }

    fun clearTrackedPhone() {
        _trackedDriverByPhone.value = null
    }

    fun acceptBooking(rideId: String) {
        repository.acceptBooking(rideId)
        _bannerMessage.value = if (isBengali.value) "রাইড গ্রহণ করা হয়েছে!" else "Ride accepted!"
    }

    fun updateRideStatus(rideId: String, status: RideStatus) {
        repository.updateRideStatus(rideId, status)
    }

    fun processPayment(bookingId: String, gateway: PaymentGateway, account: String) {
        val res = repository.processPayment(bookingId, gateway, account)
        res.onSuccess {
            _paymentTargetBooking.value = null
            _bannerMessage.value = if (isBengali.value)
                "ভাড়া পরিশোধ সম্পন্ন হয়েছে! ট্রানজেকশন আইডি: ${it.transactionId}"
            else
                "Payment completed! Txn: ${it.transactionId}"
        }
    }

    fun sendChatMessage(rideId: String, text: String) {
        if (text.isNotBlank()) {
            repository.sendMessage(rideId, text.trim())
        }
    }

    fun toggleLocationSharing(isSharing: Boolean) {
        repository.toggleLocationSharing(isSharing)
    }

    fun toggleDriverOnline(isOnline: Boolean) {
        repository.toggleDriverOnline(isOnline)
    }

    fun markNotificationRead(id: String) {
        repository.markNotificationAsRead(id)
    }

    fun clearAllNotifications() {
        repository.clearNotifications()
    }
}
