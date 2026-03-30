package com.saas.fastbite.screens.restaurant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.saas.fastbite.screens.client.AddDialogField
import com.saas.fastbite.ui.theme.Cream
import com.saas.fastbite.ui.theme.DeepBrown
import com.saas.fastbite.ui.theme.WarmAmber

// ── Data models ───────────────────────────────────────────────────────────────

data class WorkingHour(
    val day: String,
    var openTime: String,
    var closeTime: String,
    var isOpen: Boolean
)

val defaultWorkingHours = mutableListOf(
    WorkingHour("Monday",    "09:00 AM", "11:00 PM", true),
    WorkingHour("Tuesday",   "09:00 AM", "11:00 PM", true),
    WorkingHour("Wednesday", "09:00 AM", "11:00 PM", true),
    WorkingHour("Thursday",  "09:00 AM", "11:00 PM", true),
    WorkingHour("Friday",    "09:00 AM", "12:00 AM", true),
    WorkingHour("Saturday",  "10:00 AM", "12:00 AM", true),
    WorkingHour("Sunday",    "10:00 AM", "10:00 PM", false),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun RestaurantProfileScreen(
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var restaurantName by remember { mutableStateOf("Burger Lab") }
    var ownerName by remember { mutableStateOf("Hassan Raza") }
    var phone by remember { mutableStateOf("+92 321 9876543") }
    var email by remember { mutableStateOf("burgerlab@email.com") }
    var address by remember { mutableStateOf("Main Shahrae Faisal, Karachi") }
    var description by remember { mutableStateOf("Premium burgers made with 100% beef patties, fresh ingredients and secret sauces.") }
    var isOpen by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var autoAcceptEnabled by remember { mutableStateOf(false) }
    var hoursExpanded by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val workingHours = remember { mutableStateListOf(*defaultWorkingHours.toTypedArray()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // ── Top bar
            item { RestaurantProfileTopBar(onBack = onBack) }

            // ── Hero card
            item {
                RestaurantHeroCard(
                    restaurantName = restaurantName,
                    ownerName = ownerName,
                    isOpen = isOpen,
                    onToggleOpen = { isOpen = !isOpen },
                    onEditClick = { showEditDialog = true }
                )
            }

            // ── Quick stats
            item { RestaurantProfileStats() }

            // ── Info section
            item {
                RestaurantProfileSectionTitle("📋 Restaurant Info")
                RestaurantInfoCard(
                    phone = phone,
                    email = email,
                    address = address,
                    description = description
                )
            }

            // ── Working hours
            item {
                RestaurantProfileSectionTitle("🕐 Working Hours")
                WorkingHoursSection(
                    hours = workingHours,
                    expanded = hoursExpanded,
                    onToggle = { hoursExpanded = !hoursExpanded },
                    onToggleDay = { index ->
                        workingHours[index] = workingHours[index].copy(
                            isOpen = !workingHours[index].isOpen
                        )
                    }
                )
            }

            // ── Preferences
            item {
                RestaurantProfileSectionTitle("⚙️ Preferences")
                RestaurantPreferencesCard(
                    notificationsEnabled = notificationsEnabled,
                    onNotificationsToggle = { notificationsEnabled = it },
                    autoAcceptEnabled = autoAcceptEnabled,
                    onAutoAcceptToggle = { autoAcceptEnabled = it }
                )
            }

            // ── Account settings
            item {
                RestaurantProfileSectionTitle("👤 Account")
                RestaurantAccountCard()
            }

            // ── Danger zone
            item {
                RestaurantProfileSectionTitle("⚠️ Danger Zone")
                RestaurantDangerCard(
                    onLogout = { showLogoutDialog = true }
                )
            }
        }
    }

    // ── Edit dialog
    if (showEditDialog) {
        EditRestaurantDialog(
            restaurantName = restaurantName,
            ownerName = ownerName,
            phone = phone,
            email = email,
            address = address,
            description = description,
            onSave = { rName, oName, ph, em, addr, desc ->
                restaurantName = rName
                ownerName = oName
                phone = ph
                email = em
                address = addr
                description = desc
                showEditDialog = false
            },
            onDismiss = { showEditDialog = false }
        )
    }

    // ── Logout dialog
    if (showLogoutDialog) {
        RestaurantLogoutDialog(
            onConfirm = {
                showLogoutDialog = false
                onLogout()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
fun RestaurantProfileTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = DeepBrown,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = "Restaurant Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
        }
    }
}

// ── Hero card ─────────────────────────────────────────────────────────────────

@Composable
fun RestaurantHeroCard(
    restaurantName: String,
    ownerName: String,
    isOpen: Boolean,
    onToggleOpen: () -> Unit,
    onEditClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(DeepBrown)
            .padding(24.dp)
    ) {
        // Decorative circles
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.04f))
                .align(Alignment.TopEnd)
                .offset(x = 20.dp, y = (-20).dp)
        )
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.03f))
                .align(Alignment.BottomStart)
                .offset(x = (-10).dp, y = 10.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo box
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(WarmAmber),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🍔", fontSize = 32.sp)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = restaurantName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Cream
                        )
                        Text(
                            text = "Owner: $ownerName",
                            fontSize = 13.sp,
                            color = Cream.copy(alpha = 0.6f)
                        )
                        // Open/Close toggle
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isOpen) Color(0xFF4CAF50).copy(alpha = 0.2f)
                                    else Color(0xFFE53935).copy(alpha = 0.2f)
                                )
                                .clickable { onToggleOpen() }
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isOpen) Color(0xFF4CAF50)
                                        else Color(0xFFE53935)
                                    )
                            )
                            Text(
                                text = if (isOpen) "Open Now" else "Closed",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isOpen) Color(0xFF4CAF50)
                                else Color(0xFFE53935)
                            )
                        }
                    }
                }

                // Edit button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(WarmAmber.copy(alpha = 0.2f))
                        .clickable { onEditClick() }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "✏️ Edit",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = WarmAmber
                    )
                }
            }

            // Verification badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.06f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "✅", fontSize = 14.sp)
                Text(
                    text = "Verified Restaurant · ID: REST-2041",
                    fontSize = 12.sp,
                    color = Cream.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// ── Profile stats ─────────────────────────────────────────────────────────────

@Composable
fun RestaurantProfileStats() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        listOf(
            Triple("203",     "Total Orders",  Color(0xFFE3F2FD)),
            Triple("4.8 ⭐",  "Rating",        Color(0xFFFFF8E1)),
            Triple("142 🪙",  "Tokens Left",   Color(0xFFFFF3E0)),
            Triple("2 yrs",   "Member Since",  Color(0xFFE8F5E9)),
        ).forEach { (value, label, bg) ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(bg)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = value,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBrown,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = label,
                    fontSize = 9.sp,
                    color = DeepBrown.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    lineHeight = 13.sp
                )
            }
        }
    }
}

// ── Section title ─────────────────────────────────────────────────────────────

@Composable
fun RestaurantProfileSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = DeepBrown,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
    )
}

// ── Info card ─────────────────────────────────────────────────────────────────

@Composable
fun RestaurantInfoCard(
    phone: String,
    email: String,
    address: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InfoRow(Icons.Default.Phone,       Color(0xFFE8F5E9), Color(0xFF2E7D32), "Phone",       phone)
        HorizontalDivider(color = DeepBrown.copy(alpha = 0.05f))
        InfoRow(Icons.Default.Email,       Color(0xFFE3F2FD), Color(0xFF1565C0), "Email",       email)
        HorizontalDivider(color = DeepBrown.copy(alpha = 0.05f))
        InfoRow(Icons.Default.LocationOn,  Color(0xFFFCE4EC), Color(0xFFE53935), "Address",     address)
        HorizontalDivider(color = DeepBrown.copy(alpha = 0.05f))
        InfoRow(Icons.Default.Description, Color(0xFFFFF3E0), WarmAmber,         "Description", description)
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 11.sp,
                color = DeepBrown.copy(alpha = 0.4f)
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = DeepBrown,
                lineHeight = 18.sp
            )
        }
    }
}

// ── Working hours ─────────────────────────────────────────────────────────────

@Composable
fun WorkingHoursSection(
    hours: List<WorkingHour>,
    expanded: Boolean,
    onToggle: () -> Unit,
    onToggleDay: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val openDays = hours.count { it.isOpen }
            Text(
                text = "$openDays days open this week",
                fontSize = 14.sp,
                color = DeepBrown.copy(alpha = 0.6f)
            )
            Text(
                text = if (expanded) "▲ Hide" else "▼ Show",
                fontSize = 12.sp,
                color = WarmAmber,
                fontWeight = FontWeight.SemiBold
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                HorizontalDivider(color = DeepBrown.copy(alpha = 0.05f))
                hours.forEachIndexed { index, hour ->
                    WorkingHourRow(
                        hour = hour,
                        onToggle = { onToggleDay(index) }
                    )
                    if (index < hours.size - 1) {
                        HorizontalDivider(
                            color = DeepBrown.copy(alpha = 0.04f),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WorkingHourRow(hour: WorkingHour, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Day
        Text(
            text = hour.day.take(3),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (hour.isOpen) DeepBrown else DeepBrown.copy(alpha = 0.3f),
            modifier = Modifier.width(36.dp)
        )

        // Hours
        if (hour.isOpen) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(WarmAmber.copy(alpha = 0.08f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${hour.openTime} – ${hour.closeTime}",
                    fontSize = 12.sp,
                    color = DeepBrown.copy(alpha = 0.7f)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFEBEE))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Closed",
                    fontSize = 12.sp,
                    color = Color(0xFFE53935).copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = hour.isOpen,
            onCheckedChange = { onToggle() },
            modifier = Modifier.height(22.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = WarmAmber,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = DeepBrown.copy(alpha = 0.2f)
            )
        )
    }
}

// ── Preferences card ──────────────────────────────────────────────────────────

@Composable
fun RestaurantPreferencesCard(
    notificationsEnabled: Boolean,
    onNotificationsToggle: (Boolean) -> Unit,
    autoAcceptEnabled: Boolean,
    onAutoAcceptToggle: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
    ) {
        RestaurantToggleRow(
            icon = Icons.Default.Notifications,
            iconBg = Color(0xFFE3F2FD),
            iconTint = Color(0xFF1565C0),
            label = "Order Notifications",
            subtitle = "Get alerts for new orders",
            checked = notificationsEnabled,
            onToggle = onNotificationsToggle
        )
        HorizontalDivider(
            color = DeepBrown.copy(alpha = 0.05f),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        RestaurantToggleRow(
            icon = Icons.Default.AutoMode,
            iconBg = Color(0xFFE8F5E9),
            iconTint = Color(0xFF2E7D32),
            label = "Auto-Accept Orders",
            subtitle = "Skip manual confirmation",
            checked = autoAcceptEnabled,
            onToggle = onAutoAcceptToggle
        )
    }
}

@Composable
fun RestaurantToggleRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    label: String,
    subtitle: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = DeepBrown
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = DeepBrown.copy(alpha = 0.45f)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            modifier = Modifier.height(24.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = WarmAmber,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = DeepBrown.copy(alpha = 0.2f)
            )
        )
    }
}

// ── Account card ──────────────────────────────────────────────────────────────

@Composable
fun RestaurantAccountCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
    ) {
        listOf(
            Triple(Icons.Default.Analytics,   Pair(Color(0xFFFFF3E0), WarmAmber),          "Analytics & Reports"),
            Triple(Icons.Default.Payment,      Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32)),  "Payment & Billing"),
            Triple(Icons.Default.Group,        Pair(Color(0xFFE3F2FD), Color(0xFF1565C0)),  "Manage Riders"),
            Triple(Icons.Default.Help,         Pair(Color(0xFFF3E5F5), Color(0xFF7B1FA2)),  "Help & Support"),
            Triple(Icons.Default.Policy,       Pair(Color(0xFFE8EAF6), Color(0xFF3949AB)),  "Terms & Privacy"),
            Triple(Icons.Default.Info,         Pair(Color(0xFFFCE4EC), Color(0xFFE53935)),  "App Version v1.0.0"),
        ).forEachIndexed { index, (icon, colors, label) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(colors.first),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = colors.second,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepBrown,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "›",
                    fontSize = 22.sp,
                    color = DeepBrown.copy(alpha = 0.2f)
                )
            }
            if (index < 5) {
                HorizontalDivider(
                    color = DeepBrown.copy(alpha = 0.05f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

// ── Danger card ───────────────────────────────────────────────────────────────

@Composable
fun RestaurantDangerCard(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Logout
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFFFEBEE))
                .border(
                    1.dp,
                    Color(0xFFE53935).copy(alpha = 0.2f),
                    RoundedCornerShape(16.dp)
                )
                .clickable { onLogout() }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Log Out",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFE53935)
                )
            }
        }

        // Deactivate account
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(
                    1.dp,
                    DeepBrown.copy(alpha = 0.1f),
                    RoundedCornerShape(16.dp)
                )
                .clickable { }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Block,
                    contentDescription = null,
                    tint = DeepBrown.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Request Deactivation",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepBrown.copy(alpha = 0.4f)
                )
            }
        }
    }
}

// ── Edit restaurant dialog ────────────────────────────────────────────────────

@Composable
fun EditRestaurantDialog(
    restaurantName: String,
    ownerName: String,
    phone: String,
    email: String,
    address: String,
    description: String,
    onSave: (String, String, String, String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var rName by remember { mutableStateOf(restaurantName) }
    var oName by remember { mutableStateOf(ownerName) }
    var ph by remember { mutableStateOf(phone) }
    var em by remember { mutableStateOf(email) }
    var addr by remember { mutableStateOf(address) }
    var desc by remember { mutableStateOf(description) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Edit Restaurant",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )

            // Logo preview
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(WarmAmber)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🍔", fontSize = 28.sp)
            }

            AddDialogField("Restaurant Name", rName,  { rName = it })
            AddDialogField("Owner Name",      oName,  { oName = it })
            AddDialogField("Phone",           ph,     { ph = it })
            AddDialogField("Email",           em,     { em = it })
            AddDialogField("Address",         addr,   { addr = it })
            AddDialogField("Description",     desc,   { desc = it })

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = DeepBrown
                    )
                ) { Text("Cancel") }

                Button(
                    onClick = {
                        if (rName.isNotEmpty())
                            onSave(rName, oName, ph, em, addr, desc)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WarmAmber,
                        contentColor = Cream
                    ),
                    enabled = rName.isNotEmpty()
                ) {
                    Text("Save", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// ── Logout dialog ─────────────────────────────────────────────────────────────

@Composable
fun RestaurantLogoutDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "👋", fontSize = 44.sp)
            Text(
                text = "Log Out?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
            Text(
                text = "You'll need to log in again\nto manage your restaurant.",
                fontSize = 13.sp,
                color = DeepBrown.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Yes, Log Out",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    color = DeepBrown.copy(alpha = 0.4f),
                    fontSize = 14.sp
                )
            }
        }
    }
}