package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.LiveTrackingMapCanvas
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveTrackingScreen(
    ride: RideBooking,
    driverLocation: DriverLocation,
    isBengali: Boolean,
    onBack: () -> Unit,
    onOpenChat: () -> Unit,
    onOpenSlip: () -> Unit,
    onPayFare: () -> Unit,
    onOpenSos: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (isBengali) "লাইভ ভেহিক্যাল ট্র্যাকিং" else "Live Vehicle Tracking",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "বুকিং #${ride.bookingReference} • ${ride.vehiclePlate}",
                            fontSize = 11.sp,
                            color = EmeraldContainer
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = onOpenSos) {
                        Icon(Icons.Default.Sos, contentDescription = "SOS", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EmeraldDark)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceLight)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Live Interactive Radar Map Canvas
            LiveTrackingMapCanvas(
                driverLocation = driverLocation,
                progressFraction = ride.progressFraction,
                pickupName = ride.pickupLocation,
                dropoffName = ride.dropoffLocation,
                estimatedMinutes = ride.estimatedMinutes,
                distanceKm = ride.distanceKm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            // Origin to Destination Live Route Distance & Fare Tracking Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldContainer.copy(alpha = 0.5f)),
                border = BorderStroke(1.5.dp, EmeraldPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("live_route_tracking_fare_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Navigation,
                                contentDescription = null,
                                tint = EmeraldDark,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBengali) "যাত্রার স্থান হতে গন্তব্য পর্যন্ত লাইভ ট্র্যাকিং" else "Origin to Destination Live Route Tracking",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = EmeraldDark
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = GoldAccent
                        ) {
                            Text(
                                text = "১৫৳/কিমি",
                                color = Slate900,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Route Nodes Display
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.TripOrigin, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(30.dp)
                                    .background(EmeraldDark.copy(alpha = 0.5f))
                            )
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "যাত্রার স্থান: ${ride.pickupLocation}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Slate900
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "গন্তব্য স্থান: ${ride.dropoffLocation}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Slate900
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = EmeraldPrimary.copy(alpha = 0.3f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Dynamic Distance & Fare Metrics
                    val coveredDistance = ride.distanceKm * ride.progressFraction.coerceIn(0f, 1f)
                    val remainingDistance = (ride.distanceKm - coveredDistance).coerceAtLeast(0.0)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "স্বয়ংক্রিয় মোট দূরত্ব: ${String.format("%.1f", ride.distanceKm)} কিমি",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Slate900
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "অতিক্রান্ত: ${String.format("%.1f", coveredDistance)} কিমি • বাকি: ${String.format("%.1f", remainingDistance)} কিমি",
                                fontSize = 11.sp,
                                color = Slate700
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "৳${ride.fare.toInt()}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                color = EmeraldDark
                            )
                            Text(
                                text = if (isBengali) "হিসাবকৃত মোট ভাড়া" else "Auto Counted Fare",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDark.copy(alpha = 0.85f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Live Route Progress Bar
                    LinearProgressIndicator(
                        progress = { ride.progressFraction.coerceIn(0.05f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = EmeraldPrimary,
                        trackColor = Color.White
                    )
                }
            }

            // Trip Status Stepper
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isBengali) "যাত্রার পর্যায়ক্রম" else "Trip Progression",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StepIndicator("বুকিং", true)
                        Divider(modifier = Modifier.weight(1f), color = EmeraldLight)
                        StepIndicator("অ্যাসাইন", ride.status != RideStatus.PENDING)
                        Divider(modifier = Modifier.weight(1f), color = if (ride.status == RideStatus.DRIVER_ARRIVED || ride.status == RideStatus.IN_TRANSIT || ride.status == RideStatus.COMPLETED) EmeraldLight else Slate100)
                        StepIndicator("উপস্থিত", ride.status == RideStatus.DRIVER_ARRIVED || ride.status == RideStatus.IN_TRANSIT || ride.status == RideStatus.COMPLETED)
                        Divider(modifier = Modifier.weight(1f), color = if (ride.status == RideStatus.IN_TRANSIT || ride.status == RideStatus.COMPLETED) EmeraldLight else Slate100)
                        StepIndicator("চলমান", ride.status == RideStatus.IN_TRANSIT || ride.status == RideStatus.COMPLETED)
                    }
                }
            }

            // Driver Card with Chat & Call buttons
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(28.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = ride.driverName.ifEmpty { "অভিজ্ঞ চালক" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Slate900
                                )
                                Text(
                                    text = "${ride.vehicleModel} • ★ 4.9",
                                    fontSize = 12.sp,
                                    color = Slate700
                                )
                                Text(
                                    text = ride.vehiclePlate,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = EmeraldDark
                                )
                            }
                        }

                        Row {
                            IconButton(
                                onClick = {},
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(SuccessGreen.copy(alpha = 0.15f))
                            ) {
                                Icon(Icons.Default.Call, contentDescription = "Call", tint = SuccessGreen)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(
                                onClick = onOpenChat,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldPrimary)
                                    .testTag("driver_chat_button")
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = "Chat", tint = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onOpenSlip,
                            colors = ButtonDefaults.buttonColors(containerColor = Slate900),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("view_slip_from_tracking_button")
                        ) {
                            Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("বুকিং কনফার্মেশন", fontSize = 12.sp)
                        }

                        if (ride.paymentStatus == PaymentStatus.UNPAID) {
                            Button(
                                onClick = onPayFare,
                                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("pay_fare_from_tracking_button")
                            ) {
                                Icon(Icons.Default.Payment, contentDescription = null, tint = Slate900, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("ভাড়া পরিশোধ", color = Slate900, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Developer Footer
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "SafarGo Transport Service",
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
private fun StepIndicator(label: String, isActive: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(if (isActive) SuccessGreen else Slate100),
            contentAlignment = Alignment.Center
        ) {
            if (isActive) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
            color = if (isActive) Slate900 else Slate700
        )
    }
}
