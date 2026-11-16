package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.RideStatus
import com.example.data.model.UserRole
import com.example.ui.AppScreen
import com.example.ui.TransportViewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SurfaceLight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent()
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: TransportViewModel = viewModel()) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val activeRide by viewModel.activeRide.collectAsStateWithLifecycle()
    val driverLocation by viewModel.driverLocation.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val payments by viewModel.payments.collectAsStateWithLifecycle()
    val isBengali by viewModel.isBengali.collectAsStateWithLifecycle()
    val currentOperatingZone by viewModel.currentOperatingZone.collectAsStateWithLifecycle()
    val registeredDrivers by viewModel.registeredDrivers.collectAsStateWithLifecycle()
    val isDriverRegistrationOpen by viewModel.isDriverRegistrationOpen.collectAsStateWithLifecycle()
    val trackedDriverByPhone by viewModel.trackedDriverByPhone.collectAsStateWithLifecycle()

    val selectedBookingForSlip by viewModel.selectedBookingForSlip.collectAsStateWithLifecycle()
    val paymentTargetBooking by viewModel.paymentTargetBooking.collectAsStateWithLifecycle()
    val isChatOpen by viewModel.isChatOpen.collectAsStateWithLifecycle()
    val isNotificationSheetOpen by viewModel.isNotificationSheetOpen.collectAsStateWithLifecycle()
    val bannerMessage by viewModel.bannerMessage.collectAsStateWithLifecycle()

    var showSosDialog by remember { mutableStateOf(false) }

    val unreadNotifs = notifications.count { !it.isRead }

    // Handle back press
    BackHandler(enabled = currentScreen != AppScreen.PASSENGER_HOME && currentScreen != AppScreen.DRIVER_MODULE && currentScreen != AppScreen.ADMIN_DASHBOARD) {
        when (currentUser.role) {
            UserRole.PASSENGER -> viewModel.navigateTo(AppScreen.PASSENGER_HOME)
            UserRole.DRIVER -> viewModel.navigateTo(AppScreen.DRIVER_MODULE)
            UserRole.ADMIN -> viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentScreen != AppScreen.AUTH_LOGIN && currentScreen != AppScreen.LIVE_TRACKING) {
                AppTopBar(
                    role = currentUser.role,
                    isBengali = isBengali,
                    unreadNotifCount = unreadNotifs,
                    onRoleSwitch = { role -> viewModel.switchRole(role) },
                    onLanguageToggle = { viewModel.toggleLanguage() },
                    onOpenNotifications = { viewModel.openNotifications(true) },
                    onOpenSos = { showSosDialog = true }
                )
            }
        },
        bottomBar = {
            if (currentScreen != AppScreen.AUTH_LOGIN) {
                NavigationBar(
                    containerColor = EmeraldDark,
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    when (currentUser.role) {
                        UserRole.PASSENGER -> {
                            NavigationBarItem(
                                selected = currentScreen == AppScreen.PASSENGER_HOME,
                                onClick = { viewModel.navigateTo(AppScreen.PASSENGER_HOME) },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                label = { Text(if (isBengali) "বুকিং ও হোম" else "Book & Home", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = EmeraldDark,
                                    selectedTextColor = GoldAccent,
                                    indicatorColor = GoldAccent,
                                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.7f)
                                ),
                                modifier = Modifier.testTag("nav_passenger_home")
                            )
                            NavigationBarItem(
                                selected = currentScreen == AppScreen.LIVE_TRACKING,
                                onClick = {
                                    activeRide?.let { viewModel.selectActiveRide(it.id) }
                                        ?: viewModel.showBanner("বর্তমানে কোনো চলমান রাইড নেই")
                                },
                                icon = { Icon(Icons.Default.Navigation, contentDescription = "Live Track") },
                                label = { Text(if (isBengali) "লাইভ ট্র্যাকিং" else "Tracking", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = EmeraldDark,
                                    selectedTextColor = GoldAccent,
                                    indicatorColor = GoldAccent,
                                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.7f)
                                ),
                                modifier = Modifier.testTag("nav_passenger_tracking")
                            )
                            NavigationBarItem(
                                selected = currentScreen == AppScreen.AUTH_LOGIN,
                                onClick = { viewModel.navigateTo(AppScreen.AUTH_LOGIN) },
                                icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Account") },
                                label = { Text(if (isBengali) "প্রোফাইল / লগইন" else "Login", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = EmeraldDark,
                                    selectedTextColor = GoldAccent,
                                    indicatorColor = GoldAccent,
                                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.7f)
                                ),
                                modifier = Modifier.testTag("nav_passenger_account")
                            )
                        }

                        UserRole.DRIVER -> {
                            NavigationBarItem(
                                selected = currentScreen == AppScreen.DRIVER_MODULE,
                                onClick = { viewModel.navigateTo(AppScreen.DRIVER_MODULE) },
                                icon = { Icon(Icons.Default.DirectionsCar, contentDescription = "Terminal") },
                                label = { Text(if (isBengali) "ড্রাইভার টার্মিনাল" else "Terminal", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = EmeraldDark,
                                    selectedTextColor = GoldAccent,
                                    indicatorColor = GoldAccent,
                                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.7f)
                                ),
                                modifier = Modifier.testTag("nav_driver_terminal")
                            )
                            NavigationBarItem(
                                selected = currentScreen == AppScreen.LIVE_TRACKING,
                                onClick = {
                                    activeRide?.let { viewModel.selectActiveRide(it.id) }
                                        ?: viewModel.showBanner("বর্তমানে কোনো অ্যাসাইনড রাইড নেই")
                                },
                                icon = { Icon(Icons.Default.LocationOn, contentDescription = "Trip Radar") },
                                label = { Text(if (isBengali) "ম্যাপ রাডার" else "Map Radar", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = EmeraldDark,
                                    selectedTextColor = GoldAccent,
                                    indicatorColor = GoldAccent,
                                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.7f)
                                ),
                                modifier = Modifier.testTag("nav_driver_radar")
                            )
                            NavigationBarItem(
                                selected = currentScreen == AppScreen.AUTH_LOGIN,
                                onClick = { viewModel.navigateTo(AppScreen.AUTH_LOGIN) },
                                icon = { Icon(Icons.Default.SwitchAccount, contentDescription = "Switch") },
                                label = { Text(if (isBengali) "রোল পরিবর্তন" else "Switch Role", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = EmeraldDark,
                                    selectedTextColor = GoldAccent,
                                    indicatorColor = GoldAccent,
                                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.7f)
                                ),
                                modifier = Modifier.testTag("nav_driver_switch")
                            )
                        }

                        UserRole.ADMIN -> {
                            NavigationBarItem(
                                selected = currentScreen == AppScreen.ADMIN_DASHBOARD,
                                onClick = { viewModel.navigateTo(AppScreen.ADMIN_DASHBOARD) },
                                icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                                label = { Text(if (isBengali) "ড্যাশবোর্ড" else "Dashboard", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = EmeraldDark,
                                    selectedTextColor = GoldAccent,
                                    indicatorColor = GoldAccent,
                                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.7f)
                                ),
                                modifier = Modifier.testTag("nav_admin_dashboard")
                            )
                            NavigationBarItem(
                                selected = currentScreen == AppScreen.AUTH_LOGIN,
                                onClick = { viewModel.navigateTo(AppScreen.AUTH_LOGIN) },
                                icon = { Icon(Icons.Default.ExitToApp, contentDescription = "Switch") },
                                label = { Text(if (isBengali) "লগইন / সুইচ" else "Switch Role", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = EmeraldDark,
                                    selectedTextColor = GoldAccent,
                                    indicatorColor = GoldAccent,
                                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.7f)
                                ),
                                modifier = Modifier.testTag("nav_admin_switch")
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.AUTH_LOGIN -> {
                    AuthScreen(
                        isBengali = isBengali,
                        onLoginSuccess = { phone, role ->
                            viewModel.switchRole(role)
                        },
                        onQuickDemoLogin = { role ->
                            viewModel.switchRole(role)
                        }
                    )
                }

                AppScreen.PASSENGER_HOME -> {
                    PassengerHomeScreen(
                        bookings = bookings,
                        activeRide = activeRide,
                        currentOperatingZone = currentOperatingZone,
                        availableZones = viewModel.availableOperatingZones,
                        isBengali = isBengali,
                        onChangeZone = { viewModel.changeOperatingZone(it) },
                        onBookAdvanceRide = { pickup, dropoff, category, scheduledMillis, seats, notes, isReserve ->
                            viewModel.bookAdvanceRide(pickup, dropoff, category, scheduledMillis, seats, notes, isReserve)
                        },
                        onTrackRide = { rideId ->
                            viewModel.selectActiveRide(rideId)
                        },
                        onOpenChat = { ride ->
                            viewModel.openChat(true)
                        },
                        onOpenSlip = { ride ->
                            viewModel.openBookingSlip(ride)
                        },
                        onPayFare = { ride ->
                            viewModel.openPaymentModal(ride)
                        },
                        onOpenDriverRegistration = { viewModel.openDriverRegistration(true) },
                        onTrackDriverByPhone = { phone -> viewModel.searchAndTrackPhone(phone) }
                    )
                }

                AppScreen.LIVE_TRACKING -> {
                    if (activeRide != null) {
                        LiveTrackingScreen(
                            ride = activeRide!!,
                            driverLocation = driverLocation,
                            isBengali = isBengali,
                            onBack = {
                                if (currentUser.role == UserRole.DRIVER) {
                                    viewModel.navigateTo(AppScreen.DRIVER_MODULE)
                                } else {
                                    viewModel.navigateTo(AppScreen.PASSENGER_HOME)
                                }
                            },
                            onOpenChat = { viewModel.openChat(true) },
                            onOpenSlip = { viewModel.openBookingSlip(activeRide!!) },
                            onPayFare = { viewModel.openPaymentModal(activeRide!!) },
                            onOpenSos = { showSosDialog = true }
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("বর্তমানে কোনো সক্রিয় রাইড নেই", fontSize = 14.sp)
                        }
                    }
                }

                AppScreen.DRIVER_MODULE -> {
                    DriverHomeScreen(
                        driver = currentUser,
                        driverLocation = driverLocation,
                        bookings = bookings,
                        activeRide = activeRide,
                        isBengali = isBengali,
                        onToggleOnline = { viewModel.toggleDriverOnline(it) },
                        onToggleLocationSharing = { viewModel.toggleLocationSharing(it) },
                        onAcceptBooking = { viewModel.acceptBooking(it) },
                        onUpdateRideStatus = { id, status -> viewModel.updateRideStatus(id, status) },
                        onOpenChat = { viewModel.openChat(true) },
                        onOpenSlip = { viewModel.openBookingSlip(it) },
                        onOpenDriverRegistration = { viewModel.openDriverRegistration(true) }
                    )
                }

                AppScreen.ADMIN_DASHBOARD -> {
                    AdminDashboardScreen(
                        bookings = bookings,
                        payments = payments,
                        driverLocation = driverLocation,
                        currentOperatingZone = currentOperatingZone,
                        availableZones = viewModel.availableOperatingZones,
                        registeredDrivers = registeredDrivers,
                        isBengali = isBengali,
                        onChangeZone = { viewModel.changeOperatingZone(it) },
                        onViewBookingSlip = { viewModel.openBookingSlip(it) },
                        onVerifyDriver = { viewModel.verifyDriver(it) },
                        onDeleteDriver = { viewModel.deleteDriver(it) },
                        onTrackDriverByPhone = { viewModel.searchAndTrackPhone(it) },
                        onOpenDriverRegistration = { viewModel.openDriverRegistration(true) }
                    )
                }

                else -> {}
            }

            // Notification / Toast Banner
            AnimatedVisibility(
                visible = bannerMessage != null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                bannerMessage?.let { msg ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EmeraldDark,
                        shadowElevation = 6.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = msg,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { viewModel.clearBanner() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Dialogs & Sheets
    selectedBookingForSlip?.let { booking ->
        ConfirmationTicketDialog(
            booking = booking,
            isBengali = isBengali,
            onDismiss = { viewModel.closeBookingSlip() },
            onPayClicked = { viewModel.openPaymentModal(it) }
        )
    }

    paymentTargetBooking?.let { booking ->
        PaymentDialog(
            booking = booking,
            isBengali = isBengali,
            onDismiss = { viewModel.closePaymentModal() },
            onConfirmPayment = { id, gateway, acc ->
                viewModel.processPayment(id, gateway, acc)
            }
        )
    }

    if (isChatOpen && activeRide != null) {
        val rideId = activeRide!!.id
        val rideChat = chatMessages.filter { it.rideId == rideId }
        ChatBottomSheet(
            ride = activeRide!!,
            currentUserRole = currentUser.role,
            messages = rideChat,
            isBengali = isBengali,
            onSendMessage = { viewModel.sendChatMessage(rideId, it) },
            onDismiss = { viewModel.openChat(false) }
        )
    }

    if (isNotificationSheetOpen) {
        NotificationSheet(
            notifications = notifications,
            isBengali = isBengali,
            onDismiss = { viewModel.openNotifications(false) },
            onMarkRead = { viewModel.markNotificationRead(it) },
            onClearAll = { viewModel.clearAllNotifications() },
            onSelectRide = { rideId ->
                viewModel.openNotifications(false)
                viewModel.selectActiveRide(rideId)
            }
        )
    }

    if (showSosDialog) {
        SosDialog(
            isBengali = isBengali,
            onDismiss = { showSosDialog = false }
        )
    }

    if (isDriverRegistrationOpen) {
        RickshawDriverRegistrationDialog(
            isBengali = isBengali,
            onDismiss = { viewModel.openDriverRegistration(false) },
            onSubmit = { driver ->
                viewModel.registerRickshawDriver(driver)
            }
        )
    }

    trackedDriverByPhone?.let { loc ->
        PhoneTrackingDialog(
            location = loc,
            searchedPhone = loc.driverPhone,
            isBengali = isBengali,
            onDismiss = { viewModel.clearTrackedPhone() },
            onBookWithDriver = { stand ->
                viewModel.clearTrackedPhone()
                viewModel.changeOperatingZone(stand)
            }
        )
    }
}
