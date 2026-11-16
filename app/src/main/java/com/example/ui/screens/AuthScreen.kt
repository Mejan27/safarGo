package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserRole
import com.example.ui.components.AutoRickshawIcon
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    isBengali: Boolean,
    onLoginSuccess: (phone: String, role: UserRole) -> Unit,
    onQuickDemoLogin: (UserRole) -> Unit
) {
    var selectedRole by remember { mutableStateOf(UserRole.PASSENGER) }
    var phoneInput by remember { mutableStateOf("01711234567") }
    var pinInput by remember { mutableStateOf("1234") }
    var isSignUp by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = EmeraldDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .height(90.dp)
                        .width(180.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_safargo_logo),
                        contentDescription = "SafarGo Official Logo",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentScale = androidx.compose.ui.layout.ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (isBengali) "সফরগো (SafarGo) রাজশাহী" else "SafarGo Rajshahi",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Ride Smart. Travel Easy. • Developed by MEJAN",
                    fontSize = 11.sp,
                    color = GoldAccent,
                    fontWeight = FontWeight.Medium
                )
            }

            // Auth Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isSignUp) "নতুন একাউন্ট তৈরি করুন" else (if (isBengali) "লগইন করুন" else "Sign In"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Slate900
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Role Tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Slate100)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf(
                            UserRole.PASSENGER to "যাত্রী",
                            UserRole.DRIVER to "ড্রাইভার",
                            UserRole.ADMIN to "অ্যাডমিন"
                        ).forEach { (role, label) ->
                            val isSelected = selectedRole == role
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) EmeraldDark else Color.Transparent,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedRole = role }
                                    .testTag("auth_role_tab_${role.name}")
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else Slate700,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val authFieldColors = OutlinedTextFieldDefaults.colors(
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
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text("মোবাইল নম্বর (+880)", color = Color.Black, fontWeight = FontWeight.Medium) },
                        placeholder = { Text("01711234567", color = Color.Black) },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = EmeraldDark) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_phone_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = authFieldColors
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { pinInput = it },
                        label = { Text("গোপন পিন / পাসওয়ার্ড (1234)", color = Color.Black, fontWeight = FontWeight.Medium) },
                        placeholder = { Text("•••• (1234)", color = Color.Black) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldDark) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_pin_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = authFieldColors
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = { onLoginSuccess(phoneInput, selectedRole) },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("auth_submit_button")
                    ) {
                        Text(
                            text = if (isSignUp) "একাউন্ট খুলুন" else "প্রবেশ করুন",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Switch Demo Profiles
                    Text(
                        text = "— অথবা এক-ট্যাপে ডেমো প্রোফাইলে প্রবেশ করুন —",
                        fontSize = 10.sp,
                        color = Slate700
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onQuickDemoLogin(UserRole.PASSENGER) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("demo_passenger_btn"),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                        ) {
                            Text("যাত্রী ডেমো", fontSize = 11.sp, maxLines = 1)
                        }

                        OutlinedButton(
                            onClick = { onQuickDemoLogin(UserRole.DRIVER) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("demo_driver_btn"),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                        ) {
                            Text("ড্রাইভার ডেমো", fontSize = 11.sp, maxLines = 1)
                        }

                        OutlinedButton(
                            onClick = { onQuickDemoLogin(UserRole.ADMIN) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("demo_admin_btn"),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                        ) {
                            Text("অ্যাডমিন ডেমো", fontSize = 11.sp, maxLines = 1)
                        }
                    }
                }
            }

            // Developer Signature
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = "SafarGo Transport Service",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Developed by MEJAN",
                    color = GoldAccent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}
