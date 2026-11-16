package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.platform.LocalContext
import com.example.R
import com.example.data.model.*
import com.example.ui.components.AutoRickshawIcon
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdminDashboardScreen(
    bookings: List<RideBooking>,
    payments: List<PaymentRecord>,
    driverLocation: DriverLocation,
    currentOperatingZone: String,
    availableZones: List<String>,
    registeredDrivers: List<RickshawDriverRegistration>,
    isBengali: Boolean,
    onChangeZone: (String) -> Unit,
    onViewBookingSlip: (RideBooking) -> Unit,
    onVerifyDriver: (String) -> Unit,
    onDeleteDriver: (String) -> Unit,
    onTrackDriverByPhone: (String) -> Unit,
    onOpenDriverRegistration: () -> Unit
) {
    val context = LocalContext.current
    var adminTab by remember { mutableIntStateOf(0) } // 0: Rides & Confirmations, 1: Registered Rickshaw Pullers
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") }
    var driverSearchQuery by remember { mutableStateOf("") }
    var driverVehicleFilter by remember { mutableStateOf("ALL") }
    var isZoneMenuOpen by remember { mutableStateOf(false) }

    val totalRevenue = bookings.sumOf { it.fare }
    val advanceBookingsCount = bookings.count { it.isAdvanceBooking }
    val activeCount = bookings.count { it.status == RideStatus.ACCEPTED || it.status == RideStatus.IN_TRANSIT }
    val verifiedDriversCount = registeredDrivers.count { it.isVerified }

    val filteredDrivers = registeredDrivers.filter { d ->
        val q = driverSearchQuery.trim()
        val matchesQuery = q.isEmpty() ||
                d.fullName.contains(q, ignoreCase = true) ||
                d.phone.contains(q, ignoreCase = true) ||
                d.nidNumber.contains(q, ignoreCase = true) ||
                d.rccTokenNumber.contains(q, ignoreCase = true) ||
                d.assignedStand.contains(q, ignoreCase = true) ||
                d.garageArea.contains(q, ignoreCase = true)

        val matchesVehicle = when (driverVehicleFilter) {
            "AUTO" -> d.vehicleType.contains("অটো")
            "CHARGER" -> d.vehicleType.contains("চার্জার")
            "PEDAL" -> d.vehicleType.contains("প্যাডেল")
            else -> true
        }
        matchesQuery && matchesVehicle
    }

    val filteredBookings = bookings.filter { b ->
        val matchesQuery = b.passengerName.contains(searchQuery, ignoreCase = true) ||
                b.bookingReference.contains(searchQuery, ignoreCase = true) ||
                b.pickupLocation.contains(searchQuery, ignoreCase = true) ||
                b.dropoffLocation.contains(searchQuery, ignoreCase = true) ||
                b.driverTokenNumber.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "PENDING" -> b.status == RideStatus.PENDING
            "ACTIVE" -> b.status == RideStatus.ACCEPTED || b.status == RideStatus.IN_TRANSIT
            "COMPLETED" -> b.status == RideStatus.COMPLETED
            "PAID" -> b.paymentStatus != PaymentStatus.UNPAID
            else -> true
        }
        matchesQuery && matchesFilter
    }

    val timeFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLight)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Admin Header Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_header_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White,
                                shadowElevation = 2.dp,
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(62.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_safargo_logo),
                                    contentDescription = "SafarGo Official Logo",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(2.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isBengali) "ট্রান্সপোর্ট অ্যাডমিন প্যানেল" else "Area Fleet Admin Console",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "SafarGo Rajshahi Fleet",
                                    color = EmeraldContainer,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GoldAccent
                        ) {
                            Text(
                                text = "MEJAN ADMIN",
                                color = Slate900,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Operating Zone Switcher
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isZoneMenuOpen = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Place, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("বর্তমান অপারেটিং এলাকা:", fontSize = 10.sp, color = EmeraldContainer)
                                    Text(currentOperatingZone, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
                        }
                    }

                    DropdownMenu(
                        expanded = isZoneMenuOpen,
                        onDismissRequest = { isZoneMenuOpen = false }
                    ) {
                        availableZones.forEach { zone ->
                            DropdownMenuItem(
                                text = { Text(zone, fontSize = 13.sp) },
                                onClick = {
                                    onChangeZone(zone)
                                    isZoneMenuOpen = false
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Metrics Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MetricCard(
                            title = if (isBengali) "মোট লোকাল ট্রিপ" else "Total Trips",
                            value = "${bookings.size} টি",
                            icon = Icons.Default.ElectricRickshaw,
                            color = EmeraldLight,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            title = if (isBengali) "মোট ভাড়া জমা" else "Total Fare",
                            value = "৳${totalRevenue.toInt()}",
                            icon = Icons.Default.MonetizationOn,
                            color = GoldAccent,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MetricCard(
                            title = if (isBengali) "নিবন্ধিত রিকশাচালক" else "Registered Drivers",
                            value = "${registeredDrivers.size} জন",
                            icon = Icons.Default.Badge,
                            color = GoldAccent,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            title = if (isBengali) "অন-রোড চলমান" else "Active on Road",
                            value = "$activeCount টি",
                            icon = Icons.Default.Speed,
                            color = SuccessGreen,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Live Rickshaw Radar & Stand Telemetry
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBengali) "এলাকার রিকশা ও অটো লাইভ রাডার" else "Area Rickshaw & Auto Radar",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Slate900
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (driverLocation.isSharingLocation) EmeraldContainer else ErrorRed.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = if (driverLocation.isSharingLocation) "GPS ACTIVE" else "GPS OFFLINE",
                                color = if (driverLocation.isSharingLocation) EmeraldDark else ErrorRed,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Slate50,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldDark),
                                contentAlignment = Alignment.Center
                            ) {
                                AutoRickshawIcon(contentDescription = null, tint = GoldAccent, modifier = Modifier.size(22.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${driverLocation.driverName} • ${driverLocation.driverTokenNumber}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "স্ট্যান্ড: ${driverLocation.nearbyStand} • গতি: ${String.format("%.1f", driverLocation.speedKmh)} কিমি/ঘণ্টা",
                                    fontSize = 11.sp,
                                    color = Slate700
                                )
                            }
                        }
                    }
                }
            }
        }

        // Admin Control Tab Switcher & Action
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (adminTab == 0) EmeraldPrimary else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { adminTab = 0 }
                                .testTag("admin_tab_bookings")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.ReceiptLong,
                                    contentDescription = null,
                                    tint = if (adminTab == 0) Color.White else Slate700,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isBengali) "ট্রিপ ডেটা (${bookings.size})" else "Trips (${bookings.size})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (adminTab == 0) Color.White else Slate700
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (adminTab == 1) EmeraldPrimary else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { adminTab = 1 }
                                .testTag("admin_tab_drivers")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Badge,
                                    contentDescription = null,
                                    tint = if (adminTab == 1) Color.White else Slate700,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isBengali) "রিকশাচালক ডেটা (${registeredDrivers.size})" else "Drivers (${registeredDrivers.size})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (adminTab == 1) Color.White else Slate700
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (adminTab == 0) {
                    // Search Bar with High Contrast Black Placeholder
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                text = if (isBengali) "যাত্রী, টোকেন নং বা স্ট্যান্ড দিয়ে খুঁজুন..." else "Search by name, token #, or stand...",
                                color = Color.Black
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_search_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedPlaceholderColor = Color.Black,
                            unfocusedPlaceholderColor = Color.Black,
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = Color.Black.copy(alpha = 0.5f),
                            cursorColor = EmeraldPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Booking Filter Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("ALL" to "সকল", "PENDING" to "অপেক্ষমাণ", "ACTIVE" to "চলমান", "PAID" to "পেইড", "COMPLETED" to "সম্পন্ন").forEach { (key, label) ->
                            FilterChip(
                                selected = selectedFilter == key,
                                onClick = { selectedFilter = key },
                                label = { Text(label, fontSize = 11.sp) }
                            )
                        }
                    }
                } else {
                    // Registered Rickshaw Pullers Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isBengali) "নিবন্ধিত রিকশা/অটো চালক তালিকা" else "Registered Rickshaw Pullers",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Slate900
                            )
                            Text(
                                text = "RCC টোকেন, এনআইডি এবং মোবাইল লোকেশন ট্র্যাকিং",
                                fontSize = 11.sp,
                                color = Slate700
                            )
                        }

                        Button(
                            onClick = onOpenDriverRegistration,
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("admin_register_driver_btn")
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isBengali) "নতুন চালক" else "New Driver", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Driver Search Bar
                    OutlinedTextField(
                        value = driverSearchQuery,
                        onValueChange = { driverSearchQuery = it },
                        placeholder = {
                            Text(
                                text = if (isBengali) "নাম, ফোন, NID, বা টোকেন দিয়ে খুঁজুন..." else "Search by name, phone, NID, or token...",
                                color = Color.Black
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_driver_search_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedPlaceholderColor = Color.Black,
                            unfocusedPlaceholderColor = Color.Black,
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = Color.Black.copy(alpha = 0.5f),
                            cursorColor = EmeraldPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Vehicle Type Chips for Drivers
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("ALL" to "সকল যানবাহন", "AUTO" to "ব্যাটারি অটো", "CHARGER" to "চার্জার রিকশা", "PEDAL" to "প্যাডেল রিকশা").forEach { (key, label) ->
                            FilterChip(
                                selected = driverVehicleFilter == key,
                                onClick = { driverVehicleFilter = key },
                                label = { Text(label, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            }
        }

        // Tab Content Items
        if (adminTab == 0) {
            // BOOKINGS LIST
            if (filteredBookings.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                            Text("কোনো বুকিং রেকর্ড পাওয়া যায়নি", color = Slate700, fontSize = 13.sp)
                        }
                    }
                }
            } else {
                items(filteredBookings) { booking ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onViewBookingSlip(booking) }
                            .testTag("admin_booking_item_${booking.bookingReference}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = booking.passengerName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = EmeraldContainer
                                        ) {
                                            Text(
                                                text = booking.driverTokenNumber,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = OnEmeraldContainer,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }

                                        if (booking.isReserve) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = GoldAccent.copy(alpha = 0.2f),
                                                border = BorderStroke(1.dp, GoldAccent)
                                            ) {
                                                Text(
                                                    text = "🌟 রিজার্ভ",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Slate900,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "${booking.passengerPhone} • ${booking.vehicleCategory.typeName}",
                                        fontSize = 11.sp,
                                        color = Slate700
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "৳${booking.fare.toInt()}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 16.sp,
                                        color = EmeraldDark
                                    )
                                    Text(
                                        text = if (booking.paymentStatus == PaymentStatus.UNPAID) "অপরিশোধিত" else "পেইড (${booking.paymentMethod})",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (booking.paymentStatus == PaymentStatus.UNPAID) WarningAmber else SuccessGreen
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${booking.pickupLocation} ➔ ${booking.dropoffLocation}",
                                fontSize = 12.sp,
                                color = Slate900,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "শিডিউল: ${timeFormat.format(Date(booking.scheduledTimeMillis))}",
                                    fontSize = 10.sp,
                                    color = Slate700
                                )

                                TextButton(
                                    onClick = { onViewBookingSlip(booking) },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("পূর্ণাঙ্গ কনফার্মেশন ডেটা", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // REGISTERED RICKSHAW DRIVERS LIST
            if (filteredDrivers.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("কোনো নিবন্ধিত রিকশাচালকের তথ্য পাওয়া যায়নি", color = Slate700, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = onOpenDriverRegistration,
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                            ) {
                                Icon(Icons.Default.PersonAdd, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("রিকশাচালক নিবন্ধন করুন")
                            }
                        }
                    }
                }
            } else {
                items(filteredDrivers) { driver ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_driver_item_${driver.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Driver Header: Name, Token, Verified Status
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(EmeraldContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.ElectricRickshaw,
                                            contentDescription = null,
                                            tint = OnEmeraldContainer,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = driver.fullName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Slate900
                                        )
                                        Text(
                                            text = "${driver.vehicleType} • ${driver.experienceYears} বছর অভিজ্ঞতা",
                                            fontSize = 11.sp,
                                            color = Slate700
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (driver.isVerified) SuccessGreen.copy(alpha = 0.15f) else WarningAmber.copy(alpha = 0.15f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            if (driver.isVerified) Icons.Default.Verified else Icons.Default.HourglassEmpty,
                                            contentDescription = null,
                                            tint = if (driver.isVerified) SuccessGreen else WarningAmber,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (driver.isVerified) "যাচাইকৃত" else "অপেক্ষমাণ",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (driver.isVerified) SuccessGreen else WarningAmber
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Identification and Stand details
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Slate100,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text("RCC টোকেন নং", fontSize = 10.sp, color = Slate700)
                                            Text(driver.rccTokenNumber, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                        }
                                        Column {
                                            Text("জাতীয় পরিচয়পত্র (NID)", fontSize = 10.sp, color = Slate700)
                                            Text(driver.nidNumber, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text("স্ট্যান্ড ও গ্যারেজ", fontSize = 10.sp, color = Slate700)
                                            Text("${driver.assignedStand} (${driver.garageArea})", fontSize = 11.sp, color = Slate900)
                                        }
                                        Column {
                                            Text("জরুরি যোগাযোগ", fontSize = 10.sp, color = Slate700)
                                            Text(driver.emergencyContactPhone, fontSize = 11.sp, color = Slate900)
                                        }
                                    }

                                    if (driver.notes.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("নোট: ${driver.notes}", fontSize = 10.sp, color = Slate700)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Driver Action Row: Phone Track, Direct Call, Verify, Delete
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Track by Phone Button
                                Button(
                                    onClick = { onTrackDriverByPhone(driver.phone) },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                                    modifier = Modifier.weight(1f).testTag("track_driver_phone_${driver.id}")
                                ) {
                                    Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("লোকেশন ট্র্যাক", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                // Call Button
                                OutlinedButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${driver.phone}"))
                                        context.startActivity(intent)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                                    modifier = Modifier.weight(0.8f).testTag("call_driver_${driver.id}")
                                ) {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("কল", fontSize = 11.sp, color = Slate900)
                                }

                                if (!driver.isVerified) {
                                    FilledTonalButton(
                                        onClick = { onVerifyDriver(driver.id) },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = SuccessGreen.copy(alpha = 0.2f)),
                                        modifier = Modifier.testTag("verify_driver_${driver.id}")
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                                    }
                                }

                                IconButton(
                                    onClick = { onDeleteDriver(driver.id) },
                                    modifier = Modifier.size(32.dp).testTag("delete_driver_${driver.id}")
                                ) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Developer Credit and Company Verification
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Slate100),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SafarGo Transport Service",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = EmeraldDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Developed by MEJAN",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GoldAccent
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "এলাকাভিত্তিক রিকশা ও অটো ব্যবস্থাপনা এবং জিপিএস ট্র্যাকিং সিস্টেম",
                        fontSize = 10.sp,
                        color = Slate700
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.12f),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = title, fontSize = 10.sp, color = EmeraldContainer)
                Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
