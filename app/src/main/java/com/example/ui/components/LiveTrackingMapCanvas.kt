package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DriverLocation
import com.example.ui.theme.*
import kotlin.math.*

@Composable
fun LiveTrackingMapCanvas(
    driverLocation: DriverLocation,
    progressFraction: Float,
    pickupName: String,
    dropoffName: String,
    estimatedMinutes: Int,
    distanceKm: Double,
    isDarkMap: Boolean = true,
    modifier: Modifier = Modifier
) {
    // Pulse animation for radar effect around the vehicle
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 16f,
        targetValue = 44f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha"
    )

    var zoomLevel by remember { mutableFloatStateOf(1.0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(340.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (isDarkMap) Color(0xFF0F1720) else Color(0xFFE2E8F0))
            .testTag("live_tracking_map_canvas")
    ) {
        // Custom Canvas Map
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Background grid & street networks
            drawMapGridAndRoads(canvasWidth, canvasHeight, isDarkMap)

            // Calculate Path Waypoints
            val startPoint = Offset(canvasWidth * 0.18f, canvasHeight * 0.78f)
            val endPoint = Offset(canvasWidth * 0.82f, canvasHeight * 0.22f)
            val controlPoint1 = Offset(canvasWidth * 0.25f, canvasHeight * 0.35f)
            val controlPoint2 = Offset(canvasWidth * 0.75f, canvasHeight * 0.65f)

            // Draw route polyline
            val path = Path().apply {
                moveTo(startPoint.x, startPoint.y)
                cubicTo(
                    controlPoint1.x, controlPoint1.y,
                    controlPoint2.x, controlPoint2.y,
                    endPoint.x, endPoint.y
                )
            }

            // Route casing & glowing path
            drawPath(
                path = path,
                color = if (isDarkMap) Color(0xFF0D3B23) else Color(0xFF94A3B8),
                style = Stroke(width = 12f * zoomLevel, cap = StrokeCap.Round)
            )
            drawPath(
                path = path,
                color = EmeraldLight,
                style = Stroke(
                    width = 6f * zoomLevel,
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)
                )
            )

            // Draw Pickup Marker
            drawCircle(
                color = SuccessGreen,
                radius = 10f * zoomLevel,
                center = startPoint
            )
            drawCircle(
                color = Color.White,
                radius = 4f * zoomLevel,
                center = startPoint
            )

            // Draw Dropoff Marker
            drawCircle(
                color = ErrorRed,
                radius = 12f * zoomLevel,
                center = endPoint
            )
            drawCircle(
                color = GoldAccent,
                radius = 6f * zoomLevel,
                center = endPoint
            )

            // Calculate Vehicle Position along the cubic bezier
            val t = progressFraction.coerceIn(0.05f, 0.95f)
            val vehicleX = (1 - t).pow(3) * startPoint.x +
                    3 * (1 - t).pow(2) * t * controlPoint1.x +
                    3 * (1 - t) * t.pow(2) * controlPoint2.x +
                    t.pow(3) * endPoint.x

            val vehicleY = (1 - t).pow(3) * startPoint.y +
                    3 * (1 - t).pow(2) * t * controlPoint1.y +
                    3 * (1 - t) * t.pow(2) * controlPoint2.y +
                    t.pow(3) * endPoint.y

            val vehiclePos = Offset(vehicleX, vehicleY)

            // Pulse Radar Ring around vehicle
            if (driverLocation.isSharingLocation) {
                drawCircle(
                    color = EmeraldLight.copy(alpha = pulseAlpha),
                    radius = pulseRadius * zoomLevel,
                    center = vehiclePos,
                    style = Stroke(width = 2.5f)
                )
            }

            // Driver Car Marker Body
            drawCircle(
                color = EmeraldDark,
                radius = 18f * zoomLevel,
                center = vehiclePos
            )
            drawCircle(
                color = GoldAccent,
                radius = 14f * zoomLevel,
                center = vehiclePos
            )
            drawCircle(
                color = Color.White,
                radius = 8f * zoomLevel,
                center = vehiclePos
            )

            // Heading indicator line
            val headingRad = Math.toRadians(driverLocation.headingDegrees.toDouble())
            val headingEnd = Offset(
                vehiclePos.x + (22f * zoomLevel * sin(headingRad)).toFloat(),
                vehiclePos.y - (22f * zoomLevel * cos(headingRad)).toFloat()
            )
            drawLine(
                color = Slate900,
                start = vehiclePos,
                end = headingEnd,
                strokeWidth = 3f * zoomLevel,
                cap = StrokeCap.Round
            )
        }

        // Overlay: Top Status Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Slate900.copy(alpha = 0.88f),
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (driverLocation.isSharingLocation) SuccessGreen else ErrorRed)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (driverLocation.isSharingLocation) "GPS LIVE: 4G" else "GPS PAUSED",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• ${String.format("%.1f", driverLocation.speedKmh)} km/h",
                        color = GoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Zoom In / Out Controls
            Row {
                IconButton(
                    onClick = { zoomLevel = (zoomLevel + 0.15f).coerceAtMost(1.6f) },
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Slate900.copy(alpha = 0.85f))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Zoom In", tint = Color.White, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(
                    onClick = { zoomLevel = (zoomLevel - 0.15f).coerceAtLeast(0.7f) },
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Slate900.copy(alpha = 0.85f))
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Zoom Out", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
        }

        // Overlay: Bottom Info Card (ETA & Coordinates)
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(14.dp),
            color = Slate900.copy(alpha = 0.92f),
            tonalElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "$pickupName ➔ $dropoffName",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    val autoFare = (Math.round((distanceKm * 15.0) / 5.0) * 5.0).toInt().coerceAtLeast(15)
                    Text(
                        text = "স্বয়ংক্রিয় দূরত্ব: ${String.format("%.1f", distanceKm)} কিমি • ভাড়া: ৳$autoFare (১৫৳/কিমি) • ETA: $estimatedMinutes মিনিট",
                        color = GoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = EmeraldDark
                ) {
                    Text(
                        text = "লাইভ ট্র্যাকার",
                        color = EmeraldContainer,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawMapGridAndRoads(width: Float, height: Float, isDark: Boolean) {
    val roadColor = if (isDark) Color(0xFF1E293B) else Color(0xFFCBD5E1)
    val minorRoadColor = if (isDark) Color(0xFF15202B) else Color(0xFFE2E8F0)

    // Minor road grid
    val spacing = 36f
    var x = 0f
    while (x < width) {
        drawLine(
            color = minorRoadColor,
            start = Offset(x, 0f),
            end = Offset(x, height),
            strokeWidth = 1.2f
        )
        x += spacing
    }
    var y = 0f
    while (y < height) {
        drawLine(
            color = minorRoadColor,
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = 1.2f
        )
        y += spacing
    }

    // Major Arterials & Flyovers
    drawLine(
        color = roadColor,
        start = Offset(0f, height * 0.45f),
        end = Offset(width, height * 0.45f),
        strokeWidth = 9f
    )
    drawLine(
        color = roadColor,
        start = Offset(width * 0.5f, 0f),
        end = Offset(width * 0.5f, height),
        strokeWidth = 9f
    )
    // Diagonal express route
    drawLine(
        color = roadColor,
        start = Offset(0f, 0f),
        end = Offset(width, height),
        strokeWidth = 14f
    )
}
