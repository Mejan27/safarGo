package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class TransportRepository private constructor() {

    private val scope = CoroutineScope(Dispatchers.Default)

    // Rajshahi Metropolitan Operating Area Zones
    val availableOperatingZones = listOf(
        "রাজশাহী মহানগর - সাহেব বাজার ও জিরো পয়েন্ট এলাকা",
        "রাজশাহী মহানগর - রাবি ও কাজলা/তালাইমারী জোন",
        "রাজশাহী মহানগর - লক্ষ্মীপুর ও মেডিকেল কলেজ এলাকা",
        "রাজশাহী মহানগর - শিরোইল স্টেশন ও ভদ্রা এলাকা",
        "রাজশাহী মহানগর - উপশহর ও নিউ মার্কেট আবাসিক জোন",
        "রাজশাহী মহানগর - কোর্ট চত্বর ও সিঅ্যান্ডবি মোড়",
        "রাজশাহী মহানগর - পদ্মা গার্ডেন ও টি-বাঁধ এলাকা"
    )

    private val _currentOperatingZone = MutableStateFlow("রাজশাহী মহানগর - সাহেব বাজার ও জিরো পয়েন্ট এলাকা")
    val currentOperatingZone: StateFlow<String> = _currentOperatingZone.asStateFlow()

    // Pre-configured Demo Users tailored for Rajshahi Metropolitan Area
    val demoPassenger = User(
        id = "pass_01",
        name = "মো. সায়মন চৌধুরী (রাবি শিক্ষার্থী / যাত্রী)",
        phone = "+880 1711-234567",
        role = UserRole.PASSENGER,
        email = "saymon.rajshahi@safargo.com",
        rating = 4.95,
        totalTrips = 32
    )

    val demoDriver = User(
        id = "drv_01",
        name = "মো. রহিম মিয়া (অটোচালক)",
        phone = "+880 1812-987654",
        role = UserRole.DRIVER,
        email = "rahim.rajshahi@safargo.com",
        vehicleModel = "সিসিক অনুমোদিত ইজিবাইক অটো (লাল শিফট)",
        vehiclePlateNumber = "রাজশাহী-অটো-০৮",
        vehicleType = "সিসিক ব্যাটারি অটোরিকশা",
        driverTokenNumber = "সিসিক টোকেন: লাল-০৮",
        assignedStand = "সাহেব বাজার জিরো পয়েন্ট স্ট্যান্ড",
        rating = 4.92,
        totalTrips = 418,
        isOnline = true
    )

    val demoAdmin = User(
        id = "adm_01",
        name = "মেজান (রাজশাহী মহানগর এডমিন)",
        phone = "+880 1700-000000",
        role = UserRole.ADMIN,
        email = "mehedihassanmejan45@gmail.com",
        rating = 5.0,
        totalTrips = 0
    )

    // Current User
    private val _currentUser = MutableStateFlow<User>(demoPassenger)
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    // Bookings State
    private val _bookings = MutableStateFlow<List<RideBooking>>(emptyList())
    val bookings: StateFlow<List<RideBooking>> = _bookings.asStateFlow()

    // Active Ride for tracking
    private val _activeRideId = MutableStateFlow<String?>(null)
    val activeRideId: StateFlow<String?> = _activeRideId.asStateFlow()

    // Driver Telemetry / Location for Rajshahi Metropolitan Area
    private val _driverLocation = MutableStateFlow(
        DriverLocation(
            driverId = "drv_01",
            driverName = "মো. রহিম মিয়া",
            latitude = 24.3686, // Saheb Bazar Zero Point, Rajshahi
            longitude = 88.6015,
            speedKmh = 19.5f,
            headingDegrees = 75f,
            isSharingLocation = true,
            vehiclePlate = "রাজশাহী-অটো-০৮",
            driverTokenNumber = "সিসিক টোকেন: লাল-০৮",
            vehicleType = "সিসিক ব্যাটারি অটো",
            currentZone = "রাজশাহী মহানগর - সাহেব বাজার এলাকা",
            nearbyStand = "জিরো পয়েন্ট মোড় স্ট্যান্ড"
        )
    )
    val driverLocation: StateFlow<DriverLocation> = _driverLocation.asStateFlow()

    // Chat Messages
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    // Payments
    private val _payments = MutableStateFlow<List<PaymentRecord>>(emptyList())
    val payments: StateFlow<List<PaymentRecord>> = _payments.asStateFlow()

    // Language toggle: true = Bengali, false = English
    private val _isBengali = MutableStateFlow(true)
    val isBengali: StateFlow<Boolean> = _isBengali.asStateFlow()

    // Registered Rickshaw Pullers Data Directory
    private val _registeredDrivers = MutableStateFlow<List<RickshawDriverRegistration>>(
        listOf(
            RickshawDriverRegistration(
                id = "reg_drv_01",
                fullName = "মো. রহিম মিয়া",
                phone = "01711-234567",
                nidNumber = "1988501234567890",
                rccTokenNumber = "সিসিক টোকেন: লাল-০৮",
                vehicleType = "সিসিক ব্যাটারি অটো",
                assignedStand = "সাহেব বাজার জিরো পয়েন্ট",
                emergencyContactPhone = "01711-998877",
                garageArea = "আলুপট্টি গ্যারেজ",
                experienceYears = 8,
                isVerified = true,
                notes = "অভিজ্ঞ ও সিসিক লাইসেন্সধারী নিয়মিত চালক"
            ),
            RickshawDriverRegistration(
                id = "reg_drv_02",
                fullName = "কাশেম আলী",
                phone = "01610-998877",
                nidNumber = "1975509876543210",
                rccTokenNumber = "সিসিক টোকেন: সবুজ-২১",
                vehicleType = "চার্জার ব্যাটারি রিকশা",
                assignedStand = "শিরোইল রেল স্টেশন",
                emergencyContactPhone = "01819-223344",
                garageArea = "শিরোইল কলোনি গ্যারেজ",
                experienceYears = 12,
                isVerified = true,
                notes = "রেল স্টেশন ও মেডিকেল রুটে নিয়মিত চালান"
            ),
            RickshawDriverRegistration(
                id = "reg_drv_03",
                fullName = "মো. সুজন হোসেন",
                phone = "01822-334455",
                nidNumber = "1994503456789123",
                rccTokenNumber = "সিসিক টোকেন: লাল-১৫",
                vehicleType = "সিসিক ব্যাটারি অটো",
                assignedStand = "তালাইমারী মোড় (রুয়েট)",
                emergencyContactPhone = "01722-112233",
                garageArea = "কাজলা সুইপার কলোনি গ্যারেজ",
                experienceYears = 5,
                isVerified = true,
                notes = "রুয়েট ও বিশ্ববিদ্যালয় গেটে সার্বক্ষণিক পাওয়া যায়"
            ),
            RickshawDriverRegistration(
                id = "reg_drv_04",
                fullName = "বাবুল মিয়া",
                phone = "01912-778899",
                nidNumber = "1982506789123456",
                rccTokenNumber = "সিসিক টোকেন: সবুজ-০৩",
                vehicleType = "ঐতিহ্যবাহী প্যাডেল রিকশা",
                assignedStand = "লক্ষ্মীপুর মোড় (মেডিকেল)",
                emergencyContactPhone = "01912-556677",
                garageArea = "হড়গ্রাম বাজার",
                experienceYears = 15,
                isVerified = true,
                notes = "প্যাডেল রিকশা, শান্ত ও বিশ্বস্ত চালক"
            ),
            RickshawDriverRegistration(
                id = "reg_drv_05",
                fullName = "আলমগীর কবির",
                phone = "01733-445566",
                nidNumber = "1990508912345678",
                rccTokenNumber = "সিসিক টোকেন: লাল-৩৩",
                vehicleType = "সিসিক ব্যাটারি অটো",
                assignedStand = "রাবি কাজলা গেট",
                emergencyContactPhone = "01733-889900",
                garageArea = "বিনোদপুর বাজার",
                experienceYears = 6,
                isVerified = true,
                notes = "ক্যাম্পাস শিক্ষার্থীদের বিশেষ যত্ন সহকারে সেবা দেন"
            )
        )
    )
    val registeredDrivers: StateFlow<List<RickshawDriverRegistration>> = _registeredDrivers.asStateFlow()

    init {
        seedInitialRajshahiData()
        startLiveRajshahiTelemetrySimulation()
    }

    fun setOperatingZone(zone: String) {
        _currentOperatingZone.value = zone
        _driverLocation.value = _driverLocation.value.copy(currentZone = zone)
        postNotification(
            title = if (_isBengali.value) "রাজশাহী অপারেটিং এলাকা আপডেট" else "Operating Zone Updated",
            message = if (_isBengali.value) 
                "বর্তমানে '$zone'-এর লোকাল রিকশা ও অটো সার্ভিসে যুক্ত আছেন।" 
            else 
                "Currently connected to local transit in '$zone'.",
            type = NotificationType.SYSTEM_ALERT
        )
    }

    fun toggleLanguage() {
        _isBengali.value = !_isBengali.value
    }

    fun switchUser(user: User) {
        _currentUser.value = user
    }

    fun login(phone: String, role: UserRole): Boolean {
        val user = when (role) {
            UserRole.PASSENGER -> demoPassenger.copy(phone = phone)
            UserRole.DRIVER -> demoDriver.copy(phone = phone)
            UserRole.ADMIN -> demoAdmin.copy(phone = phone)
        }
        _currentUser.value = user
        return true
    }

    fun toggleDriverOnline(isOnline: Boolean) {
        if (_currentUser.value.role == UserRole.DRIVER) {
            _currentUser.value = _currentUser.value.copy(isOnline = isOnline)
        }
    }

    fun toggleLocationSharing(isSharing: Boolean) {
        _driverLocation.value = _driverLocation.value.copy(
            isSharingLocation = isSharing,
            lastUpdatedMillis = System.currentTimeMillis()
        )
        postNotification(
            title = if (_isBengali.value) "রাজশাহী অটো/রিকশা জিপিএস শেয়ারিং" else "Rajshahi GPS Sharing",
            message = if (isSharing) 
                "চালকের লাইভ স্ট্যান্ড লোকেশন শেয়ারিং সক্রিয় রয়েছে।" 
            else 
                "লোকেশন শেয়ারিং বন্ধ রয়েছে।",
            type = NotificationType.SYSTEM_ALERT
        )
    }

    // Advance Booking (at least 30 minutes ahead, or scheduled in Rajshahi City)
    fun bookRide(
        pickup: String,
        dropoff: String,
        vehicleCategory: VehicleCategory,
        scheduledTimeMillis: Long,
        passengerCount: Int,
        specialNotes: String,
        isReserve: Boolean = false
    ): Result<RideBooking> {
        val now = System.currentTimeMillis()
        val thirtyMinutesAhead = now + (28 * 60 * 1000)
        val isAdvance = scheduledTimeMillis >= thirtyMinutesAhead

        // Rajshahi local area distance & fare calculation (15 Taka/km shared, 25 Taka/km reserve auto)
        val distanceKm = calculateRajshahiDistance(pickup, dropoff)
        val estMinutes = (distanceKm * 4.0).toInt().coerceAtLeast(8)
        val perKmRate = if (isReserve) 25.0 else 15.0
        val minFare = if (isReserve) 40.0 else 15.0
        val rawFare = distanceKm * perKmRate
        // Round to nearest 5 Taka for realistic Rajshahi local cash/bKash payment
        val roundedFare = (Math.round(rawFare / 5.0) * 5.0).coerceAtLeast(minFare)

        val bookingRef = "RAJ-" + (1000..9999).random()
        val bookingId = UUID.randomUUID().toString()

        val qrData = "SAFARGO|RAJSHAHI|ZONE:${_currentOperatingZone.value}|REF:$bookingRef|FARE:৳$roundedFare|TYPE:${if (isReserve) "RESERVE" else "SHARED"}"

        val newBooking = RideBooking(
            id = bookingId,
            bookingReference = bookingRef,
            passengerId = _currentUser.value.id,
            passengerName = _currentUser.value.name,
            passengerPhone = _currentUser.value.phone,
            driverId = demoDriver.id,
            driverName = demoDriver.name,
            driverPhone = demoDriver.phone,
            driverTokenNumber = demoDriver.driverTokenNumber,
            vehicleModel = demoDriver.vehicleModel,
            vehiclePlate = demoDriver.vehiclePlateNumber,
            vehicleCategory = vehicleCategory,
            operatingZone = _currentOperatingZone.value,
            pickupLocation = pickup,
            dropoffLocation = dropoff,
            distanceKm = distanceKm,
            estimatedMinutes = estMinutes,
            fare = roundedFare,
            isAdvanceBooking = isAdvance,
            isReserve = isReserve,
            scheduledTimeMillis = scheduledTimeMillis,
            bookedAtMillis = now,
            status = RideStatus.PENDING,
            paymentStatus = PaymentStatus.UNPAID,
            passengerCount = if (isReserve) 5 else passengerCount,
            specialNotes = specialNotes,
            qrCodeData = qrData
        )

        _bookings.value = listOf(newBooking) + _bookings.value
        _activeRideId.value = newBooking.id

        // Post in-app push notification
        val reserveBadge = if (isReserve) " (পুরো অটো রিজার্ভ)" else ""
        postNotification(
            title = if (_isBengali.value) "অগ্রিম ${vehicleCategory.typeName}$reserveBadge বুকিং সম্পন্ন! (#$bookingRef)" else "Advance ${vehicleCategory.labelEn}$reserveBadge Booked! (#$bookingRef)",
            message = if (_isBengali.value) 
                "রাজশাহী মহানগর এলাকায় $pickup হতে $dropoff পর্যন্ত ${vehicleCategory.labelBn}$reserveBadge শিডিউল করা হয়েছে।" 
            else 
                "Rajshahi ride scheduled from $pickup to $dropoff in ${_currentOperatingZone.value}.",
            type = NotificationType.BOOKING_CONFIRMED,
            relatedRideId = bookingId
        )

        return Result.success(newBooking)
    }

    fun acceptBooking(bookingId: String) {
        val current = _bookings.value.toMutableList()
        val index = current.indexOfFirst { it.id == bookingId }
        if (index != -1) {
            val updated = current[index].copy(
                status = RideStatus.ACCEPTED,
                driverId = demoDriver.id,
                driverName = demoDriver.name,
                driverPhone = demoDriver.phone,
                driverTokenNumber = demoDriver.driverTokenNumber,
                vehicleModel = demoDriver.vehicleModel,
                vehiclePlate = demoDriver.vehiclePlateNumber
            )
            current[index] = updated
            _bookings.value = current

            postNotification(
                title = if (_isBengali.value) "চালক রাইড গ্রহণ করেছেন!" else "Driver Accepted!",
                message = if (_isBengali.value)
                    "${demoDriver.name} (${demoDriver.driverTokenNumber}) আপনার বুকিং গ্রহণ করেছেন। লাইভ ম্যাপে অবস্থান দেখুন।"
                else
                    "${demoDriver.name} (${demoDriver.driverTokenNumber}) accepted. Tracking is live in Rajshahi.",
                type = NotificationType.DRIVER_ASSIGNED,
                relatedRideId = bookingId
            )
        }
    }

    fun updateRideStatus(bookingId: String, newStatus: RideStatus) {
        val current = _bookings.value.toMutableList()
        val index = current.indexOfFirst { it.id == bookingId }
        if (index != -1) {
            val updated = current[index].copy(status = newStatus)
            current[index] = updated
            _bookings.value = current

            val notif = when (newStatus) {
                RideStatus.DRIVER_ARRIVED -> AppNotification(
                    id = UUID.randomUUID().toString(),
                    title = if (_isBengali.value) "অটো/রিকশা আপনার পিকআপে উপস্থিত" else "Auto/Rickshaw at Pickup",
                    message = if (_isBengali.value)
                        "${updated.driverName} (${updated.driverTokenNumber}) পিকআপ পয়েন্টে অপেক্ষা করছেন।"
                    else
                        "${updated.driverName} is waiting at pickup.",
                    type = NotificationType.DRIVER_ARRIVED,
                    relatedRideId = bookingId
                )
                RideStatus.IN_TRANSIT -> AppNotification(
                    id = UUID.randomUUID().toString(),
                    title = if (_isBengali.value) "যাত্রা শুরু হয়েছে (সফরগো)" else "Ride Started (SafarGo)",
                    message = if (_isBengali.value)
                        "${updated.pickupLocation} হতে গন্তব্যের উদ্দেশ্যে গাড়ি চলছে।"
                    else
                        "Vehicle en route to destination. Have a safe ride!",
                    type = NotificationType.RIDE_STARTED,
                    relatedRideId = bookingId
                )
                RideStatus.COMPLETED -> AppNotification(
                    id = UUID.randomUUID().toString(),
                    title = if (_isBengali.value) "গন্তব্যে পৌঁছেছেন! ভাড়া পরিশোধ করুন" else "Destination Reached! Pay Fare",
                    message = if (_isBengali.value)
                        "ভাড়া ৳${updated.fare.toInt()} চালককে ক্যাশ দিন অথবা বিকাশ/নগদে পরিশোধ করুন।"
                    else
                        "Total fare ৳${updated.fare.toInt()}. Pay via Cash or bKash.",
                    type = NotificationType.RIDE_COMPLETED,
                    relatedRideId = bookingId
                )
                RideStatus.CANCELLED -> AppNotification(
                    id = UUID.randomUUID().toString(),
                    title = if (_isBengali.value) "বুকিং বাতিল করা হয়েছে" else "Booking Cancelled",
                    message = "Ride # ${updated.bookingReference} has been cancelled.",
                    type = NotificationType.SYSTEM_ALERT,
                    relatedRideId = bookingId
                )
                else -> null
            }
            notif?.let { _notifications.value = listOf(it) + _notifications.value }
        }
    }

    // Payment Processing
    fun processPayment(bookingId: String, gateway: PaymentGateway, accountPhone: String): Result<PaymentRecord> {
        val current = _bookings.value.toMutableList()
        val index = current.indexOfFirst { it.id == bookingId }
        if (index == -1) return Result.failure(Exception("Booking not found"))

        val booking = current[index]
        val prefix = when (gateway) {
            PaymentGateway.BKASH -> "BK-"
            PaymentGateway.NAGAD -> "NG-"
            PaymentGateway.ROCKET -> "RK-"
            PaymentGateway.CARD -> "CARD-"
            PaymentGateway.CASH -> "CASH-"
        }
        val txnId = prefix + (100000..999999).random()
        val pStatus = when (gateway) {
            PaymentGateway.BKASH -> PaymentStatus.PAID_BKASH
            PaymentGateway.NAGAD -> PaymentStatus.PAID_NAGAD
            PaymentGateway.ROCKET -> PaymentStatus.PAID_BKASH
            PaymentGateway.CARD -> PaymentStatus.PAID_CARD
            PaymentGateway.CASH -> PaymentStatus.PAID_CASH
        }

        val updatedBooking = booking.copy(
            paymentStatus = pStatus,
            transactionId = txnId,
            paymentMethod = gateway.displayName
        )
        current[index] = updatedBooking
        _bookings.value = current

        val paymentRecord = PaymentRecord(
            transactionId = txnId,
            rideId = bookingId,
            passengerName = booking.passengerName,
            amount = booking.fare,
            gateway = gateway,
            accountNumber = accountPhone.ifEmpty { "ক্যাশ পরিশোধ (হাতে হাতে)" },
            receiptNumber = "REC-" + (10000..99999).random()
        )
        _payments.value = listOf(paymentRecord) + _payments.value

        postNotification(
            title = if (_isBengali.value) "ভাড়া পরিশোধ সম্পন্ন: ৳${booking.fare.toInt()}" else "Fare Paid: ৳${booking.fare.toInt()}",
            message = if (_isBengali.value)
                "${gateway.displayName}-এর মাধ্যমে ৳${booking.fare.toInt()} চালককে পরিশোধ করা হয়েছে।"
            else
                "Payment completed via ${gateway.displayName}. Txn: $txnId",
            type = NotificationType.PAYMENT_SUCCESS,
            relatedRideId = bookingId
        )

        return Result.success(paymentRecord)
    }

    // Chat
    fun sendMessage(rideId: String, messageText: String) {
        val user = _currentUser.value
        val chat = ChatMessage(
            id = UUID.randomUUID().toString(),
            rideId = rideId,
            senderId = user.id,
            senderRole = user.role,
            senderName = user.name,
            message = messageText
        )
        _chatMessages.value = _chatMessages.value + chat

        // Realistic Rajshahi driver response
        if (user.role == UserRole.PASSENGER) {
            scope.launch {
                delay(1200)
                val replies = listOf(
                    "জি ভাইয়া, আমি জিরো পয়েন্ট পার হয়েছি, ২ মিনিটে আসছি।",
                    "কাজলা গেটেই আছি ভাই, রিকশা নিয়ে সামনে আসছি।",
                    "লক্ষ্মীপুর মোড়ে একটু জ্যামে আছি, ১ মিনিট অপেক্ষা করুন ভাইয়া।",
                    "জি ভাইয়া, চলে এসেছি।"
                )
                val replyMsg = replies.random()
                val driverReply = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    rideId = rideId,
                    senderId = demoDriver.id,
                    senderRole = UserRole.DRIVER,
                    senderName = demoDriver.name,
                    message = replyMsg
                )
                _chatMessages.value = _chatMessages.value + driverReply
            }
        }
    }

    fun setActiveRide(rideId: String?) {
        _activeRideId.value = rideId
    }

    fun postNotification(title: String, message: String, type: NotificationType, relatedRideId: String? = null) {
        val notif = AppNotification(
            id = UUID.randomUUID().toString(),
            title = title,
            message = message,
            type = type,
            relatedRideId = relatedRideId
        )
        _notifications.value = listOf(notif) + _notifications.value
    }

    fun markNotificationAsRead(id: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
    }

    fun clearNotifications() {
        _notifications.value = emptyList()
    }

    // Rickshaw Driver Registration Management
    fun registerRickshawDriver(registration: RickshawDriverRegistration): Result<RickshawDriverRegistration> {
        val current = _registeredDrivers.value.toMutableList()
        current.add(0, registration)
        _registeredDrivers.value = current

        postNotification(
            title = if (_isBengali.value) "নতুন রিকশাচালক নিবন্ধিত!" else "New Driver Registered!",
            message = if (_isBengali.value)
                "${registration.fullName} (${registration.phone}) সফলভাবে নিবন্ধিত হয়েছেন। স্ট্যান্ড: ${registration.assignedStand}"
            else
                "${registration.fullName} (${registration.phone}) registered for ${registration.assignedStand}.",
            type = NotificationType.SYSTEM_ALERT
        )
        return Result.success(registration)
    }

    fun verifyDriver(driverId: String) {
        _registeredDrivers.value = _registeredDrivers.value.map {
            if (it.id == driverId) it.copy(isVerified = !it.isVerified) else it
        }
    }

    fun deleteDriver(driverId: String) {
        _registeredDrivers.value = _registeredDrivers.value.filter { it.id != driverId }
    }

    // Lookup / Track driver telemetry by phone number
    fun trackDriverByPhone(phoneQuery: String): DriverLocation? {
        val clean = phoneQuery.trim().replace("-", "").replace(" ", "").replace("+88", "")
        if (clean.length < 3) return null

        val matched = _registeredDrivers.value.firstOrNull {
            it.phone.replace("-", "").replace(" ", "").replace("+88", "").contains(clean)
        }

        val standCoords = mapOf(
            "সাহেব বাজার জিরো পয়েন্ট" to Pair(24.3686, 88.6015),
            "রাবি কাজলা গেট" to Pair(24.3680, 88.6360),
            "তালাইমারী মোড় (রুয়েট)" to Pair(24.3635, 88.6280),
            "শিরোইল রেল স্টেশন" to Pair(24.3745, 88.6040),
            "লক্ষ্মীপুর মোড় (মেডিকেল)" to Pair(24.3710, 88.5830),
            "ভদ্রা মোড় স্ট্যান্ড" to Pair(24.3715, 88.6180),
            "উপশহর পানির ট্যাংক" to Pair(24.3820, 88.5990),
            "পদ্মা গার্ডেন" to Pair(24.3600, 88.5920),
            "নিউ মার্কেট" to Pair(24.3710, 88.6080),
            "কোর্ট চত্বর" to Pair(24.3615, 88.5720)
        )

        if (matched != null) {
            val coords = standCoords.entries.firstOrNull { matched.assignedStand.contains(it.key) }?.value
                ?: Pair(24.3686, 88.6015)
            val hash = Math.abs(clean.hashCode())
            val latOffset = ((hash % 10) - 5) * 0.00015
            val lngOffset = (((hash / 10) % 10) - 5) * 0.00015

            return DriverLocation(
                driverId = matched.id,
                driverName = matched.fullName,
                latitude = coords.first + latOffset,
                longitude = coords.second + lngOffset,
                speedKmh = (14f + (hash % 8)).toFloat(),
                headingDegrees = ((hash % 36) * 10).toFloat(),
                isSharingLocation = true,
                vehiclePlate = "রাজশাহী-${matched.rccTokenNumber.takeLast(6)}",
                driverTokenNumber = matched.rccTokenNumber,
                vehicleType = matched.vehicleType,
                driverPhone = matched.phone,
                currentZone = matched.assignedStand,
                nearbyStand = matched.assignedStand,
                lastUpdatedMillis = System.currentTimeMillis()
            )
        }

        // Dynamically resolve any other entered mobile number in Rajshahi
        if (clean.length >= 6) {
            val hash = Math.abs(clean.hashCode())
            val stands = standCoords.entries.toList()
            val entry = stands[hash % stands.size]
            return DriverLocation(
                driverId = "drv_$clean",
                driverName = "রিকশাচালক ($phoneQuery)",
                latitude = entry.value.first + (((hash % 7) - 3) * 0.0002),
                longitude = entry.value.second + ((((hash / 7) % 7) - 3) * 0.0002),
                speedKmh = (12f + (hash % 10)).toFloat(),
                headingDegrees = (hash % 360).toFloat(),
                isSharingLocation = true,
                vehiclePlate = "রাজশাহী-টোকেন-${(hash % 90) + 10}",
                driverTokenNumber = "সিসিক টোকেন: ${(hash % 90) + 10}",
                vehicleType = if (hash % 2 == 0) "সিসিক ব্যাটারি অটো" else "চার্জার ব্যাটারি রিকশা",
                driverPhone = phoneQuery,
                currentZone = entry.key,
                nearbyStand = entry.key,
                lastUpdatedMillis = System.currentTimeMillis()
            )
        }
        return null
    }

    // Rajshahi city landmark distance calculation in km (Bidirectional, accurate for Rajshahi Metro)
    fun calculateRajshahiDistance(pickup: String, dropoff: String): Double {
        val p = pickup.lowercase().trim()
        val d = dropoff.lowercase().trim()

        if (p.isEmpty() || d.isEmpty()) return 2.5
        if (p == d) return 1.0

        // Landmark point resolution supporting Bengali & English / Banglish keywords
        fun resolvePoint(str: String): Int {
            return when {
                str.contains("জিরো") || str.contains("সাহেব") || str.contains("zero") || str.contains("saheb") || str.contains("বাটার") -> 0
                str.contains("কাজলা") || str.contains("রাবি") || str.contains("বিশ্ববিদ্যালয়") || str.contains("বিশ্ববিদ্যালয়") || str.contains("kazla") || str.contains("ru") || str.contains("university") -> 1
                str.contains("তালাইমারী") || str.contains("রুয়েট") || str.contains("talaimari") || str.contains("ruet") -> 2
                str.contains("শিরোইল") || str.contains("স্টেশন") || str.contains("রেল") || str.contains("shiroil") || str.contains("station") || str.contains("train") -> 3
                str.contains("ভদ্রা") || str.contains("bhadra") -> 4
                str.contains("লক্ষ্মীপুর") || str.contains("মেডিকেল") || str.contains("হাসপাতাল") || str.contains("laxmipur") || str.contains("medical") || str.contains("rmch") -> 5
                str.contains("কোর্ট") || str.contains("সিঅ্যান্ডবি") || str.contains("court") || str.contains("c&b") || str.contains("কাজীহাটা") -> 6
                str.contains("উপশহর") || str.contains("uposhahar") || str.contains("হাউজিং") -> 7
                str.contains("মার্কেট") || str.contains("আলুপট্টি") || str.contains("market") || str.contains("alupatti") || str.contains("রানী বাজার") -> 8
                str.contains("পদ্মা") || str.contains("টি-বাঁধ") || str.contains("padma") || str.contains("t-bandh") -> 9
                str.contains("নওদাপাড়া") || str.contains("নওদাপাড়া") || str.contains("শালবাগান") || str.contains("nawdapara") || str.contains("shalbagan") -> 10
                str.contains("বিনোদপুর") || str.contains("binodpur") -> 11
                str.contains("বর্ণালী") || str.contains("কাদিরগঞ্জ") || str.contains("bornali") -> 12
                str.contains("বহরমপুর") || str.contains("রেলগেট") || str.contains("হড়গ্রাম") || str.contains("baharampur") -> 13
                else -> -1
            }
        }

        val pId = resolvePoint(p)
        val dId = resolvePoint(d)

        // Exact pairs required for compatibility and standard routes
        if ((pId == 0 && dId == 1) || (pId == 1 && dId == 0)) return 4.2 // Zero Point <-> RU Kazla (65 Tk)
        if ((pId == 3 && dId == 5) || (pId == 5 && dId == 3)) return 3.2 // Shiroil Station <-> Laxmipur Medical (50 Tk)
        if ((pId == 2 && dId == 0) || (pId == 0 && dId == 2)) return 3.6 // Talaimari RUET <-> Zero Point (55 Tk)

        // If both points are known Rajshahi landmarks, lookup real road distances (km)
        if (pId != -1 && dId != -1) {
            if (pId == dId) return 1.0

            val minId = Math.min(pId, dId)
            val maxId = Math.max(pId, dId)
            val pairKey = "$minId-$maxId"

            val distanceTable = mapOf(
                "0-1" to 4.2,  // Zero Point <-> RU Kazla
                "0-2" to 3.6,  // Zero Point <-> Talaimari
                "0-3" to 1.9,  // Zero Point <-> Shiroil Station
                "0-4" to 2.8,  // Zero Point <-> Bhadra
                "0-5" to 2.2,  // Zero Point <-> Laxmipur Medical
                "0-6" to 3.4,  // Zero Point <-> Court / C&B
                "0-7" to 2.3,  // Zero Point <-> Uposhahar
                "0-8" to 0.9,  // Zero Point <-> New Market
                "0-9" to 1.4,  // Zero Point <-> Padma Garden
                "0-10" to 5.2, // Zero Point <-> Nawdapara
                "0-11" to 4.8, // Zero Point <-> Binodpur
                "0-12" to 1.6, // Zero Point <-> Bornali
                "0-13" to 3.1, // Zero Point <-> Baharampur

                "1-2" to 1.2,  // RU Kazla <-> Talaimari
                "1-3" to 3.8,  // RU Kazla <-> Shiroil Station
                "1-4" to 2.5,  // RU Kazla <-> Bhadra
                "1-5" to 5.8,  // RU Kazla <-> Laxmipur Medical
                "1-6" to 7.1,  // RU Kazla <-> Court
                "1-7" to 4.6,  // RU Kazla <-> Uposhahar
                "1-8" to 3.9,  // RU Kazla <-> New Market
                "1-9" to 4.8,  // RU Kazla <-> Padma Garden
                "1-10" to 5.9, // RU Kazla <-> Nawdapara
                "1-11" to 1.1, // RU Kazla <-> Binodpur
                "1-12" to 4.9, // RU Kazla <-> Bornali
                "1-13" to 6.8, // RU Kazla <-> Baharampur

                "2-3" to 3.2,  // Talaimari <-> Shiroil Station
                "2-4" to 2.1,  // Talaimari <-> Bhadra
                "2-5" to 4.9,  // Talaimari <-> Laxmipur Medical
                "2-6" to 6.4,  // Talaimari <-> Court
                "2-7" to 3.9,  // Talaimari <-> Uposhahar
                "2-8" to 3.2,  // Talaimari <-> New Market
                "2-9" to 3.8,  // Talaimari <-> Padma Garden
                "2-10" to 5.4, // Talaimari <-> Nawdapara
                "2-11" to 1.8, // Talaimari <-> Binodpur
                "2-12" to 4.1, // Talaimari <-> Bornali
                "2-13" to 5.9, // Talaimari <-> Baharampur

                "3-4" to 1.3,  // Shiroil Station <-> Bhadra
                "3-5" to 3.2,  // Shiroil Station <-> Laxmipur Medical
                "3-6" to 4.3,  // Shiroil Station <-> Court
                "3-7" to 1.5,  // Shiroil Station <-> Uposhahar
                "3-8" to 1.7,  // Shiroil Station <-> New Market
                "3-9" to 2.7,  // Shiroil Station <-> Padma Garden
                "3-10" to 3.6, // Shiroil Station <-> Nawdapara
                "3-11" to 4.4, // Shiroil Station <-> Binodpur
                "3-12" to 1.4, // Shiroil Station <-> Bornali
                "3-13" to 3.8, // Shiroil Station <-> Baharampur

                "4-5" to 4.1,  // Bhadra <-> Laxmipur Medical
                "4-6" to 5.2,  // Bhadra <-> Court
                "4-7" to 2.2,  // Bhadra <-> Uposhahar
                "4-8" to 2.4,  // Bhadra <-> New Market
                "4-9" to 3.5,  // Bhadra <-> Padma Garden
                "4-10" to 3.9, // Bhadra <-> Nawdapara
                "4-11" to 3.1, // Bhadra <-> Binodpur
                "4-12" to 2.5, // Bhadra <-> Bornali
                "4-13" to 4.6, // Bhadra <-> Baharampur

                "5-6" to 2.4,  // Laxmipur Medical <-> Court
                "5-7" to 2.9,  // Laxmipur Medical <-> Uposhahar
                "5-8" to 2.0,  // Laxmipur Medical <-> New Market
                "5-9" to 2.3,  // Laxmipur Medical <-> Padma Garden
                "5-10" to 5.6, // Laxmipur Medical <-> Nawdapara
                "5-11" to 6.4, // Laxmipur Medical <-> Binodpur
                "5-12" to 1.5, // Laxmipur Medical <-> Bornali
                "5-13" to 1.6, // Laxmipur Medical <-> Baharampur

                "6-7" to 3.8,  // Court <-> Uposhahar
                "6-8" to 3.0,  // Court <-> New Market
                "6-9" to 2.6,  // Court <-> Padma Garden
                "6-10" to 6.8, // Court <-> Nawdapara
                "6-11" to 7.8, // Court <-> Binodpur
                "6-12" to 2.8, // Court <-> Bornali
                "6-13" to 1.7, // Court <-> Baharampur

                "7-8" to 1.8,  // Uposhahar <-> New Market
                "7-9" to 3.1,  // Uposhahar <-> Padma Garden
                "7-10" to 2.8, // Uposhahar <-> Nawdapara
                "7-11" to 5.2, // Uposhahar <-> Binodpur
                "7-12" to 1.2, // Uposhahar <-> Bornali
                "7-13" to 3.4, // Uposhahar <-> Baharampur

                "8-9" to 1.5,  // New Market <-> Padma Garden
                "8-10" to 4.9, // New Market <-> Nawdapara
                "8-11" to 4.5, // New Market <-> Binodpur
                "8-12" to 1.7, // New Market <-> Bornali
                "8-13" to 3.3, // New Market <-> Baharampur

                "9-10" to 6.2, // Padma Garden <-> Nawdapara
                "9-11" to 5.4, // Padma Garden <-> Binodpur
                "9-12" to 2.6, // Padma Garden <-> Bornali
                "9-13" to 3.2, // Padma Garden <-> Baharampur

                "10-11" to 6.5,// Nawdapara <-> Binodpur
                "10-12" to 2.9,// Nawdapara <-> Bornali
                "10-13" to 4.8,// Nawdapara <-> Baharampur

                "11-12" to 5.5,// Binodpur <-> Bornali
                "11-13" to 7.4,// Binodpur <-> Baharampur

                "12-13" to 2.2 // Bornali <-> Baharampur
            )
            val dist = distanceTable[pairKey]
            if (dist != null) return dist
        }

        // For any custom addresses or arbitrary typed locations:
        // Compute deterministic variation based on input text length & character hash
        val pHash = Math.abs(p.hashCode())
        val dHash = Math.abs(d.hashCode())
        val combo = Math.abs(pHash xor dHash)
        val dynamicKm = 1.6 + ((combo % 42) / 10.0) // varies smoothly between 1.6 km and 5.8 km
        return Math.round(dynamicKm * 10.0) / 10.0
    }

    // Public helper to calculate estimated distance and fare at 15 Taka per km (or 25 Tk/km for Reserve)
    fun getEstimatedDistanceAndFare(pickup: String, dropoff: String, isReserve: Boolean = false): Pair<Double, Double> {
        val dist = calculateRajshahiDistance(pickup, dropoff)
        val rate = if (isReserve) 25.0 else 15.0
        val minFare = if (isReserve) 40.0 else 15.0
        val raw = dist * rate
        val fare = (Math.round(raw / 5.0) * 5.0).coerceAtLeast(minFare)
        return dist to fare
    }

    private fun seedInitialRajshahiData() {
        val now = System.currentTimeMillis()
        val oneHourLater = now + (65 * 60 * 1000)
        val twoHoursLater = now + (130 * 60 * 1000)

        val ride1 = RideBooking(
            id = "ride_seed_01",
            bookingReference = "RAJ-4812",
            passengerId = demoPassenger.id,
            passengerName = demoPassenger.name,
            passengerPhone = demoPassenger.phone,
            driverId = demoDriver.id,
            driverName = demoDriver.name,
            driverPhone = demoDriver.phone,
            driverTokenNumber = "সিসিক টোকেন: লাল-০৮",
            vehicleModel = "সিসিক অনুমোদিত ইজিবাইক অটো (লাল শিফট)",
            vehiclePlate = "রাজশাহী-অটো-০৮",
            vehicleCategory = VehicleCategory.BATTERY_AUTO,
            operatingZone = "রাজশাহী মহানগর - সাহেব বাজার ও জিরো পয়েন্ট এলাকা",
            pickupLocation = "সাহেব বাজার জিরো পয়েন্ট স্ট্যান্ড",
            dropoffLocation = "রাজশাহী বিশ্ববিদ্যালয় (কাজলা গেট)",
            distanceKm = 4.2,
            estimatedMinutes = 16,
            fare = 65.0,
            isAdvanceBooking = true,
            scheduledTimeMillis = oneHourLater,
            bookedAtMillis = now - (15 * 60 * 1000),
            status = RideStatus.COMPLETED,
            paymentStatus = PaymentStatus.PAID_BKASH,
            transactionId = "BK-498210",
            paymentMethod = "bKash (বিকাশ)",
            passengerCount = 3,
            luggageDetails = "বইয়ের ব্যাগ ও ডকুমেন্টস",
            specialNotes = "কাজলা গেটের ভেতরে ড্রপ করবেন",
            progressFraction = 1.0f,
            qrCodeData = "SAFARGO|RAJSHAHI|REF:RAJ-4812|PASS:মো. সায়মন|FARE:৳65"
        )

        val ride2 = RideBooking(
            id = "ride_seed_02",
            bookingReference = "RAJ-9120",
            passengerId = "pass_02",
            passengerName = "অধ্যাপক ড. রফিকুল ইসলাম",
            passengerPhone = "+880 1912-334455",
            driverId = "drv_02",
            driverName = "কাশেম আলী (অটোচালক)",
            driverPhone = "+880 1610-998877",
            driverTokenNumber = "সিসিক টোকেন: সবুজ-২১",
            vehicleModel = "সিসিক চার্জার রিকশা",
            vehiclePlate = "রাজশাহী-রিকশা-২১",
            vehicleCategory = VehicleCategory.CHARGER_RICKSHAW,
            operatingZone = "রাজশাহী মহানগর - লক্ষ্মীপুর ও মেডিকেল কলেজ এলাকা",
            pickupLocation = "শিরোইল রেলওয়ে স্টেশন ১ নং গেট",
            dropoffLocation = "লক্ষ্মীপুর মোড় (রাজশাহী মেডিকেল কলেজ হাসপাতাল)",
            distanceKm = 3.2,
            estimatedMinutes = 14,
            fare = 50.0,
            isAdvanceBooking = true,
            scheduledTimeMillis = twoHoursLater,
            bookedAtMillis = now - (45 * 60 * 1000),
            status = RideStatus.PENDING,
            paymentStatus = PaymentStatus.UNPAID,
            passengerCount = 2,
            specialNotes = "ট্রেন থেকে নেমে সাথে সাথে উঠতে চাই",
            progressFraction = 0f,
            qrCodeData = "SAFARGO|RAJSHAHI|REF:RAJ-9120|FARE:৳50"
        )

        val ride3 = RideBooking(
            id = "ride_seed_03",
            bookingReference = "RAJ-3301",
            passengerId = demoPassenger.id,
            passengerName = demoPassenger.name,
            passengerPhone = demoPassenger.phone,
            driverId = demoDriver.id,
            driverName = demoDriver.name,
            driverPhone = demoDriver.phone,
            driverTokenNumber = "সিসিক টোকেন: লাল-০৮",
            vehicleModel = demoDriver.vehicleModel,
            vehiclePlate = demoDriver.vehiclePlateNumber,
            vehicleCategory = VehicleCategory.BATTERY_AUTO,
            operatingZone = "রাজশাহী মহানগর - উপশহর ও নিউ মার্কেট এলাকা",
            pickupLocation = "উপশহর পানির ট্যাংক মোড়",
            dropoffLocation = "পদ্মা গার্ডেন ও টি-বাঁধ রিভারসাইড",
            distanceKm = 2.1,
            estimatedMinutes = 10,
            fare = 30.0,
            isAdvanceBooking = false,
            scheduledTimeMillis = now - (90 * 60 * 1000),
            bookedAtMillis = now - (120 * 60 * 1000),
            status = RideStatus.COMPLETED,
            paymentStatus = PaymentStatus.PAID_CASH,
            transactionId = "CASH-3301",
            paymentMethod = "Cash on Trip (নগদ)",
            passengerCount = 2,
            driverRating = 5.0f,
            progressFraction = 1.0f,
            qrCodeData = "SAFARGO|RAJSHAHI|REF:RAJ-3301|COMPLETED|FARE:৳30"
        )

        _bookings.value = listOf(ride1, ride2, ride3)
        _activeRideId.value = null

        _chatMessages.value = listOf(
            ChatMessage(
                id = "chat_1",
                rideId = ride1.id,
                senderId = demoPassenger.id,
                senderRole = UserRole.PASSENGER,
                senderName = demoPassenger.name,
                message = "আসসালামু আলাইকুম রহিম ভাই, আমি জিরো পয়েন্ট গোলচত্বরে দাঁড়িয়ে আছি।"
            ),
            ChatMessage(
                id = "chat_2",
                rideId = ride1.id,
                senderId = demoDriver.id,
                senderRole = UserRole.DRIVER,
                senderName = demoDriver.name,
                message = "ওয়ালাইকুম আসসালাম ভাই, আলুপট্টি পার হচ্ছি, ১ মিনিটে মোড়েই পাবেন।"
            )
        )

        _notifications.value = listOf(
            AppNotification(
                id = "notif_1",
                title = "রাজশাহী অটো বুকিং নিশ্চিত হয়েছে (RAJ-4812)",
                message = "আপনার অগ্রিম অটোরিকশা বুকিং গৃহীত হয়েছে। চালক: মো. রহিম মিয়া (সিসিক টোকেন: লাল-০৮)।",
                type = NotificationType.BOOKING_CONFIRMED,
                relatedRideId = ride1.id
            ),
            AppNotification(
                id = "notif_2",
                title = "ভাড়া পরিশোধ সফল (বিকাশ: ৳৬৫)",
                message = "বিকাশের মাধ্যমে অটোরিকশার ভাড়া পরিশোধ করা হয়েছে। ট্রানজেকশন: BK-498210",
                type = NotificationType.PAYMENT_SUCCESS,
                relatedRideId = ride1.id
            )
        )

        _payments.value = listOf(
            PaymentRecord(
                transactionId = "BK-498210",
                rideId = ride1.id,
                passengerName = ride1.passengerName,
                amount = ride1.fare,
                gateway = PaymentGateway.BKASH,
                accountNumber = "01711-234567",
                receiptNumber = "REC-49102"
            ),
            PaymentRecord(
                transactionId = "CASH-3301",
                rideId = ride3.id,
                passengerName = ride3.passengerName,
                amount = ride3.fare,
                gateway = PaymentGateway.CASH,
                accountNumber = "হাতে হাতে নগদ",
                receiptNumber = "REC-11042"
            )
        )
    }

    private fun startLiveRajshahiTelemetrySimulation() {
        scope.launch {
            var step = 0
            while (true) {
                delay(3000)
                step++
                val cur = _driverLocation.value
                if (cur.isSharingLocation) {
                    val latOffset = (Math.sin(step * 0.15) * 0.0006)
                    val lngOffset = (Math.cos(step * 0.15) * 0.0008)
                    val speed = (18f + (Math.sin(step * 0.4) * 4f)).toFloat().coerceIn(14f, 24f)
                    val heading = ((step * 18) % 360).toFloat()

                    _driverLocation.value = cur.copy(
                        latitude = 24.3686 + latOffset,
                        longitude = 88.6015 + lngOffset,
                        speedKmh = speed,
                        headingDegrees = heading,
                        lastUpdatedMillis = System.currentTimeMillis()
                    )

                    // Animate progress of active ride
                    val activeId = _activeRideId.value
                    if (activeId != null) {
                        val currentBookings = _bookings.value.toMutableList()
                        val idx = currentBookings.indexOfFirst { it.id == activeId }
                        if (idx != -1) {
                            val r = currentBookings[idx]
                            if (r.status == RideStatus.ACCEPTED || r.status == RideStatus.IN_TRANSIT) {
                                val newProgress = (r.progressFraction + 0.03f).coerceAtMost(0.98f)
                                currentBookings[idx] = r.copy(progressFraction = newProgress)
                                _bookings.value = currentBookings
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TransportRepository? = null

        fun getInstance(): TransportRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TransportRepository().also { INSTANCE = it }
            }
        }
    }
}
