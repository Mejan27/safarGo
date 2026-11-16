package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.example.ui.components.AutoRickshawIcon
import com.example.R
import com.example.data.model.*
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

fun getDefaultStandForZone(zone: String): String {
    val z = zone.lowercase()
    return when {
        z.contains("সাহেব বাজার") || z.contains("জিরো") || z.contains("zero") -> "সাহেব বাজার জিরো পয়েন্ট স্ট্যান্ড"
        z.contains("মতিহার") || z.contains("কাজলা") || z.contains("বিশ্ববিদ্যালয়") || z.contains("বিশ্ববিদ্যালয়") || z.contains("ru") || z.contains("kazla") -> "রাবি কাজলা গেট"
        z.contains("তালাইমারী") || z.contains("রুয়েট") || z.contains("talaimari") || z.contains("ruet") -> "তালাইমারী মোড় (রুয়েট)"
        z.contains("বোয়ালিয়া") || z.contains("শিরোইল") || z.contains("স্টেশন") || z.contains("shiroil") || z.contains("station") -> "শিরোইল রেল স্টেশন"
        z.contains("লক্ষ্মীপুর") || z.contains("মেডিকেল") || z.contains("রাজপাড়া") || z.contains("laxmipur") || z.contains("medical") -> "লক্ষ্মীপুর মোড় (মেডিকেল)"
        z.contains("উপশহর") || z.contains("uposhahar") -> "উপশহর পানির ট্যাংক"
        z.contains("কোর্ট") || z.contains("সিঅ্যান্ডবি") || z.contains("court") -> "কোর্ট চত্বর"
        z.contains("ভদ্রা") || z.contains("bhadra") -> "ভদ্রা মোড় স্ট্যান্ড"
        z.contains("শাহ মখদুম") || z.contains("নওদাপাড়া") || z.contains("নওদাপাড়া") || z.contains("nawdapara") -> "নওদাপাড়া স্ট্যান্ড"
        z.contains("পদ্মা") || z.contains("টি-বাঁধ") || z.contains("padma") -> "পদ্মা গার্ডেন"
        z.contains("নিউ মার্কেট") || z.contains("new market") -> "নিউ মার্কেট ও পদ্মা গার্ডেন"
        z.contains("বিনোদপুর") || z.contains("binodpur") -> "বিনোদপুর বাজার"
        z.contains("বহরমপুর") || z.contains("baharampur") -> "বহরমপুর রেলগেট"
        z.contains("বর্ণালী") || z.contains("bornali") -> "বর্ণালী মোড়"
        else -> "সাহেব বাজার জিরো পয়েন্ট স্ট্যান্ড"
    }
}

fun getDefaultDestinationForStand(stand: String): String {
    val s = stand.lowercase()
    return when {
        s.contains("সাহেব") || s.contains("জিরো") || s.contains("zero") -> "রাবি কাজলা গেট"
        s.contains("কাজলা") || s.contains("রাবি") || s.contains("kazla") -> "সাহেব বাজার জিরো পয়েন্ট স্ট্যান্ড"
        s.contains("তালাইমারী") || s.contains("রুয়েট") || s.contains("talaimari") -> "সাহেব বাজার জিরো পয়েন্ট স্ট্যান্ড"
        s.contains("শিরোইল") || s.contains("স্টেশন") || s.contains("station") -> "লক্ষ্মীপুর মেডিকেল"
        s.contains("লক্ষ্মীপুর") || s.contains("মেডিকেল") || s.contains("medical") -> "শিরোইল রেল স্টেশন"
        s.contains("উপশহর") || s.contains("uposhahar") -> "পদ্মা গার্ডেন"
        s.contains("কোর্ট") || s.contains("court") -> "সাহেব বাজার জিরো পয়েন্ট স্ট্যান্ড"
        s.contains("ভদ্রা") || s.contains("bhadra") -> "তালাইমারী মোড় (রুয়েট)"
        s.contains("নওদাপাড়া") || s.contains("নওদাপাড়া") -> "শিরোইল রেল স্টেশন"
        s.contains("পদ্মা") || s.contains("padma") -> "উপশহর পানির ট্যাংক"
        s.contains("নিউ মার্কেট") || s.contains("market") -> "রাবি কাজলা গেট"
        s.contains("বিনোদপুর") || s.contains("binodpur") -> "জিরো পয়েন্ট স্ট্যান্ড"
        s.contains("বহরমপুর") || s.contains("baharampur") -> "লক্ষ্মীপুর মেডিকেল"
        s.contains("বর্ণালী") || s.contains("bornali") -> "শিরোইল রেল স্টেশন"
        else -> "রাবি কাজলা গেট"
    }
}

@Composable
fun PassengerHomeScreen(
    bookings: List<RideBooking>,
    activeRide: RideBooking?,
    currentOperatingZone: String,
    availableZones: List<String>,
    isBengali: Boolean,
    onChangeZone: (String) -> Unit,
    onBookAdvanceRide: (pickup: String, dropoff: String, category: VehicleCategory, scheduledMillis: Long, seats: Int, notes: String, isReserve: Boolean) -> Unit,
    onTrackRide: (String) -> Unit,
    onOpenChat: (RideBooking) -> Unit,
    onOpenSlip: (RideBooking) -> Unit,
    onPayFare: (RideBooking) -> Unit,
    onOpenDriverRegistration: () -> Unit,
    onTrackDriverByPhone: (String) -> Unit
) {
    var areaSearchQuery by remember { mutableStateOf("") }
    var pickupText by remember { mutableStateOf(getDefaultStandForZone(currentOperatingZone)) }
    var dropoffText by remember { mutableStateOf(getDefaultDestinationForStand(getDefaultStandForZone(currentOperatingZone))) }

    // Sync pickup and dropoff automatically when operating zone changes
    LaunchedEffect(currentOperatingZone) {
        val defaultStand = getDefaultStandForZone(currentOperatingZone)
        pickupText = defaultStand
        dropoffText = getDefaultDestinationForStand(defaultStand)
    }
    var selectedCategory by remember { mutableStateOf(VehicleCategory.BATTERY_AUTO) }
    var isReserveAuto by remember { mutableStateOf(false) } // Battery Auto Reserve System
    var phoneSearchQuery by remember { mutableStateOf("") }
    var isPhoneSearchFocused by remember { mutableStateOf(false) }

    var advanceMinutesOffset by remember { mutableIntStateOf(30) } // default 30 minutes in advance
    var customMinutesInput by remember { mutableStateOf("30") }
    var isMinutesFocused by remember { mutableStateOf(false) }
    var passengerCount by remember { mutableIntStateOf(2) }
    var specialNotes by remember { mutableStateOf("") }

    var isAreaFocused by remember { mutableStateOf(false) }
    var isPickupFocused by remember { mutableStateOf(false) }
    var isDropoffFocused by remember { mutableStateOf(false) }
    var isNotesFocused by remember { mutableStateOf(false) }

    val quickRajshahiStands = listOf(
        "জিরো পয়েন্ট স্ট্যান্ড",
        "লক্ষ্মীপুর মোড় (মেডিকেল)",
        "রাবি কাজলা গেট",
        "তালাইমারী মোড় (রুয়েট)",
        "শিরোইল রেল স্টেশন",
        "নিউ মার্কেট ও পদ্মা গার্ডেন",
        "উপশহর পানির ট্যাংক",
        "ভদ্রা মোড় স্ট্যান্ড"
    )

    val scheduledMillis = System.currentTimeMillis() + (advanceMinutesOffset.coerceAtLeast(30).toLong() * 60 * 1000L)
    val timeFormat = SimpleDateFormat("hh:mm a, dd MMM", Locale.getDefault())

    // High-contrast input color token styling for black text & placeholders
    val formFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedPlaceholderColor = Color.Black,
        unfocusedPlaceholderColor = Color.Black,
        focusedLabelColor = EmeraldDark,
        unfocusedLabelColor = Color.Black,
        focusedBorderColor = EmeraldPrimary,
        unfocusedBorderColor = Color.Black.copy(alpha = 0.5f),
        cursorColor = EmeraldPrimary
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLight)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Banner Card with Rajshahi City Rickshaw & Auto Image
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_rajshahi_banner),
                        contentDescription = "SafarGo Rajshahi City Transport",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        EmeraldDark.copy(alpha = 0.92f)
                                    )
                                )
                            )
                    )
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.95f),
                        shadowElevation = 3.dp,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .height(38.dp)
                            .width(85.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_safargo_logo),
                            contentDescription = "SafarGo Official Logo",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(3.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GoldAccent
                        ) {
                            Text(
                                text = if (isBengali) "রাজশাহী মহানগরী রিকশা ও অটোরিকশা সেবা" else "Rajshahi Metro Auto & Rickshaw",
                                color = Slate900,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isBengali) "৩০ মিনিট পূর্বে অটো বা রিকশা শিডিউল বুকিং" else "Advance Auto & Rickshaw Booking",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "SafarGo • রাজশাহী গ্রিন ও সিল্ক সিটি • Developed by MEJAN",
                            color = EmeraldContainer,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Operating Area Search Bar Card (Replaced dropdown with Area Search Bar)
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("area_search_card")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationCity, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBengali) "সার্ভিস এলাকা খুঁজুন (Search Area)" else "Search Service Area",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.Black
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EmeraldContainer
                        ) {
                            Text(
                                text = "রাজশাহী সিসিক",
                                color = EmeraldDark,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Area Search Input Field
                    OutlinedTextField(
                        value = areaSearchQuery,
                        onValueChange = { query ->
                            areaSearchQuery = query
                            if (query.isNotBlank()) {
                                onChangeZone(query)
                                val matchedStand = getDefaultStandForZone(query)
                                pickupText = matchedStand
                                dropoffText = getDefaultDestinationForStand(matchedStand)
                            }
                        },
                        placeholder = {
                            if (!isAreaFocused && areaSearchQuery.isEmpty()) {
                                Text(
                                    text = if (isBengali) "এলাকা / মোড় বা স্ট্যান্ড লিখে খুঁজুন..." else "Type area, mor, or stand name...",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Black)
                        },
                        trailingIcon = {
                            if (areaSearchQuery.isNotEmpty()) {
                                IconButton(onClick = { areaSearchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Black)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isAreaFocused = it.isFocused }
                            .testTag("area_search_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = formFieldColors,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Current Active Area Badge
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = EmeraldContainer.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(SuccessGreen)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "নির্বাচিত এলাকা: $currentOperatingZone",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = EmeraldDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick Area Chips for Rajshahi
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(availableZones) { zone ->
                            val cleanName = zone.replace("রাজশাহী মহানগর - ", "")
                            val isSelected = currentOperatingZone == zone
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    onChangeZone(zone)
                                    areaSearchQuery = cleanName
                                    val newStand = getDefaultStandForZone(zone)
                                    pickupText = newStand
                                    dropoffText = getDefaultDestinationForStand(newStand)
                                },
                                label = {
                                    Text(
                                        text = cleanName,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldDark,
                                    selectedLabelColor = Color.White,
                                    containerColor = Slate50,
                                    labelColor = Color.Black
                                )
                            )
                        }
                    }
                }
            }
        }

        // Phone Number Tracking for Rickshaw Pullers
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("phone_tracking_card")
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
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(EmeraldContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PhoneIphone, contentDescription = null, tint = EmeraldDark, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isBengali) "ফোন নম্বর দিয়ে রিকশা অবস্থান ট্র্যাকিং" else "Track Rickshaw by Phone",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = if (isBengali) "চালকের ফোন নম্বর লিখে তার লাইভ অবস্থান ও স্ট্যান্ড দেখুন" else "Enter driver phone to track location in Rajshahi",
                                    fontSize = 11.sp,
                                    color = Color.Black.copy(alpha = 0.75f)
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GoldAccent
                        ) {
                            Text(
                                text = "লাইভ জিপিএস",
                                color = Slate900,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = phoneSearchQuery,
                        onValueChange = { phoneSearchQuery = it },
                        label = { Text(if (isBengali) "রিকশাচালকের মোবাইল নম্বর" else "Driver Phone Number", color = Color.Black) },
                        placeholder = {
                            if (!isPhoneSearchFocused && phoneSearchQuery.isEmpty()) {
                                Text(if (isBengali) "যেমন: 01711-234567 বা 01610-998877" else "e.g. 01711234567", color = Color.Black)
                            }
                        },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = EmeraldDark) },
                        trailingIcon = {
                            if (phoneSearchQuery.isNotEmpty()) {
                                IconButton(onClick = { phoneSearchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = null, tint = Color.Black)
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isPhoneSearchFocused = it.isFocused }
                            .testTag("driver_phone_search_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = formFieldColors,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick registered phone numbers suggestions
                    Text(
                        text = if (isBengali) "ক্লিক করে দ্রুত চালক ট্র্যাক করুন:" else "Quick Select Driver:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "01711-234567" to "মো. রহিম (জিরো পয়েন্ট)",
                            "01610-998877" to "কাশেম আলী (রেল স্টেশন)",
                            "01822-334455" to "সুজন (তালাইমারী)",
                            "01912-778899" to "বাবুল মিয়া (মেডিকেল)",
                            "01733-445566" to "আলমগীর (কাজলা গেট)"
                        ).forEach { (phoneNum, label) ->
                            FilterChip(
                                selected = phoneSearchQuery == phoneNum,
                                onClick = {
                                    phoneSearchQuery = phoneNum
                                    onTrackDriverByPhone(phoneNum)
                                },
                                label = { Text(label, fontSize = 11.sp, color = Color.Black) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (phoneSearchQuery.trim().isNotEmpty()) {
                                onTrackDriverByPhone(phoneSearchQuery.trim())
                            }
                        },
                        enabled = phoneSearchQuery.trim().isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("track_phone_button")
                    ) {
                        Icon(Icons.Default.GpsFixed, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBengali) "অবস্থান খুঁজুন ও ট্র্যাক করুন" else "Track Rickshaw Position",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Rickshaw Puller Driver Registration Promo Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Slate50),
                border = BorderStroke(1.5.dp, EmeraldLight.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("driver_registration_promo_card")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(GoldAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        AutoRickshawIcon(contentDescription = null, tint = EmeraldDark, modifier = Modifier.size(26.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBengali) "রিকশাচালক নিবন্ধন ফর্ম" else "Register Rickshaw Driver",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = if (isBengali) "চালকের নাম, ফোন ও স্ট্যান্ডের তথ্য ডাটাবেজে যুক্ত করুন" else "Add driver info & phone to database",
                            fontSize = 11.sp,
                            color = Color.Black.copy(alpha = 0.75f)
                        )
                    }
                    Button(
                        onClick = onOpenDriverRegistration,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("open_driver_reg_button")
                    ) {
                        Text(
                            text = if (isBengali) "নিবন্ধন" else "Register",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Active Ride Banner (If ongoing in Rajshahi)
        if (activeRide != null && (activeRide.status == RideStatus.ACCEPTED || activeRide.status == RideStatus.DRIVER_ARRIVED || activeRide.status == RideStatus.IN_TRANSIT)) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("active_ride_card")
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
                                        .background(SuccessGreen)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = when (activeRide.status) {
                                        RideStatus.ACCEPTED -> if (isBengali) "চালক পিকআপে আসছেন" else "Driver on the way"
                                        RideStatus.DRIVER_ARRIVED -> if (isBengali) "অটো/রিকশা স্ট্যান্ডে উপস্থিত" else "Auto at Stand"
                                        RideStatus.IN_TRANSIT -> if (isBengali) "চলমান রাইড (রাজশাহী)" else "En Route"
                                        else -> "Active Ride"
                                    },
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Text(
                                text = "#${activeRide.bookingReference}",
                                color = GoldAccent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "${activeRide.pickupLocation} ➔ ${activeRide.dropoffLocation}",
                            color = EmeraldContainer,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                        Text(
                            text = "চালক: ${activeRide.driverName} • ${activeRide.driverTokenNumber}",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onTrackRide(activeRide.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("track_active_ride_button")
                            ) {
                                Icon(Icons.Default.LocationSearching, contentDescription = null, tint = Slate900, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isBengali) "লাইভ অবস্থান" else "Live Radar",
                                    color = Slate900,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            Button(
                                onClick = { onOpenChat(activeRide) },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldLight),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isBengali) "চালকের সাথে চ্যাট" else "Chat",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            IconButton(
                                onClick = { onOpenSlip(activeRide) },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White.copy(alpha = 0.15f))
                            ) {
                                Icon(Icons.Default.Receipt, contentDescription = "Slip", tint = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // 1-Hour Advance Rajshahi Booking Form
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("advance_booking_form_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    val estPickup = pickupText.trim().ifEmpty { getDefaultStandForZone(currentOperatingZone) }
                    val estDropoff = dropoffText.trim().ifEmpty { getDefaultDestinationForStand(estPickup) }
                    val isReserveEffective = isReserveAuto && selectedCategory == VehicleCategory.BATTERY_AUTO
                    val (estDistance, estFare) = remember(estPickup, estDropoff, isReserveEffective) {
                        com.example.data.repository.TransportRepository.getInstance().getEstimatedDistanceAndFare(estPickup, estDropoff, isReserveEffective)
                    }
                    val estMinutes = remember(estDistance) {
                        (estDistance * 4.0).toInt().coerceAtLeast(6)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(EmeraldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            AutoRickshawIcon(
                                contentDescription = null,
                                tint = EmeraldDark,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isBengali) "রাজশাহী অটো / রিকশা বুকিং" else "Rajshahi Auto / Rickshaw Booking",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.Black
                            )
                            Text(
                                text = if (isBengali) "৩০ মিনিট পূর্বে বুক করে গাড়ি নিশ্চিন্ত রাখুন" else "Book 30 mins ahead for guaranteed ride",
                                fontSize = 11.sp,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Vehicle Type Selector (Rajshahi Metro Fleet)
                    Text(
                        text = if (isBengali) "যানবাহনের ধরন নির্বাচন (রাজশাহী সিসিক)" else "Select Fleet Category (RCC)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(VehicleCategory.values()) { cat ->
                            val isSelected = selectedCategory == cat
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) EmeraldDark else Slate50,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) GoldAccent else Color.Black.copy(alpha = 0.2f)
                                ),
                                modifier = Modifier
                                    .clickable { selectedCategory = cat }
                                    .testTag("vehicle_cat_${cat.name}")
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                                    Text(
                                        text = if (isBengali) cat.labelBn else cat.labelEn,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (isSelected) Color.White else Color.Black
                                    )
                                    Text(
                                        text = "${cat.capacity} জন • ৳${cat.perKmRate.toInt()}/কিমি",
                                        fontSize = 10.sp,
                                        color = if (isSelected) GoldAccent else Color.Black.copy(alpha = 0.75f)
                                    )
                                }
                            }
                        }
                    }

                    // Reserve System Option for Battery Auto
                    if (selectedCategory == VehicleCategory.BATTERY_AUTO) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isReserveAuto) GoldAccent.copy(alpha = 0.15f) else Slate50,
                            border = BorderStroke(1.5.dp, if (isReserveAuto) GoldAccent else EmeraldPrimary.copy(alpha = 0.35f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("battery_auto_reserve_box")
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (isReserveAuto) Icons.Default.Stars else Icons.Default.Groups,
                                            contentDescription = null,
                                            tint = if (isReserveAuto) OnGoldContainer else EmeraldDark,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = if (isBengali) "ব্যাটারি অটো ট্রিপ মোড (রিজার্ভ সিস্টেম)" else "Battery Auto Trip Mode",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Color.Black
                                            )
                                            Text(
                                                text = if (isReserveAuto)
                                                    (if (isBengali) "🌟 পুরো অটো রিজার্ভ সক্রিয় (সম্পূর্ণ ৫ সিট বুকড)" else "Full Auto Reserve Active")
                                                else
                                                    (if (isBengali) "সাধারণ শেয়ার ট্রিপ (১৫৳/কিমি)" else "Shared Trip (৳15/km)"),
                                                fontSize = 11.sp,
                                                color = if (isReserveAuto) OnGoldContainer else EmeraldDark,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (!isReserveAuto) EmeraldDark else Color.White,
                                        border = BorderStroke(1.dp, if (!isReserveAuto) EmeraldDark else Color.Black.copy(alpha = 0.2f)),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                isReserveAuto = false
                                                passengerCount = 2
                                            }
                                            .testTag("shared_trip_chip")
                                    ) {
                                        Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = if (isBengali) "🛺 শেয়ার ট্রিপ" else "Shared Trip",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = if (!isReserveAuto) Color.White else Color.Black
                                            )
                                            Text(
                                                text = "১৫৳/কিমি • সাধারণ",
                                                fontSize = 10.sp,
                                                color = if (!isReserveAuto) GoldAccent else Color.Black.copy(alpha = 0.7f)
                                            )
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isReserveAuto) GoldAccent else Color.White,
                                        border = BorderStroke(1.5.dp, if (isReserveAuto) GoldAccent else Color.Black.copy(alpha = 0.2f)),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                isReserveAuto = true
                                                passengerCount = 5
                                            }
                                            .testTag("reserve_auto_chip")
                                    ) {
                                        Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = if (isBengali) "🌟 পুরো অটো রিজার্ভ" else "🌟 Auto Reserve",
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 12.sp,
                                                color = Slate900
                                            )
                                            Text(
                                                text = "২৫৳/কিমি • পুরো ৫ সিট",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Slate900.copy(alpha = 0.85f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Pickup Stand in Rajshahi with Black Placeholder & Label
                    OutlinedTextField(
                        value = pickupText,
                        onValueChange = { pickupText = it },
                        label = {
                            Text(
                                text = if (isBengali) "পিকআপ মোড় / স্ট্যান্ড (যেমন: জিরো পয়েন্ট)" else "Pickup Stand in Rajshahi",
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        placeholder = {
                            if (!isPickupFocused && pickupText.isEmpty()) {
                                Text(
                                    text = if (isBengali) "পিকআপ মোড় বা স্ট্যান্ড লিখুন..." else "Enter pickup point...",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        },
                        leadingIcon = { Icon(Icons.Default.TripOrigin, contentDescription = null, tint = SuccessGreen) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isPickupFocused = it.isFocused }
                            .testTag("pickup_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = formFieldColors
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Quick Stand Chips in Rajshahi
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(quickRajshahiStands) { stand ->
                            FilterChip(
                                selected = pickupText == stand,
                                onClick = {
                                    pickupText = stand
                                    dropoffText = getDefaultDestinationForStand(stand)
                                },
                                label = { Text(stand, fontSize = 11.sp, color = Color.Black) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dropoff Location in Rajshahi with Black Placeholder & Label
                    OutlinedTextField(
                        value = dropoffText,
                        onValueChange = { dropoffText = it },
                        label = {
                            Text(
                                text = if (isBengali) "গন্তব্য / মোড় (যেমন: কাজলা গেট, মেডিকেল)" else "Dropoff Destination",
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        placeholder = {
                            if (!isDropoffFocused && dropoffText.isEmpty()) {
                                Text(
                                    text = if (isBengali) "গন্তব্যের নাম বা ঠিকানা লিখুন..." else "Enter dropoff destination...",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = ErrorRed) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isDropoffFocused = it.isFocused }
                            .testTag("dropoff_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = formFieldColors
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Quick Destination Chips in Rajshahi
                    val quickDestinations = listOf(
                        "রাবি কাজলা গেট",
                        "লক্ষ্মীপুর মেডিকেল",
                        "জিরো পয়েন্ট স্ট্যান্ড",
                        "শিরোইল রেল স্টেশন",
                        "রুয়েট মোড়",
                        "কোর্ট চত্বর",
                        "উপশহর পানির ট্যাংক",
                        "পদ্মা গার্ডেন"
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(quickDestinations) { dest ->
                            FilterChip(
                                selected = dropoffText == dest,
                                onClick = { dropoffText = dest },
                                label = { Text(dest, fontSize = 11.sp, color = Color.Black) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Real-time Route Distance & Automatic Fare Tracker Card
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = EmeraldContainer.copy(alpha = 0.5f),
                        border = BorderStroke(1.5.dp, EmeraldPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("route_distance_fare_tracker_card")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.GpsFixed,
                                        contentDescription = null,
                                        tint = EmeraldDark,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isBengali) "স্বয়ংক্রিয় রুট ও দূরত্ব ট্র্যাকার" else "Automatic Route & Distance Tracker",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = EmeraldDark
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = EmeraldPrimary
                                ) {
                                    Text(
                                        text = if (isBengali) "লাইভ মিটার" else "Live Meter",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Origin to Destination Route Nodes
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.TripOrigin, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(28.dp)
                                            .background(EmeraldDark.copy(alpha = 0.4f))
                                    )
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(14.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "যাত্রার স্থান: $estPickup",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "গন্তব্য স্থান: $estDropoff",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = EmeraldPrimary.copy(alpha = 0.3f), thickness = 1.dp)
                            Spacer(modifier = Modifier.height(10.dp))

                            // Live Auto-Calculated Distance and Fare Counters
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Straighten, contentDescription = null, tint = EmeraldDark, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "স্বয়ংক্রিয় দূরত্ব: ${String.format("%.1f", estDistance)} কিমি",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = Color.Black
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (isReserveEffective)
                                            "আনুমানিক সময়: $estMinutes মিনিট • রেট: ২৫৳/কিমি (রিজার্ভ অটো)"
                                        else
                                            "আনুমানিক সময়: $estMinutes মিনিট • রেট: ১৫৳/কিমি",
                                        fontSize = 11.sp,
                                        color = Color.Black.copy(alpha = 0.75f)
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "৳${estFare.toInt()}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 22.sp,
                                        color = EmeraldDark
                                    )
                                    Text(
                                        text = if (isBengali) "হিসাবকৃত চূড়ান্ত ভাড়া" else "Calculated Fare",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldDark.copy(alpha = 0.85f)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Schedule Time (30 Minutes Ahead Validation - Type & Select)
                    Text(
                        text = if (isBengali) "কখন গাড়ি প্রয়োজন? (৩০ মিনিট পূর্বে অগ্রিম বুকিং)" else "When do you need the ride? (30 mins advance)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                    Text(
                        text = if (isBengali) "ডিফল্ট ৩০ মিনিট পর নির্ধারিত। আপনি সময় লিখে বা নিচের বাটনে ক্লিক করে পছন্দমতো নির্বাচন করতে পারেন:" else "Default is set to 30 mins ahead. You can type minutes or select from below:",
                        fontSize = 11.sp,
                        color = Color.Black.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // 1. User can type / write the minutes ("lika")
                    OutlinedTextField(
                        value = customMinutesInput,
                        onValueChange = { newVal ->
                            val digitsOnly = newVal.filter { it.isDigit() }
                            customMinutesInput = digitsOnly
                            val parsed = digitsOnly.toIntOrNull()
                            if (parsed != null && parsed > 0) {
                                advanceMinutesOffset = parsed
                            }
                        },
                        label = {
                            Text(
                                text = if (isBengali) "কত মিনিট পর গাড়ি লাগবে? (লিখুন, উদা: 30, 45, 60)" else "Minutes in advance (type, e.g. 30, 45, 60)",
                                color = Color.Black,
                                fontSize = 12.sp
                            )
                        },
                        placeholder = {
                            if (!isMinutesFocused) {
                                Text(
                                    text = if (isBengali) "৩০ মিনিট বা তদূর্ধ্ব" else "30 mins or more",
                                    color = Color.Black
                                )
                            }
                        },
                        isError = customMinutesInput.isNotEmpty() && (customMinutesInput.toIntOrNull() ?: 0) < 30,
                        supportingText = {
                            val mins = customMinutesInput.toIntOrNull() ?: 0
                            if (customMinutesInput.isNotEmpty() && mins < 30) {
                                Text(
                                    text = if (isBengali) "⚠️ অগ্রিম বুকিংয়ের নিয়ম: কমপক্ষে ৩০ মিনিট পূর্বে শিডিউল করতে হবে (৩০ মিনিট কার্যকর হবে)" else "⚠️ Advance rule: Minimum 30 mins ahead required",
                                    color = WarningAmber,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = if (isBengali) "✓ সময় লিখে বা নিচের বাটন ক্লিক করে পরিবর্তন করতে পারেন" else "✓ Type custom minutes or choose quick button below",
                                    color = Color.Black.copy(alpha = 0.7f),
                                    fontSize = 11.sp
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        leadingIcon = {
                            Icon(Icons.Default.Timer, contentDescription = null, tint = EmeraldDark)
                        },
                        trailingIcon = {
                            if (customMinutesInput.isNotEmpty()) {
                                IconButton(onClick = {
                                    customMinutesInput = ""
                                }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Black)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isMinutesFocused = it.isFocused }
                            .testTag("advance_minutes_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = formFieldColors,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // 2. User can select from chips ("select korta parba")
                    Text(
                        text = if (isBengali) "দ্রুত সময় নির্বাচন করুন:" else "Quick Select Time:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            30 to (if (isBengali) "৩০ মিনিট পর (ডিফল্ট)" else "In 30 Mins (Default)"),
                            45 to (if (isBengali) "৪৫ মিনিট পর" else "In 45 Mins"),
                            60 to (if (isBengali) "১ ঘণ্টা পর" else "In 1 Hour"),
                            90 to (if (isBengali) "১.৫ ঘণ্টা পর" else "In 1.5 Hours"),
                            120 to (if (isBengali) "২ ঘণ্টা পর" else "In 2 Hours"),
                            180 to (if (isBengali) "৩ ঘণ্টা পর" else "In 3 Hours"),
                            360 to (if (isBengali) "৬ ঘণ্টা পর" else "In 6 Hours")
                        ).forEach { (mins, label) ->
                            val isSel = advanceMinutesOffset == mins
                            FilterChip(
                                selected = isSel,
                                onClick = {
                                    advanceMinutesOffset = mins
                                    customMinutesInput = mins.toString()
                                },
                                label = {
                                    Text(
                                        label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSel) EmeraldDark else Color.Black
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldContainer,
                                    selectedLabelColor = OnEmeraldContainer
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 3. Current Selected Scheduled Time Card
                    val effectiveMins = advanceMinutesOffset.coerceAtLeast(30)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = EmeraldContainer.copy(alpha = 0.4f),
                        border = BorderStroke(1.dp, EmeraldPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AccessTime, contentDescription = null, tint = EmeraldDark, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "যাত্রার নির্ধারিত সময়: ${timeFormat.format(Date(scheduledMillis))}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = if (isBengali)
                                        "✓ বর্তমান সময় হতে $effectiveMins মিনিট পর গাড়ি প্রস্তুত থাকবে (৩০ মিনিট অগ্রিম বুকিং নিশ্চিত)"
                                    else
                                        "✓ Ride ready in $effectiveMins mins from now (30 min advance active)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = EmeraldDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Passenger Count
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBengali) "যাত্রী সংখ্যা" else "Passengers",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { if (passengerCount > 1) passengerCount-- },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Slate100)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Black)
                            }
                            Text(
                                text = "$passengerCount জন",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            IconButton(
                                onClick = { if (passengerCount < selectedCategory.capacity) passengerCount++ },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Slate100)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Black)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Special Notes with Black Placeholder
                    OutlinedTextField(
                        value = specialNotes,
                        onValueChange = { specialNotes = it },
                        label = {
                            Text(
                                text = if (isBengali) "বিশেষ নির্দেশনা" else "Special Notes",
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        placeholder = {
                            if (!isNotesFocused && specialNotes.isEmpty()) {
                                Text(
                                    text = if (isBengali) "বিশেষ নির্দেশনা (যেমন: ট্রেনের টিকিট কাটা আছে, মেডিকেলের রোগী)" else "Notes (e.g. train departure, hospital patient)",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isNotesFocused = it.isFocused },
                        shape = RoundedCornerShape(12.dp),
                        colors = formFieldColors
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Final Fare Summary (15 Taka per kilometer rate)

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EmeraldContainer,
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldLight.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("fare_estimate_box")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.MonetizationOn,
                                        contentDescription = null,
                                        tint = EmeraldDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isBengali) "ভাড়া হিসাব (প্রতি কিমি ১৫ টাকা)" else "Fare Rate (৳15 / km)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = EmeraldDark
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (isBengali) "দূরত্ব: ${String.format("%.1f", estDistance)} কিমি • রেট: ৳১৫/কিমি" else "Distance: ${String.format("%.1f", estDistance)} km • Rate: ৳15/km",
                                    fontSize = 11.sp,
                                    color = OnEmeraldContainer
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "৳${estFare.toInt()}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp,
                                    color = EmeraldDark
                                )
                                Text(
                                    text = if (isBengali) "আনুমানিক ভাড়া" else "Est. Total",
                                    fontSize = 10.sp,
                                    color = EmeraldDark.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            val finalPickup = pickupText.trim().ifEmpty { getDefaultStandForZone(currentOperatingZone) }
                            val finalDropoff = dropoffText.trim().ifEmpty { getDefaultDestinationForStand(finalPickup) }
                            onBookAdvanceRide(
                                finalPickup,
                                finalDropoff,
                                selectedCategory,
                                scheduledMillis,
                                if (isReserveEffective) 5 else passengerCount,
                                specialNotes,
                                isReserveEffective
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("submit_advance_booking_button")
                    ) {
                        AutoRickshawIcon(contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBengali) "বুকিং নিশ্চিত করুন (কনফার্মেশন স্লিপ পান)" else "Confirm Ride Booking",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Recent Bookings in Rajshahi
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBengali) "আমার রাজশাহী রাইড হিস্ট্রি" else "My Rajshahi Ride History",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = "${bookings.size} টি রেকর্ড",
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            }
        }

        items(bookings) { booking ->
            BookingRecordCard(
                booking = booking,
                isBengali = isBengali,
                onViewSlip = { onOpenSlip(booking) },
                onPayFare = { onPayFare(booking) },
                onTrackRide = { onTrackRide(booking.id) }
            )
        }

        // Footer
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "SafarGo • রাজশাহী মহানগর অটোরিকশা ও রিকশা সেবা",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = EmeraldDark
                )
                Text(
                    text = "Developed by MEJAN",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GoldAccent
                )
            }
        }
    }
}

@Composable
fun BookingRecordCard(
    booking: RideBooking,
    isBengali: Boolean,
    onViewSlip: () -> Unit,
    onPayFare: () -> Unit,
    onTrackRide: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onViewSlip() }
            .testTag("booking_record_${booking.bookingReference}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = EmeraldContainer
                    ) {
                        Text(
                            text = booking.driverTokenNumber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnEmeraldContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = booking.vehicleCategory.typeName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when (booking.status) {
                        RideStatus.PENDING -> WarningAmber.copy(alpha = 0.15f)
                        RideStatus.ACCEPTED -> InfoBlue.copy(alpha = 0.15f)
                        RideStatus.DRIVER_ARRIVED -> WarningAmber.copy(alpha = 0.2f)
                        RideStatus.IN_TRANSIT -> SuccessGreen.copy(alpha = 0.15f)
                        RideStatus.COMPLETED -> EmeraldContainer
                        RideStatus.CANCELLED -> ErrorRed.copy(alpha = 0.15f)
                    }
                ) {
                    Text(
                        text = when (booking.status) {
                            RideStatus.PENDING -> if (isBengali) "অপেক্ষমাণ" else "Pending"
                            RideStatus.ACCEPTED -> if (isBengali) "গ্রহণকৃত" else "Accepted"
                            RideStatus.DRIVER_ARRIVED -> if (isBengali) "গাড়ি উপস্থিত" else "Arrived"
                            RideStatus.IN_TRANSIT -> if (isBengali) "চলমান" else "In Transit"
                            RideStatus.COMPLETED -> if (isBengali) "সম্পন্ন" else "Completed"
                            RideStatus.CANCELLED -> if (isBengali) "বাতিল" else "Cancelled"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (booking.status) {
                            RideStatus.PENDING -> OnGoldContainer
                            RideStatus.ACCEPTED -> InfoBlue
                            RideStatus.DRIVER_ARRIVED -> OnGoldContainer
                            RideStatus.IN_TRANSIT -> SuccessGreen
                            RideStatus.COMPLETED -> OnEmeraldContainer
                            RideStatus.CANCELLED -> ErrorRed
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${booking.pickupLocation} ➔ ${booking.dropoffLocation}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ভাড়া: ৳${booking.fare.toInt()} • ${if (booking.paymentStatus == PaymentStatus.UNPAID) "বাকি" else "পরিশোধিত"}",
                    fontSize = 12.sp,
                    color = if (booking.paymentStatus == PaymentStatus.UNPAID) WarningAmber else SuccessGreen,
                    fontWeight = FontWeight.Bold
                )

                Row {
                    if (booking.paymentStatus == PaymentStatus.UNPAID) {
                        TextButton(onClick = onPayFare) {
                            Text("পে করুন", color = GoldAccent, fontWeight = FontWeight.Bold)
                        }
                    }
                    TextButton(onClick = onViewSlip) {
                        Text("টিকিট/স্লিপ", color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
