package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.RickshawDriverRegistration
import com.example.ui.theme.*
import java.util.UUID

@Composable
fun RickshawDriverRegistrationDialog(
    isBengali: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (RickshawDriverRegistration) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var nidNumber by remember { mutableStateOf("") }
    var rccTokenNumber by remember { mutableStateOf("") }
    var selectedVehicleType by remember { mutableStateOf("সিসিক ব্যাটারি অটো") }
    var assignedStand by remember { mutableStateOf("সাহেব বাজার জিরো পয়েন্ট") }
    var emergencyPhone by remember { mutableStateOf("") }
    var garageArea by remember { mutableStateOf("") }
    var experienceYears by remember { mutableIntStateOf(5) }
    var driverNotes by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val vehicleOptions = listOf(
        "সিসিক ব্যাটারি অটো",
        "চার্জার ব্যাটারি রিকশা",
        "ঐতিহ্যবাহী প্যাডেল রিকশা"
    )

    val standOptions = listOf(
        "সাহেব বাজার জিরো পয়েন্ট",
        "রাবি কাজলা গেট",
        "তালাইমারী মোড় (রুয়েট)",
        "শিরোইল রেল স্টেশন",
        "লক্ষ্মীপুর মোড় (মেডিকেল)",
        "ভদ্রা মোড় স্ট্যান্ড",
        "উপশহর পানির ট্যাংক",
        "নিউ মার্কেট স্ট্যান্ড",
        "কোর্ট চত্বর",
        "পদ্মা গার্ডেন ও টি-বাঁধ"
    )

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

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("driver_registration_dialog_card"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(EmeraldDark)
                        .padding(horizontal = 18.dp, vertical = 16.dp)
                ) {
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
                                    .background(GoldAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                AutoRickshawIcon(contentDescription = null, tint = EmeraldDark, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isBengali) "রিকশাচালক নিবন্ধন ফর্ম" else "Rickshaw Driver Registration",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = if (isBengali) "সফরগো রাজশাহী মহানগর ডাটাবেজ" else "SafarGo Rajshahi Metro Fleet",
                                    color = EmeraldContainer,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                }

                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (isBengali)
                            "রাজশাহী মহানগরে চলাচলরত রিকশা বা অটোরিকশা চালকের তথ্য নির্ভুলভাবে প্রদান করুন। এই তথ্যের মাধ্যমে চালকের ট্র্যাকিং ও যাত্রী সেবা পরিচালিত হবে।"
                        else
                            "Register rickshaw and auto drivers operating in Rajshahi Metro for live telemetry tracking.",
                        fontSize = 12.sp,
                        color = Color.Black.copy(alpha = 0.85f),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (errorMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ErrorRed.copy(alpha = 0.1f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = errorMessage!!,
                                color = ErrorRed,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Driver Full Name
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text(if (isBengali) "চালকের পূর্ণ নাম *" else "Driver Full Name *", color = Color.Black) },
                        placeholder = { Text(if (isBengali) "যেমন: মো. রহিম মিয়া" else "e.g. Md. Rahim Mia", color = Color.Black) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = EmeraldPrimary) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_driver_name"),
                        shape = RoundedCornerShape(12.dp),
                        colors = formFieldColors,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Phone Number
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text(if (isBengali) "চালকের মোবাইল নম্বর (ট্র্যাকিং নম্বর) *" else "Phone Number (Tracking Key) *", color = Color.Black) },
                        placeholder = { Text(if (isBengali) "যেমন: 01711-234567" else "e.g. 01711234567", color = Color.Black) },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = EmeraldPrimary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_driver_phone"),
                        shape = RoundedCornerShape(12.dp),
                        colors = formFieldColors,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // NID Number
                    OutlinedTextField(
                        value = nidNumber,
                        onValueChange = { nidNumber = it },
                        label = { Text(if (isBengali) "জাতীয় পরিচয়পত্র (NID) নম্বর" else "NID Number", color = Color.Black) },
                        placeholder = { Text(if (isBengali) "১০ বা ১৭ ডিজিটের এনআইডি" else "10/17 digit NID", color = Color.Black) },
                        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = EmeraldDark) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_driver_nid"),
                        shape = RoundedCornerShape(12.dp),
                        colors = formFieldColors,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // RCC Token / License Number
                    OutlinedTextField(
                        value = rccTokenNumber,
                        onValueChange = { rccTokenNumber = it },
                        label = { Text(if (isBengali) "সিসিক টোকেন / বডি প্লেট নম্বর" else "RCC Token / Plate No.", color = Color.Black) },
                        placeholder = { Text(if (isBengali) "যেমন: সিসিক টোকেন: লাল-০৮" else "e.g. RCC Token: Red-08", color = Color.Black) },
                        leadingIcon = { Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = GoldAccent) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_driver_token"),
                        shape = RoundedCornerShape(12.dp),
                        colors = formFieldColors,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Vehicle Type Selector
                    Text(
                        text = if (isBengali) "যানবাহনের ধরন:" else "Vehicle Type:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(vehicleOptions) { vType ->
                            val isSel = selectedVehicleType == vType
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedVehicleType = vType },
                                label = { Text(vType, fontSize = 11.sp, color = if (isSel) Color.White else Color.Black) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldDark,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Assigned Stand / Area Selector
                    Text(
                        text = if (isBengali) "নিয়মিত স্ট্যান্ড / মোড়:" else "Assigned Stand in Rajshahi:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(standOptions) { stand ->
                            val isSel = assignedStand == stand
                            FilterChip(
                                selected = isSel,
                                onClick = { assignedStand = stand },
                                label = { Text(stand, fontSize = 11.sp, color = if (isSel) Color.White else Color.Black) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldDark,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Garage Area & Emergency Phone in Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = garageArea,
                            onValueChange = { garageArea = it },
                            label = { Text(if (isBengali) "গ্যারেজের এলাকা" else "Garage Area", color = Color.Black, fontSize = 11.sp) },
                            placeholder = { Text("আলুপট্টি", color = Color.Black) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = formFieldColors,
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = emergencyPhone,
                            onValueChange = { emergencyPhone = it },
                            label = { Text(if (isBengali) "জরুরি নম্বর" else "Emergency", color = Color.Black, fontSize = 11.sp) },
                            placeholder = { Text("017...", color = Color.Black) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = formFieldColors,
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Experience in Years
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBengali) "অভিজ্ঞতা: $experienceYears বছর" else "Experience: $experienceYears yrs",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { if (experienceYears > 1) experienceYears-- },
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(Slate100)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("$experienceYears", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = { if (experienceYears < 30) experienceYears++ },
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(Slate100)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Notes
                    OutlinedTextField(
                        value = driverNotes,
                        onValueChange = { driverNotes = it },
                        label = { Text(if (isBengali) "বিশেষ নির্দেশনা / মন্তব্য" else "Notes / Remarks", color = Color.Black) },
                        placeholder = { Text(if (isBengali) "চালকের শিফট, পরিচিতি ইত্যাদি" else "Shift timing, remarks", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = formFieldColors
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (fullName.trim().isEmpty()) {
                                errorMessage = if (isBengali) "দয়া করে চালকের পূর্ণ নাম লিখুন।" else "Please enter driver full name."
                                return@Button
                            }
                            if (phone.trim().length < 8) {
                                errorMessage = if (isBengali) "দয়া করে সঠিক মোবাইল নম্বর লিখুন।" else "Please enter valid mobile number."
                                return@Button
                            }
                            val reg = RickshawDriverRegistration(
                                id = "reg_" + UUID.randomUUID().toString().take(8),
                                fullName = fullName.trim(),
                                phone = phone.trim(),
                                nidNumber = nidNumber.trim().ifEmpty { "NID-অপেক্ষমাণ" },
                                rccTokenNumber = rccTokenNumber.trim().ifEmpty { "সিসিক টোকেন: ${(10..99).random()}" },
                                vehicleType = selectedVehicleType,
                                assignedStand = assignedStand,
                                emergencyContactPhone = emergencyPhone.trim(),
                                garageArea = garageArea.trim().ifEmpty { "রাজশাহী লোকাল" },
                                experienceYears = experienceYears,
                                notes = driverNotes.trim()
                            )
                            onSubmit(reg)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("submit_driver_registration_button")
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBengali) "নিবন্ধন সম্পন্ন করুন ও ডাটা জমা দিন" else "Complete Registration & Submit",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
