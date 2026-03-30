package com.saas.fastbite.screens.restaurant

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saas.fastbite.ui.theme.Cream
import com.saas.fastbite.ui.theme.DeepBrown
import com.saas.fastbite.ui.theme.WarmAmber

// ── Data models ───────────────────────────────────────────────────────────────

data class LiveOrder(
    val id: String,
    val customerName: String,
    val items: String,
    val total: Int,
    val status: LiveOrderStatus,
    val timeAgo: String,
    val itemCount: Int
)

enum class LiveOrderStatus {
    NEW, CONFIRMED, PREPARING, READY
}

data class QuickStat(
    val label: String,
    val value: String,
    val emoji: String,
    val color: Color
)

val liveOrders = listOf(
    LiveOrder("FB001", "Ahmed K.", "2x Burger, 1x Fries", 950, LiveOrderStatus.NEW, "1 min ago", 3),
    LiveOrder("FB002", "Sara M.", "1x Combo, 2x Drinks", 1100, LiveOrderStatus.CONFIRMED, "5 min ago", 3),
    LiveOrder("FB003", "Usman R.", "3x Zinger, 1x Shake", 1440, LiveOrderStatus.PREPARING, "12 min ago", 4),
    LiveOrder("FB004", "Hina A.", "1x Family Feast", 2200, LiveOrderStatus.READY, "18 min ago", 4),
)

val quickStats = listOf(
    QuickStat("Today's Orders", "24", "📦", Color(0xFF1565C0)),
    QuickStat("Revenue", "Rs.18,400", "💰", Color(0xFF2E7D32)),
    QuickStat("Tokens Left", "142", "🪙", WarmAmber),
    QuickStat("Avg. Rating", "4.8 ⭐", "⭐", Color(0xFFF57F17)),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun RestaurantDashboardScreen(
    onLiveOrdersClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onTokensClick: () -> Unit = {},
    onAnalyticsClick: () -> Unit = {},
    onChatClick: () -> Unit = {}
) {
    var isOpen by remember { mutableStateOf(true) }

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
            item { RestaurantTopBar(isOpen = isOpen, onToggle = { isOpen = !isOpen }) }

            // ── Token banner
            item { TokenBanner(tokens = 142, onBuyClick = onTokensClick) }

            // ── Quick stats
            item { QuickStatsRow() }

            // ── Quick actions
            item {
                QuickActions(
                    onLiveOrdersClick = onLiveOrdersClick,
                    onMenuClick = onMenuClick,
                    onTokensClick = onTokensClick,
                    onAnalyticsClick = onAnalyticsClick,
                    onChatClick = onChatClick
                )
            }

            // ── Live orders header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔴 Live Orders",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepBrown
                    )
                    Text(
                        text = "See all",
                        fontSize = 13.sp,
                        color = WarmAmber,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onLiveOrdersClick() }
                    )
                }
            }

            // ── Live order cards
            items(liveOrders) { order ->
                LiveOrderCard(
                    order = order,
                    onAccept = {},
                    onAssignRider = {}
                )
            }
        }
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
fun RestaurantTopBar(isOpen: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Welcome back 👋",
                fontSize = 13.sp,
                color = DeepBrown.copy(alpha = 0.5f)
            )
            Text(
                text = "Burger Lab",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Open / Closed toggle
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isOpen) Color(0xFF4CAF50).copy(alpha = 0.12f)
                        else Color(0xFFE53935).copy(alpha = 0.1f)
                    )
                    .border(
                        1.dp,
                        if (isOpen) Color(0xFF4CAF50).copy(alpha = 0.4f)
                        else Color(0xFFE53935).copy(alpha = 0.3f),
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { onToggle() }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(
                            if (isOpen) Color(0xFF4CAF50) else Color(0xFFE53935)
                        )
                )
                Text(
                    text = if (isOpen) "Open" else "Closed",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isOpen) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            }

            // Notification
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = DeepBrown,
                    modifier = Modifier.size(20.dp)
                )
                // Badge
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE53935))
                        .align(Alignment.TopEnd)
                        .offset(x = (-6).dp, y = 6.dp)
                )
            }
        }
    }
}

// ── Token banner ──────────────────────────────────────────────────────────────

@Composable
fun TokenBanner(tokens: Int, onBuyClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(DeepBrown)
            .clickable { onBuyClick() }
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "🪙", fontSize = 18.sp)
                    Text(
                        text = "Token Balance",
                        fontSize = 13.sp,
                        color = Cream.copy(alpha = 0.7f)
                    )
                }
                Text(
                    text = "$tokens Tokens",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = WarmAmber
                )
                Text(
                    text = "Each order deducts 1 token",
                    fontSize = 11.sp,
                    color = Cream.copy(alpha = 0.5f)
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(WarmAmber)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "Buy Tokens",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Cream
                    )
                }
                Text(
                    text = "View history →",
                    fontSize = 11.sp,
                    color = Cream.copy(alpha = 0.4f)
                )
            }
        }
    }
}

// ── Quick stats ───────────────────────────────────────────────────────────────

@Composable
fun QuickStatsRow() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(quickStats) { stat ->
            StatCard(stat = stat)
        }
    }
}

@Composable
fun StatCard(stat: QuickStat) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = stat.emoji, fontSize = 20.sp)
        Text(
            text = stat.value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown
        )
        Text(
            text = stat.label,
            fontSize = 11.sp,
            color = DeepBrown.copy(alpha = 0.5f),
            lineHeight = 15.sp
        )
    }
}

// ── Quick actions ─────────────────────────────────────────────────────────────

@Composable
fun QuickActions(
    onLiveOrdersClick: () -> Unit,
    onMenuClick: () -> Unit,
    onTokensClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onChatClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Quick Actions",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        // Row 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                emoji = "📋",
                label = "Live Orders",
                badge = "4",
                background = Color(0xFFE3F2FD),
                onClick = onLiveOrdersClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                emoji = "🍔",
                label = "Menu",
                background = Color(0xFFFFF3E0),
                onClick = onMenuClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                emoji = "🪙",
                label = "Tokens",
                background = Color(0xFFFFF8E1),
                onClick = onTokensClick,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        // Row 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                emoji = "📊",
                label = "Analytics",
                background = Color(0xFFE8F5E9),
                onClick = onAnalyticsClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                emoji = "💬",
                label = "Chat",
                badge = "3",
                background = Color(0xFFFCE4EC),
                onClick = onChatClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                emoji = "👤",
                label = "Profile",
                background = Color(0xFFF3E5F5),
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun QuickActionCard(
    emoji: String,
    label: String,
    badge: String? = null,
    background: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(background)
                .clickable { onClick() }
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = emoji, fontSize = 26.sp)
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = DeepBrown,
                textAlign = TextAlign.Center
            )
        }

        // Badge
        badge?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 4.dp, end = 4.dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE53935)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = it,
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ── Live order card ───────────────────────────────────────────────────────────

@Composable
fun LiveOrderCard(
    order: LiveOrder,
    onAccept: (String) -> Unit,
    onAssignRider: (String) -> Unit
) {
    val statusColor = when (order.status) {
        LiveOrderStatus.NEW -> Color(0xFFE53935)
        LiveOrderStatus.CONFIRMED -> Color(0xFF1565C0)
        LiveOrderStatus.PREPARING -> WarmAmber
        LiveOrderStatus.READY -> Color(0xFF2E7D32)
    }

    val statusLabel = when (order.status) {
        LiveOrderStatus.NEW -> "New"
        LiveOrderStatus.CONFIRMED -> "Confirmed"
        LiveOrderStatus.PREPARING -> "Preparing"
        LiveOrderStatus.READY -> "Ready"
    }

    val statusEmoji = when (order.status) {
        LiveOrderStatus.NEW -> "🆕"
        LiveOrderStatus.CONFIRMED -> "✅"
        LiveOrderStatus.PREPARING -> "👨‍🍳"
        LiveOrderStatus.READY -> "🎯"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(statusColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = statusEmoji, fontSize = 18.sp)
                }
                Column {
                    Text(
                        text = order.customerName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepBrown
                    )
                    Text(
                        text = "Order #${order.id} · ${order.timeAgo}",
                        fontSize = 11.sp,
                        color = DeepBrown.copy(alpha = 0.45f)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusLabel,
                        fontSize = 11.sp,
                        color = statusColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Rs.${order.total}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBrown
                )
            }
        }

        // Items
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Cream)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "🧾", fontSize = 14.sp)
            Text(
                text = order.items,
                fontSize = 12.sp,
                color = DeepBrown.copy(alpha = 0.65f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${order.itemCount} items",
                fontSize = 11.sp,
                color = DeepBrown.copy(alpha = 0.4f)
            )
        }

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            when (order.status) {
                LiveOrderStatus.NEW -> {
                    OrderActionButton(
                        label = "Accept Order",
                        containerColor = Color(0xFF4CAF50),
                        onClick = { onAccept(order.id) },
                        modifier = Modifier.weight(1f)
                    )
                    OrderActionButton(
                        label = "Assign Rider",
                        containerColor = WarmAmber,
                        onClick = { onAssignRider(order.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
                LiveOrderStatus.CONFIRMED, LiveOrderStatus.PREPARING -> {
                    OrderActionButton(
                        label = "Assign Rider",
                        containerColor = WarmAmber,
                        onClick = { onAssignRider(order.id) },
                        modifier = Modifier.weight(1f)
                    )
                    OrderActionButton(
                        label = "Chat",
                        containerColor = DeepBrown,
                        onClick = {},
                        modifier = Modifier.weight(1f)
                    )
                }
                LiveOrderStatus.READY -> {
                    OrderActionButton(
                        label = "✓ Ready for Pickup",
                        containerColor = Color(0xFF2E7D32),
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun OrderActionButton(
    label: String,
    containerColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(containerColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}