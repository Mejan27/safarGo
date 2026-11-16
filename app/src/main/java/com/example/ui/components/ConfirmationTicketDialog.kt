package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Image
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.PaymentStatus
import com.example.data.model.RideBooking
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ConfirmationTicketDialog(
    booking: RideBooking,
    isBengali: Boolean,
    onDismiss: () -> Unit,
    onPayClicked: (RideBooking) -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val scheduledDateStr = dateFormat.format(Date(booking.scheduledTimeMillis))
    val bookedDateStr = dateFormat.format(Date(booking.bookedAtMillis))

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("confirmation_ticket_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Ticket Top Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(EmeraldDark)
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White,
                            shadowElevation = 4.dp,
                            modifier = Modifier
                                .height(46.dp)
                                .width(128.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_safargo_logo),
                                contentDescription = "SafarGo Official Logo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isBengali) "সফরগো • রাজশাহী মহানগর পরিবহন" else "SafarGo Rajshahi Metro Transport",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isBengali) "রাজশাহী সিসিক অটো ও রিকশা ট্রাভেল কনফার্মেশন স্লিপ" else "Rajshahi Metro Travel Confirmation Slip",
                            color = EmeraldContainer,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "REF #${booking.bookingReference}",
                                color = GoldAccent,
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Ticket Body
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Status Badge & Advance Flag
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when (booking.paymentStatus) {
                                PaymentStatus.UNPAID -> WarningAmber.copy(alpha = 0.15f)
                                else -> SuccessGreen.copy(alpha = 0.15f)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (booking.paymentStatus == PaymentStatus.UNPAID) WarningAmber else SuccessGreen
                                        )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (booking.paymentStatus == PaymentStatus.UNPAID) 
                                        (if (isBengali) "ভাড়া অপরিশোধিত" else "Unpaid Fare") 
                                    else 
                                        (if (isBengali) "পরিশোধিত (${booking.paymentMethod})" else "PAID (${booking.paymentMethod})"),
                                    color = if (booking.paymentStatus == PaymentStatus.UNPAID) OnGoldContainer else EmeraldDark,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            if (booking.isReserve) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = GoldAccent
                                ) {
                                    Text(
                                        text = if (isBengali) "🌟 পুরো অটো রিজার্ভ" else "🌟 AUTO RESERVE",
                                        color = Slate900,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            if (booking.isAdvanceBooking) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = EmeraldContainer
                                ) {
                                    Text(
                                        text = if (isBengali) "৩০ মিনিট পূর্বে" else "Advance (30m+)",
                                        color = OnEmeraldContainer,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Route Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Slate50,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.TripOrigin, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = if (isBengali) "পিকআপ লোকেশন" else "Pickup Location", fontSize = 11.sp, color = Slate700)
                                    Text(text = booking.pickupLocation, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = if (isBengali) "গন্তব্য / ড্রপঅফ" else "Dropoff Destination", fontSize = 11.sp, color = Slate700)
                                    Text(text = booking.dropoffLocation, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Key Confirmation Details Grid
                    DetailRow(
                        label = if (isBengali) "যাত্রীর নাম" else "Passenger Name",
                        value = booking.passengerName
                    )
                    DetailRow(
                        label = if (isBengali) "মোবাইল নম্বর" else "Contact Phone",
                        value = booking.passengerPhone
                    )
                    DetailRow(
                        label = if (isBengali) "যাত্রার নির্ধারিত সময়" else "Scheduled Departure",
                        value = scheduledDateStr
                    )
                    DetailRow(
                        label = if (isBengali) "বুকিং করার সময়" else "Booked At",
                        value = bookedDateStr
                    )
                    DetailRow(
                        label = if (isBengali) "গাড়ির ক্যাটাগরি" else "Vehicle Class",
                        value = "${booking.vehicleCategory.labelBn} (${booking.vehicleModel})"
                    )
                    DetailRow(
                        label = if (isBengali) "গাড়ির নাম্বার প্লেট" else "Vehicle Plate #",
                        value = booking.vehiclePlate.ifEmpty { "নম্বর দ্রুত এসাইন হবে" }
                    )
                    DetailRow(
                        label = if (isBengali) "যাত্রী ও ব্যাগেজ" else "Passengers / Luggage",
                        value = "${booking.passengerCount} জন • ${booking.luggageDetails}"
                    )
                    if (booking.transactionId.isNotEmpty()) {
                        DetailRow(
                            label = if (isBengali) "ট্রানজেকশন আইডি" else "Transaction ID",
                            value = booking.transactionId
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Fare Box
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EmeraldContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (isBengali) "সর্বমোট ভাড়া (১৫ টাকা/কিমি হারে)" else "Total Fare (Rate: ৳15/km)",
                                    color = OnEmeraldContainer,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "দূরত্ব: ${String.format("%.1f", booking.distanceKm)} কিমি • রেট: ৳১৫/কিমি",
                                    color = EmeraldDark,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = "৳${booking.fare.toInt()}",
                                color = EmeraldDark,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // QR Code Canvas Simulation
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.5.dp, Slate800, RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawQrSimulation(size.width, size.height)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isBengali) "চালকের স্ক্যানিং এর জন্য ডিজিটাল ভেরিফিকেশন কিউআর" else "Digital Verification QR for Driver Scanning",
                            fontSize = 10.sp,
                            color = Slate700,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Action Buttons: Pay if unpaid, Download / Close
                    if (booking.paymentStatus == PaymentStatus.UNPAID) {
                        Button(
                            onClick = {
                                onDismiss()
                                onPayClicked(booking)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("pay_fare_now_button")
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBengali) "এখনই ভাড়া পরিশোধ করুন (বিকাশ / নগদ / কার্ড)" else "Pay Fare Now (bKash / Nagad / Card)",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("close_ticket_dialog_button")
                    ) {
                        Text(
                            text = if (isBengali) "বন্ধ করুন" else "Close Ticket",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Developer Watermark & Helpline
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Developed by MEJAN",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDark
                        )
                        Text(
                            text = "SafarGo 24/7 Helpline: +880 1700-MEJAN",
                            fontSize = 10.sp,
                            color = Slate700
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = Slate700)
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Slate900,
            textAlign = TextAlign.End,
            modifier = Modifier.widthIn(max = 190.dp)
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawQrSimulation(w: Float, h: Float) {
    val cornerSize = w * 0.28f

    // 3 Finder patterns
    fun drawFinder(x: Float, y: Float) {
        drawRect(Color.Black, Offset(x, y), Size(cornerSize, cornerSize))
        drawRect(Color.White, Offset(x + 4f, y + 4f), Size(cornerSize - 8f, cornerSize - 8f))
        drawRect(Color.Black, Offset(x + 8f, y + 8f), Size(cornerSize - 16f, cornerSize - 16f))
    }

    drawFinder(0f, 0f)
    drawFinder(w - cornerSize, 0f)
    drawFinder(0f, h - cornerSize)

    // Data matrix dots
    val dotSize = 6f
    for (i in 0..11) {
        for (j in 0..11) {
            val px = i * (w / 12f)
            val py = j * (h / 12f)
            // Skip corners
            val inTopLeft = px < cornerSize && py < cornerSize
            val inTopRight = px > (w - cornerSize) && py < cornerSize
            val inBottomLeft = px < cornerSize && py > (h - cornerSize)

            if (!inTopLeft && !inTopRight && !inBottomLeft) {
                if ((i * 7 + j * 13) % 3 == 0 || (i + j) % 2 == 0) {
                    drawRect(Color.Black, Offset(px, py), Size(dotSize, dotSize))
                }
            }
        }
    }
}
