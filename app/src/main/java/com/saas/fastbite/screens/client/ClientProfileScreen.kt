package com.saas.fastbite.screens.client

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
import com.saas.fastbite.screens.shared.AddDialogField
import com.saas.fastbite.ui.theme.Cream
import com.saas.fastbite.ui.theme.DeepBrown
import com.saas.fastbite.ui.theme.WarmAmber

// ── Data models ───────────────────────────────────────────────────────────────

data class SavedAddress(
    val id: String,
    val label: String,
    val address: String,
    val emoji: String,
    val isDefault: Boolean = false
)

data class ProfileSetting(
    val id: String,
    val icon: ImageVector,
    val label: String,
    val subtitle: String? = null,
    val trailing: ProfileTrailing = ProfileTrailing.ARROW,
    val iconBackground: Color = Color(0xFFF5F5F5),
    val iconTint: Color = DeepBrown
)

enum class ProfileTrailing { ARROW, TOGGLE, BADGE }

val savedAddresses = mutableListOf(
    SavedAddress("a1", "Home",   "Block 5, Gulshan-e-Iqbal, Karachi",  "🏠", true),
    SavedAddress("a2", "Office", "I.I. Chundrigar Road, Karachi",       "🏢", false),
    SavedAddress("a3", "Family", "North Nazimabad, Block H, Karachi",   "❤️", false),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun ClientProfileScreen(
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var name by remember { mutableStateOf("Ahmed Khan") }
    var phone by remember { mutableStateOf("+92 300 1234567") }
    var email by remember { mutableStateOf("ahmed@email.com") }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var locationEnabled by remember { mutableStateOf(true) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showAddressDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var addressExpanded by remember { mutableStateOf(false) }
    val addresses = remember { mutableStateListOf(*savedAddresses.toTypedArray()) }

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
            item {
                ProfileTopBar(onBack = onBack)
            }

            // ── Avatar card
            item {
                ProfileAvatarCard(
                    name = name,
                    phone = phone,
                    email = email,
                    onEditClick = { showEditDialog = true }
                )
            }

            // ── Stats row
            item { ProfileStatsRow() }

            // ── Addresses section
            item {
                ProfileSectionTitle("📍 Saved Addresses")
                AddressSection(
                    addresses = addresses,
                    expanded = addressExpanded,
                    onToggle = { addressExpanded = !addressExpanded },
                    onAddClick = { showAddressDialog = true },
                    onSetDefault = { id ->
                        addresses.replaceAll {
                            it.copy(isDefault = it.id == id)
                        }
                    },
                    onDelete = { id ->
                        addresses.removeIf { it.id == id }
                    }
                )
            }

            // ── Preferences
            item {
                ProfileSectionTitle("⚙️ Preferences")
                ProfileCard {
                    ToggleSettingRow(
                        icon = Icons.Default.Notifications,
                        iconBg = Color(0xFFE3F2FD),
                        iconTint = Color(0xFF1565C0),
                        label = "Push Notifications",
                        subtitle = "Order updates & offers",
                        checked = notificationsEnabled,
                        onToggle = { notificationsEnabled = it }
                    )
                    ProfileDivider()
                    ToggleSettingRow(
                        icon = Icons.Default.LocationOn,
                        iconBg = Color(0xFFE8F5E9),
                        iconTint = Color(0xFF2E7D32),
                        label = "Location Access",
                        subtitle = "For faster delivery",
                        checked = locationEnabled,
                        onToggle = { locationEnabled = it }
                    )
                }
            }

            // ── Account settings
            item {
                ProfileSectionTitle("👤 Account")
                ProfileCard {
                    ArrowSettingRow(
                        icon = Icons.Default.History,
                        iconBg = Color(0xFFFFF3E0),
                        iconTint = WarmAmber,
                        label = "Order History",
                        subtitle = "8 past orders"
                    )
                    ProfileDivider()
                    ArrowSettingRow(
                        icon = Icons.Default.Favorite,
                        iconBg = Color(0xFFFFEBEE),
                        iconTint = Color(0xFFE53935),
                        label = "Favourites",
                        subtitle = "3 saved restaurants"
                    )
                    ProfileDivider()
                    ArrowSettingRow(
                        icon = Icons.Default.Payment,
                        iconBg = Color(0xFFE8F5E9),
                        iconTint = Color(0xFF2E7D32),
                        label = "Payment Methods",
                        subtitle = "COD · Add card"
                    )
                    ProfileDivider()
                    ArrowSettingRow(
                        icon = Icons.Default.Star,
                        iconBg = Color(0xFFFFF8E1),
                        iconTint = Color(0xFFF57F17),
                        label = "My Reviews",
                        subtitle = "5 reviews given"
                    )
                }
            }

            // ── Support
            item {
                ProfileSectionTitle("🆘 Support")
                ProfileCard {
                    ArrowSettingRow(
                        icon = Icons.Default.Help,
                        iconBg = Color(0xFFE3F2FD),
                        iconTint = Color(0xFF1565C0),
                        label = "Help & FAQ",
                        subtitle = "Get answers fast"
                    )
                    ProfileDivider()
                    ArrowSettingRow(
                        icon = Icons.Default.Chat,
                        iconBg = Color(0xFFFCE4EC),
                        iconTint = Color(0xFFE53935),
                        label = "Contact Support",
                        subtitle = "Chat with us"
                    )
                    ProfileDivider()
                    ArrowSettingRow(
                        icon = Icons.Default.Policy,
                        iconBg = Color(0xFFF3E5F5),
                        iconTint = Color(0xFF7B1FA2),
                        label = "Privacy Policy",
                        subtitle = null
                    )
                    ProfileDivider()
                    ArrowSettingRow(
                        icon = Icons.Default.Info,
                        iconBg = Color(0xFFE8EAF6),
                        iconTint = Color(0xFF3949AB),
                        label = "App Version",
                        subtitle = "v1.0.0"
                    )
                }
            }

            // ── Logout button
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFFEBEE))
                        .border(
                            1.dp,
                            Color(0xFFE53935).copy(alpha = 0.2f),
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { showLogoutDialog = true }
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
            }
        }
    }

    // ── Edit profile dialog
    if (showEditDialog) {
        EditProfileDialog(
            name = name,
            email = email,
            onSave = { newName, newEmail ->
                name = newName
                email = newEmail
                showEditDialog = false
            },
            onDismiss = { showEditDialog = false }
        )
    }

    // ── Add address dialog
    if (showAddressDialog) {
        AddAddressDialog(
            onAdd = { label, address, emoji ->
                addresses.add(
                    SavedAddress(
                        id = "a${System.currentTimeMillis()}",
                        label = label,
                        address = address,
                        emoji = emoji,
                        isDefault = addresses.isEmpty()
                    )
                )
                showAddressDialog = false
            },
            onDismiss = { showAddressDialog = false }
        )
    }

    // ── Logout confirm dialog
    if (showLogoutDialog) {
        LogoutConfirmDialog(
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
fun ProfileTopBar(onBack: () -> Unit) {
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
                text = "My Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
        }
    }
}

// ── Avatar card ───────────────────────────────────────────────────────────────

@Composable
fun ProfileAvatarCard(
    name: String,
    phone: String,
    email: String,
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
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.04f))
                .align(Alignment.TopEnd)
                .offset(x = 16.dp, y = (-16).dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar circle
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(WarmAmber),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.first().uppercase(),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Cream
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Cream
                )
                Text(
                    text = phone,
                    fontSize = 13.sp,
                    color = Cream.copy(alpha = 0.65f)
                )
                Text(
                    text = email,
                    fontSize = 13.sp,
                    color = Cream.copy(alpha = 0.65f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(WarmAmber.copy(alpha = 0.2f))
                        .clickable { onEditClick() }
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "✏️ Edit Profile",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = WarmAmber
                    )
                }
            }
        }
    }
}

// ── Stats row ─────────────────────────────────────────────────────────────────

@Composable
fun ProfileStatsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        listOf(
            Triple("8",     "Orders",    Color(0xFFE3F2FD)),
            Triple("4.8 ⭐", "Avg Rating", Color(0xFFFFF8E1)),
            Triple("Rs.8,840", "Spent",   Color(0xFFE8F5E9)),
        ).forEach { (value, label, bg) ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(bg)
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = value,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBrown,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = label,
                    fontSize = 10.sp,
                    color = DeepBrown.copy(alpha = 0.5f)
                )
            }
        }
    }
}

// ── Section title ─────────────────────────────────────────────────────────────

@Composable
fun ProfileSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = DeepBrown,
        modifier = Modifier.padding(
            horizontal = 20.dp,
            vertical = 10.dp
        )
    )
}

// ── Address section ───────────────────────────────────────────────────────────

@Composable
fun AddressSection(
    addresses: List<SavedAddress>,
    expanded: Boolean,
    onToggle: () -> Unit,
    onAddClick: () -> Unit,
    onSetDefault: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
    ) {
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${addresses.size} saved addresses",
                fontSize = 14.sp,
                color = DeepBrown.copy(alpha = 0.6f)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(WarmAmber)
                        .clickable { onAddClick() }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "+ Add",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Cream
                    )
                }
                Text(
                    text = if (expanded) "▲" else "▼",
                    fontSize = 12.sp,
                    color = DeepBrown.copy(alpha = 0.3f)
                )
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                HorizontalDivider(color = DeepBrown.copy(alpha = 0.06f))
                addresses.forEachIndexed { index, address ->
                    AddressRow2(
                        address = address,
                        onSetDefault = { onSetDefault(address.id) },
                        onDelete = { onDelete(address.id) }
                    )
                    if (index < addresses.size - 1) {
                        HorizontalDivider(
                            color = DeepBrown.copy(alpha = 0.05f),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddressRow2(
    address: SavedAddress,
    onSetDefault: () -> Unit,
    onDelete: () -> Unit
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
                .background(
                    if (address.isDefault) WarmAmber.copy(alpha = 0.12f)
                    else Cream
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = address.emoji, fontSize = 20.sp)
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = address.label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepBrown
                )
                if (address.isDefault) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(WarmAmber.copy(alpha = 0.15f))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "Default",
                            fontSize = 9.sp,
                            color = WarmAmber,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Text(
                text = address.address,
                fontSize = 11.sp,
                color = DeepBrown.copy(alpha = 0.45f),
                lineHeight = 15.sp
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (!address.isDefault) {
                Text(
                    text = "Set default",
                    fontSize = 10.sp,
                    color = WarmAmber,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onSetDefault() }
                )
            }
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(Color(0xFFFFEBEB))
                    .clickable { onDelete() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

// ── Profile card wrapper ──────────────────────────────────────────────────────

@Composable
fun ProfileCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White),
        content = content
    )
}

@Composable
fun ProfileDivider() {
    HorizontalDivider(
        color = DeepBrown.copy(alpha = 0.05f),
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

// ── Toggle setting row ────────────────────────────────────────────────────────

@Composable
fun ToggleSettingRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    label: String,
    subtitle: String?,
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
            subtitle?.let {
                Text(
                    text = it,
                    fontSize = 11.sp,
                    color = DeepBrown.copy(alpha = 0.45f)
                )
            }
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

// ── Arrow setting row ─────────────────────────────────────────────────────────

@Composable
fun ArrowSettingRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    label: String,
    subtitle: String?
) {
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
            subtitle?.let {
                Text(
                    text = it,
                    fontSize = 11.sp,
                    color = DeepBrown.copy(alpha = 0.45f)
                )
            }
        }
        Text(
            text = "›",
            fontSize = 22.sp,
            color = DeepBrown.copy(alpha = 0.2f)
        )
    }
}

// ── Edit profile dialog ───────────────────────────────────────────────────────

@Composable
fun EditProfileDialog(
    name: String,
    email: String,
    onSave: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var newName by remember { mutableStateOf(name) }
    var newEmail by remember { mutableStateOf(email) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Edit Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )

            // Avatar preview
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(WarmAmber)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = newName.firstOrNull()?.uppercase() ?: "A",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Cream
                )
            }

            AddDialogField(
                label = "Full Name",
                value = newName,
                onChange = { newName = it }
            )
            AddDialogField(
                label = "Email",
                value = newEmail,
                onChange = { newEmail = it }
            )

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
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        if (newName.isNotEmpty()) onSave(newName, newEmail)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WarmAmber,
                        contentColor = Cream
                    ),
                    enabled = newName.isNotEmpty()
                ) {
                    Text("Save", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// ── Add address dialog ────────────────────────────────────────────────────────

@Composable
fun AddAddressDialog(
    onAdd: (String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var label by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("🏠") }

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
                text = "Add Address",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )

            // Emoji picker
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("🏠", "🏢", "❤️", "🏫", "🏥", "🛒").forEach { e ->
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selectedEmoji == e) WarmAmber.copy(alpha = 0.15f)
                                else Cream
                            )
                            .border(
                                1.dp,
                                if (selectedEmoji == e) WarmAmber
                                else Color.Transparent,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedEmoji = e },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = e, fontSize = 22.sp)
                    }
                }
            }

            AddDialogField("Label (e.g. Home)", label, { label = it })
            AddDialogField("Full Address", address, { address = it })

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
                        if (label.isNotEmpty() && address.isNotEmpty())
                            onAdd(label, address, selectedEmoji)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WarmAmber,
                        contentColor = Cream
                    ),
                    enabled = label.isNotEmpty() && address.isNotEmpty()
                ) {
                    Text("Add", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// ── Logout confirm dialog ─────────────────────────────────────────────────────

@Composable
fun LogoutConfirmDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
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
                text = "You'll need to log in again\nto access your account.",
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