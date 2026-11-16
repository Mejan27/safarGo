package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.*
import com.example.ui.components.AutoRickshawIcon
import com.example.ui.components.LiveTrackingMapCanvas
import com.example.ui.theme.*

@Composable
fun DriverHomeScreen(
    driver: User,
    driverLocation: DriverLocation,
    bookings: List<RideBooking>,
    activeRide: RideBooking?,
    isBengali: Boolean,
    onToggleOnline: (Boolean) -> Unit,
    onToggleLocationSharing: (Boolean) -> Unit,
    onAcceptBooking: (String) -> Unit,
    onUpdateRideStatus: (String, RideStatus) -> Unit,
    onOpenChat: (RideBooking) -> Unit,
    onOpenSlip: (RideBooking) -> Unit,
    onOpenDriverRegistration: () -> Unit
) {
    val pendingBookings = bookings.filter { it.status == RideStatus.PENDING }
    val driverActiveRides = bookings.filter { 
        it.driverId == driver.id && (it.status == RideStatus.ACCEPTED || it.status == RideStatus.DRIVER_ARRIVED || it.status == RideStatus.IN_TRANSIT) 
    }
    val completedTrips = bookings.filter { it.driverId == driver.id && it.status == RideStatus.COMPLETED }
    val totalEarnings = completedTrips.sumOf { it.fare }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLight)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Driver Profile & Rickshaw Shift Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White,
                                shadowElevation = 3.dp,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_safargo_logo),
                                    contentDescription = "SafarGo Driver",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = driver.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                                Text(
                                    text = "${driver.driverTokenNumber.ifEmpty { "টোকেন নং: ০৮" }} • ${driver.vehicleType.ifEmpty { "ব্যাটারি রিকশা" }}",
                                    color = EmeraldContainer,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "স্ট্যান্ড: ${driver.assignedStand.ifEmpty { "কাঁচাবাজার স্ট্যান্ড মোড়" }}",
                                    color = Color.White.copy(alpha = 0.75f),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Shift Online Switch
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (driver.isOnline) "শিফট চালু" else "বন্ধ",
                                color = if (driver.isOnline) SuccessGreen else Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Switch(
                                checked = driver.isOnline,
                                onCheckedChange = onToggleOnline,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = SuccessGreen,
                                    checkedTrackColor = EmeraldContainer
                                ),
                                modifier = Modifier.testTag("driver_online_toggle")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatPill("আজকের জমা/আয়", "৳${totalEarnings.toInt() + 450}")
                        StatPill("আজকের ট্রিপ", "${completedTrips.size + 8} টি")
                        StatPill("রেটিং", "★ 4.9")
                    }
                }
            }
        }

        // Live Location Sharing & Stand Radar Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("driver_gps_sharing_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (driverLocation.isSharingLocation) SuccessGreen else ErrorRed)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (isBengali) "লাইভ লোকেশন ও স্ট্যান্ড অবস্থান শেয়ার" else "Live GPS Location Sharing",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Slate900
                                )
                                Text(
                                    text = if (driverLocation.isSharingLocation) 
                                        "যাত্রীরা ম্যাপে আপনার রিকশা/অটোর অবস্থান দেখতে পাচ্ছেন" 
                                    else 
                                        "অবস্থান শেয়ার বন্ধ রয়েছে",
                                    fontSize = 11.sp,
                                    color = Slate700
                                )
                            }
                        }

                        Switch(
                            checked = driverLocation.isSharingLocation,
                            onCheckedChange = onToggleLocationSharing,
                            modifier = Modifier.testTag("driver_location_sharing_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Slate50,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "এলাকা ও বর্তমান স্ট্যান্ড", fontSize = 10.sp, color = Slate700)
                                Text(
                                    text = "${driverLocation.currentZone}\n(${driverLocation.nearbyStand})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "রিকশার গতি", fontSize = 10.sp, color = Slate700)
                                Text(
                                    text = "${String.format("%.1f", driverLocation.speedKmh)} কিমি/ঘণ্টা",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDark
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Trip Progression Card
        if (driverActiveRides.isNotEmpty()) {
            val currentTrip = driverActiveRides.first()
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("driver_active_trip_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "চলমান রিকশা রাইড #${currentTrip.bookingReference}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = EmeraldDark
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = EmeraldContainer
                            ) {
                                Text(
                                    text = "ভাড়া: ৳${currentTrip.fare.toInt()}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = OnEmeraldContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "যাত্রী: ${currentTrip.passengerName} (${currentTrip.passengerPhone})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${currentTrip.pickupLocation} ➔ ${currentTrip.dropoffLocation}",
                            fontSize = 12.sp,
                            color = Slate700
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Visual Map Preview
                        LiveTrackingMapCanvas(
                            driverLocation = driverLocation,
                            progressFraction = currentTrip.progressFraction,
                            pickupName = currentTrip.pickupLocation,
                            dropoffName = currentTrip.dropoffLocation,
                            estimatedMinutes = currentTrip.estimatedMinutes,
                            distanceKm = currentTrip.distanceKm,
                            modifier = Modifier.height(180.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Trip Progression Buttons
                        when (currentTrip.status) {
                            RideStatus.ACCEPTED -> {
                                Button(
                                    onClick = { onUpdateRideStatus(currentTrip.id, RideStatus.DRIVER_ARRIVED) },
                                    colors = ButtonDefaults.buttonColors(containerColor = WarningAmber),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("driver_arrive_button")
                                ) {
                                    Icon(Icons.Default.PinDrop, contentDescription = null, tint = Slate900)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("আমি গেটে পৌঁছে গেছি (Arrived)", color = Slate900, fontWeight = FontWeight.Bold)
                                }
                            }
                            RideStatus.DRIVER_ARRIVED -> {
                                Button(
                                    onClick = { onUpdateRideStatus(currentTrip.id, RideStatus.IN_TRANSIT) },
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("driver_start_trip_button")
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("রিকশা ছাড়ুন (যাত্রা শুরু - ফি আমানিল্লাহ)", fontWeight = FontWeight.Bold)
                                }
                            }
                            RideStatus.IN_TRANSIT -> {
                                Button(
                                    onClick = { onUpdateRideStatus(currentTrip.id, RideStatus.COMPLETED) },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("driver_complete_trip_button")
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("গন্তব্যে পৌঁছেছি ও ভাড়া সংগ্রহ সম্পন্ন", fontWeight = FontWeight.Bold)
                                }
                            }
                            else -> {}
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { onOpenChat(currentTrip) },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("যাত্রীকে বার্তা দিন")
                            }

                            OutlinedButton(
                                onClick = { onOpenSlip(currentTrip) },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("ভাড়া স্লিপ")
                            }
                        }
                    }
                }
            }
        }

        // Rickshaw Puller Registration Action Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Slate100),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("driver_registration_card")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Badge,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isBengali) "রিকশাচালক ডেটা এন্ট্রি ফর্ম" else "Driver Registration Form",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Slate900
                            )
                            Text(
                                text = "RCC টোকেন, NID ও মোবাইল দিয়ে চালকের ডেটা যোগ করুন",
                                fontSize = 11.sp,
                                color = Slate700
                            )
                        }
                    }

                    Button(
                        onClick = onOpenDriverRegistration,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("driver_mode_register_btn")
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isBengali) "ফর্ম খুলুন" else "Register", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Incoming Local Ride Requests Radar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBengali) "এলাকার নতুন রিকশা/অটো রিকোয়েস্ট" else "Nearby Ride Requests",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate900
                )
                Text(
                    text = "${pendingBookings.size} টি অপেক্ষমাণ",
                    fontSize = 12.sp,
                    color = Slate700
                )
            }
        }

        if (pendingBookings.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Radar, contentDescription = null, tint = EmeraldLight, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isBengali) "এলাকার নতুন যাত্রীর জন্য রাডার চালু রয়েছে" else "Radar is scanning for local requests",
                            fontSize = 13.sp,
                            color = Slate700
                        )
                    }
                }
            }
        } else {
            items(pendingBookings) { req ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("pending_request_${req.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${req.vehicleCategory.typeName}: ${req.passengerName}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    if (req.isReserve) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = GoldAccent.copy(alpha = 0.2f),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent)
                                        ) {
                                            Text(
                                                text = "🌟 পুরো অটো রিজার্ভ",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Slate900,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            Text(
                                text = "৳${req.fare.toInt()}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 17.sp,
                                color = EmeraldDark
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${req.pickupLocation} ➔ ${req.dropoffLocation}",
                            fontSize = 12.sp,
                            color = Slate700
                        )
                        Text(
                            text = if (req.isReserve) 
                                "বুকিং টাইপ: পুরো অটো রিজার্ভ (৫টি সিট) • দূরত্ব: ${String.format("%.1f", req.distanceKm)} কিমি • রেট: ৳২৫/কিমি"
                            else 
                                "যাত্রী: ${req.passengerCount} জন • দূরত্ব: ${String.format("%.1f", req.distanceKm)} কিমি • রেট: ৳১৫/কিমি",
                            fontSize = 11.sp,
                            color = if (req.isReserve) EmeraldDark else Slate700,
                            fontWeight = if (req.isReserve) FontWeight.Bold else FontWeight.Normal
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onAcceptBooking(req.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("accept_ride_button_${req.id}")
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBengali) "রিকশা রিকোয়েস্ট গ্রহণ করুন (Accept)" else "Accept Ride",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Developer footer
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "SafarGo • রিকশা ও অটো ড্রাইভার টার্মিনাল",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = EmeraldDark
                )
                Text(
                    text = "Developed by MEJAN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GoldAccent
                )
            }
        }
    }
}

@Composable
private fun StatPill(title: String, value: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White.copy(alpha = 0.12f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 10.sp, color = EmeraldContainer)
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
