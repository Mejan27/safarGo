package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.PaymentGateway
import com.example.data.model.RideBooking
import com.example.ui.theme.*

@Composable
fun PaymentDialog(
    booking: RideBooking,
    isBengali: Boolean,
    onDismiss: () -> Unit,
    onConfirmPayment: (bookingId: String, gateway: PaymentGateway, accountPhone: String) -> Unit
) {
    var selectedGateway by remember { mutableStateOf(PaymentGateway.BKASH) }
    var accountPhone by remember { mutableStateOf(booking.passengerPhone) }
    var pin by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("payment_dialog_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
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
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isBengali) "ডিজিটাল ভাড়া পরিশোধ" else "Digital Fare Payment",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "SafarGo Transport Service",
                                color = EmeraldContainer,
                                fontSize = 11.sp
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    // Fare Summary Card
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = GoldContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "বুকিং আইডি: ${booking.bookingReference}",
                                    fontSize = 12.sp,
                                    color = OnGoldContainer,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${booking.pickupLocation} ➔ ${booking.dropoffLocation}",
                                    fontSize = 11.sp,
                                    color = Slate700,
                                    maxLines = 1
                                )
                                Text(
                                    text = "দূরত্ব: ${String.format("%.1f", booking.distanceKm)} কিমি (রেট: ৳১৫/কিমি)",
                                    fontSize = 10.sp,
                                    color = Slate700
                                )
                            }
                            Text(
                                text = "৳${booking.fare.toInt()}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnGoldContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isBengali) "পেমেন্ট মাধ্যম বেছে নিন" else "Select Payment Gateway",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Slate900
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Gateways
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        GatewayItem(
                            title = "bKash (বিকাশ ওয়ালেট)",
                            subtitle = "ইনস্ট্যান্ট পেমেন্ট • ১.৫% গেটওয়ে ফি প্রযোজ্য",
                            color = Color(0xFFE2136E),
                            isSelected = selectedGateway == PaymentGateway.BKASH,
                            onClick = { selectedGateway = PaymentGateway.BKASH }
                        )
                        GatewayItem(
                            title = "Nagad (নগদ ওয়ালেট)",
                            subtitle = "ডিজিটাল ডাক সেবা • ১.২% ফি",
                            color = Color(0xFFF7941D),
                            isSelected = selectedGateway == PaymentGateway.NAGAD,
                            onClick = { selectedGateway = PaymentGateway.NAGAD }
                        )
                        GatewayItem(
                            title = "Visa / Mastercard / AMEX",
                            subtitle = "যেকোনো বাংলাদেশি বা আন্তর্জাতিক ডেবিট/ক্রেডিট কার্ড",
                            color = Color(0xFF1A1F71),
                            isSelected = selectedGateway == PaymentGateway.CARD,
                            onClick = { selectedGateway = PaymentGateway.CARD }
                        )
                        GatewayItem(
                            title = "Cash on Trip (চালককে নগদ পরিশোধ)",
                            subtitle = "গন্তব্যে পৌঁছে চালককে ক্যাশ দিন",
                            color = EmeraldDark,
                            isSelected = selectedGateway == PaymentGateway.CASH,
                            onClick = { selectedGateway = PaymentGateway.CASH }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (selectedGateway != PaymentGateway.CASH) {
                        val paymentFieldColors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedPlaceholderColor = Color.Black,
                            unfocusedPlaceholderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Black,
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = Color.Black.copy(alpha = 0.5f),
                            cursorColor = EmeraldPrimary
                        )

                        OutlinedTextField(
                            value = accountPhone,
                            onValueChange = { accountPhone = it },
                            label = { Text(if (isBengali) "অ্যাকাউন্ট / মোবাইল নম্বর" else "Account / Wallet Phone", color = Color.Black, fontWeight = FontWeight.Medium) },
                            placeholder = { Text(if (isBengali) "০১৭xxxxxxxx (বিকাশ/নগদ নম্বর)" else "017xxxxxxxx (Wallet number)", color = Color.Black) },
                            leadingIcon = { Icon(Icons.Default.PhoneIphone, contentDescription = null, tint = EmeraldDark) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("payment_account_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = paymentFieldColors
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = pin,
                            onValueChange = { pin = it },
                            label = { Text(if (isBengali) "পিন / ওটিপি (ডেমো: 1234)" else "PIN / OTP (Demo: 1234)", color = Color.Black, fontWeight = FontWeight.Medium) },
                            placeholder = { Text("•••• (1234)", color = Color.Black) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldDark) },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("payment_pin_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = paymentFieldColors
                        )

                        Spacer(modifier = Modifier.height(18.dp))
                    }

                    Button(
                        onClick = {
                            isProcessing = true
                            onConfirmPayment(booking.id, selectedGateway, accountPhone)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isProcessing,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("confirm_payment_submit_button")
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("প্রসেসিং হচ্ছে...")
                        } else {
                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (selectedGateway == PaymentGateway.CASH)
                                    "ক্যাশ অন ট্রিপ কনফার্ম করুন"
                                else
                                    "৳${booking.fare.toInt()} পরিশোধ সম্পন্ন করুন",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "নিরাপদ লেনদেন নিশ্চিতে SSL 256-bit এনক্রিপশন ব্যবহৃত হচ্ছে\nDeveloped by MEJAN",
                        fontSize = 10.sp,
                        color = Slate700,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun GatewayItem(
    title: String,
    subtitle: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) color.copy(alpha = 0.08f) else Slate50,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) color else Slate100
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = color)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Slate900
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Slate700
                )
            }
        }
    }
}
