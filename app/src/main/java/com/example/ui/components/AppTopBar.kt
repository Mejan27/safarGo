package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    role: UserRole,
    isBengali: Boolean,
    unreadNotifCount: Int,
    onRoleSwitch: (UserRole) -> Unit,
    onLanguageToggle: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenSos: () -> Unit
) {
    var roleMenuExpanded by remember { mutableStateOf(false) }

    Surface(
        color = EmeraldDark,
        tonalElevation = 6.dp,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand Header with Official SafarGo Logo
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White,
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .height(36.dp)
                                .width(56.dp)
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
                                text = if (isBengali) "সফরগো (SafarGo)" else "SafarGo",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Ride Smart • Travel Easy",
                                color = GoldAccent,
                                fontWeight = FontWeight.Medium,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                // Action Buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // SOS Emergency Quick Action
                    IconButton(
                        onClick = onOpenSos,
                        modifier = Modifier
                            .testTag("sos_button")
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ErrorRed.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sos,
                            contentDescription = "Emergency SOS",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Language Toggle
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .clickable { onLanguageToggle() }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .testTag("lang_toggle")
                    ) {
                        Text(
                            text = if (isBengali) "বাং" else "EN",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Notifications Bell with Badge
                    IconButton(
                        onClick = onOpenNotifications,
                        modifier = Modifier.testTag("notification_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadNotifCount > 0) {
                                    Badge(
                                        containerColor = GoldAccent,
                                        contentColor = Slate900
                                    ) {
                                        Text("$unreadNotifCount")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Role Switcher Pill Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isBengali) "রাজশাহী মহানগর অটো ও রিকশা সেবা" else "Rajshahi Metro Transit",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 11.sp
                )

                Box {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.12f),
                        modifier = Modifier
                            .clickable { roleMenuExpanded = true }
                            .testTag("role_switcher_pill")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (role) {
                                            UserRole.PASSENGER -> SuccessGreen
                                            UserRole.DRIVER -> GoldAccent
                                            UserRole.ADMIN -> InfoBlue
                                        }
                                    )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = when (role) {
                                    UserRole.PASSENGER -> if (isBengali) "যাত্রী মোড" else "Passenger Mode"
                                    UserRole.DRIVER -> if (isBengali) "ড্রাইভার মোড" else "Driver Mode"
                                    UserRole.ADMIN -> if (isBengali) "অ্যাডমিন প্যানেল" else "Admin Panel"
                                },
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Switch",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = roleMenuExpanded,
                        onDismissRequest = { roleMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Passenger / যাত্রী") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            onClick = {
                                roleMenuExpanded = false
                                onRoleSwitch(UserRole.PASSENGER)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Driver / ড্রাইভার") },
                            leadingIcon = { Icon(Icons.Default.DirectionsCar, contentDescription = null) },
                            onClick = {
                                roleMenuExpanded = false
                                onRoleSwitch(UserRole.DRIVER)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Admin / অ্যাডমিন") },
                            leadingIcon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null) },
                            onClick = {
                                roleMenuExpanded = false
                                onRoleSwitch(UserRole.ADMIN)
                            }
                        )
                    }
                }
            }
        }
    }
}
