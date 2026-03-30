package com.saas.fastbite.screens.rider

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saas.fastbite.ui.theme.Cream
import com.saas.fastbite.ui.theme.DeepBrown
import com.saas.fastbite.ui.theme.WarmAmber

// ── Data models ───────────────────────────────────────────────────────────────

enum class RiderOrderStatus {
    ASSIGNED, PICKED_UP, ON_THE_WAY, DELIVERED
}

data class RiderOrder(
    val id: String,
    val customerName: String,
    val customerAddress: String,
    val restaurantName: String,
    val restaurantAddress: String,
    val items: String,
    val total: Int,
    val distance: String,
    val estimatedTime: String,
    val status: RiderOrderStatus
)

data class RiderStat(
    val label: String,
    val value: String,
    val emoji: String,
    val background: Color
)

val assignedOrders = listOf(
    RiderOrder(
        id = "FB041",
        customerName = "Ahmed Khan",
        customerAddress = "Block 5, Gulshan-e-Iqbal",
        restaurantName = "Burger Lab",
        restaurantAddress = "Main Shahrae Faisal",
        items = "2x Burger, 1x Fries, 1x Shake",
        total = 1170,
        distance = "2.4 km",
        estimatedTime = "12 min",
        status = RiderOrderStatus.ASSIGNED
    ),
    RiderOrder(
        id = "FB042",
        customerName = "Sara Malik",
        customerAddress = "North Nazimabad, Block H",
        restaurantName = "Burger Lab",
        restaurantAddress = "Main Shahrae Faisal",
        items = "1x Family Feast",
        total = 2200,
        distance = "3.8 km",
        estimatedTime = "18 min",
        status = RiderOrderStatus.PICKED_UP
    ),
)

val riderStats = listOf(
    RiderStat("Today",       "6",      "📦", Color(0xFFE3F2FD)),
    RiderStat("This Week",   "38",     "🗓️", Color(0xFFFFF3E0)),
    RiderStat("Earnings",    "Rs.960", "💰", Color(0xFFE8F5E9)),
    RiderStat("Rating",      "4.9 ⭐", "⭐", Color(0xFFFFF8E1)),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun RiderDashboardScreen(
    onOrderClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onChatClick: () -> Unit = {}
) {
    var isOnline by remember { mutableStateOf(true) }
    val orders = remember { mutableStateListOf(*assignedOrders.toTypedArray()) }

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
                RiderTopBar(
                    isOnline = isOnline,
                    onToggle = { isOnline = !isOnline }
                )
            }

            // ── Online status hero
            item { OnlineStatusHero(isOnline = isOnline) }

            // ── Stats row
            item { RiderStatsRow() }

            // ── Active orders header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📋 Assigned Orders",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepBrown
                    )
                    Text(
                        text = "History →",
                        fontSize = 13.sp,
                        color = WarmAmber,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onHistoryClick() }
                    )
                }
            }

            // ── Orders
            if (orders.isEmpty()) {
                item { NoOrdersPlaceholder() }
            } else {
                items(orders, key = { it.id }) { order ->
                    RiderOrderCard(
                        order = order,
                        onStatusUpdate = { updatedOrder ->
                            val index = orders.indexOfFirst { it.id == updatedOrder.id }
                            if (index != -1) {
                                if (updatedOrder.status == RiderOrderStatus.DELIVERED) {
                                    orders.removeAt(index)
                                } else {
                                    orders[index] = updatedOrder
                                }
                            }
                        },
                        onChatClick = onChatClick,
                        onClick = onOrderClick
                    )
                }
            }

            // ── Quick actions
            item { RiderQuickActions(onHistoryClick = onHistoryClick) }
        }
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
fun RiderTopBar(isOnline: Boolean, onToggle: () -> Unit) {
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
                text = "Good Morning 👋",
                fontSize = 13.sp,
                color = DeepBrown.copy(alpha = 0.5f)
            )
            Text(
                text = "Ali Raza",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Online toggle
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isOnline) Color(0xFF4CAF50).copy(alpha = 0.12f)
                        else Color(0xFFE53935).copy(alpha = 0.1f)
                    )
                    .border(
                        1.dp,
                        if (isOnline) Color(0xFF4CAF50).copy(alpha = 0.4f)
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
                            if (isOnline) Color(0xFF4CAF50)
                            else Color(0xFFE53935)
                        )
                )
                Text(
                    text = if (isOnline) "Online" else "Offline",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isOnline) Color(0xFF2E7D32)
                    else Color(0xFFC62828)
                )
            }

            // Avatar
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(WarmAmber),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "A",
                    fontSize = 16.sp,
                    color = Cream,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ── Online status hero ────────────────────────────────────────────────────────

@Composable
fun OnlineStatusHero(isOnline: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isOnline) 1.06f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heroPulse"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(if (isOnline) Color(0xFF2E7D32) else DeepBrown)
            .padding(24.dp),
        contentAlignment = Alignment.Center
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
                .size(70.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.04f))
                .align(Alignment.BottomStart)
                .offset(x = (-20).dp, y = 20.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (isOnline) "🏍️" else "😴",
                fontSize = 44.sp,
                modifier = Modifier.scale(pulse)
            )
            Text(
                text = if (isOnline) "You're Online" else "You're Offline",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = if (isOnline)
                    "Ready to receive orders"
                else
                    "Go online to start receiving orders",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// ── Stats row ─────────────────────────────────────────────────────────────────

@Composable
fun RiderStatsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        riderStats.forEach { stat ->
            RiderStatCard(stat = stat, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun RiderStatCard(stat: RiderStat, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(stat.background)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = stat.emoji, fontSize = 18.sp)
        Text(
            text = stat.value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown,
            textAlign = TextAlign.Center
        )
        Text(
            text = stat.label,
            fontSize = 10.sp,
            color = DeepBrown.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
    }
}

// ── Rider order card ──────────────────────────────────────────────────────────

@Composable
fun RiderOrderCard(
    order: RiderOrder,
    onStatusUpdate: (RiderOrder) -> Unit,
    onChatClick: () -> Unit,
    onClick: () -> Unit
) {
    val statusColor = when (order.status) {
        RiderOrderStatus.ASSIGNED   -> Color(0xFF1565C0)
        RiderOrderStatus.PICKED_UP  -> WarmAmber
        RiderOrderStatus.ON_THE_WAY -> Color(0xFF6A1B9A)
        RiderOrderStatus.DELIVERED  -> Color(0xFF2E7D32)
    }

    val statusLabel = when (order.status) {
        RiderOrderStatus.ASSIGNED   -> "Assigned"
        RiderOrderStatus.PICKED_UP  -> "Picked Up"
        RiderOrderStatus.ON_THE_WAY -> "On the Way"
        RiderOrderStatus.DELIVERED  -> "Delivered"
    }

    val nextStatusLabel = when (order.status) {
        RiderOrderStatus.ASSIGNED   -> "Mark as Picked Up"
        RiderOrderStatus.PICKED_UP  -> "Mark On the Way"
        RiderOrderStatus.ON_THE_WAY -> "Mark as Delivered"
        RiderOrderStatus.DELIVERED  -> "Completed"
    }

    val nextStatus = when (order.status) {
        RiderOrderStatus.ASSIGNED   -> RiderOrderStatus.PICKED_UP
        RiderOrderStatus.PICKED_UP  -> RiderOrderStatus.ON_THE_WAY
        RiderOrderStatus.ON_THE_WAY -> RiderOrderStatus.DELIVERED
        RiderOrderStatus.DELIVERED  -> RiderOrderStatus.DELIVERED
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ── Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Order #${order.id}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBrown
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = statusLabel,
                        fontSize = 11.sp,
                        color = statusColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Text(
                text = "Rs.${order.total}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = WarmAmber
            )
        }

        // ── Route card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Cream)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Pickup
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(WarmAmber)
                    )
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(28.dp)
                            .background(DeepBrown.copy(alpha = 0.15f))
                    )
                }
                Column {
                    Text(
                        text = "Pickup",
                        fontSize = 10.sp,
                        color = DeepBrown.copy(alpha = 0.4f),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = order.restaurantName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DeepBrown
                    )
                    Text(
                        text = order.restaurantAddress,
                        fontSize = 11.sp,
                        color = DeepBrown.copy(alpha = 0.5f)
                    )
                }
            }

            // Dropoff
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(DeepBrown)
                )
                Column {
                    Text(
                        text = "Deliver to",
                        fontSize = 10.sp,
                        color = DeepBrown.copy(alpha = 0.4f),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = order.customerName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DeepBrown
                    )
                    Text(
                        text = order.customerAddress,
                        fontSize = 11.sp,
                        color = DeepBrown.copy(alpha = 0.5f)
                    )
                }
            }
        }

        // ── Distance + time chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            InfoChip(emoji = "📍", text = order.distance)
            InfoChip(emoji = "⏱", text = order.estimatedTime)
            InfoChip(
                emoji = "🧾",
                text = "${order.items.split(",").size} items"
            )
        }

        // ── Items preview
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(WarmAmber.copy(alpha = 0.06f))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "🧾", fontSize = 13.sp)
            Text(
                text = order.items,
                fontSize = 12.sp,
                color = DeepBrown.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }

        // ── Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Chat
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(WarmAmber.copy(alpha = 0.1f))
                    .clickable { onChatClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubble,
                    contentDescription = "Chat",
                    tint = WarmAmber,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Map / Navigate
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE3F2FD))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = "Navigate",
                    tint = Color(0xFF1565C0),
                    modifier = Modifier.size(18.dp)
                )
            }

            // Status update
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (order.status == RiderOrderStatus.DELIVERED)
                            Color(0xFF4CAF50)
                        else statusColor
                    )
                    .clickable {
                        if (order.status != RiderOrderStatus.DELIVERED) {
                            onStatusUpdate(order.copy(status = nextStatus))
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = nextStatusLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun InfoChip(emoji: String, text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(DeepBrown.copy(alpha = 0.06f))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = emoji, fontSize = 11.sp)
        Text(
            text = text,
            fontSize = 11.sp,
            color = DeepBrown.copy(alpha = 0.7f),
            fontWeight = FontWeight.Medium
        )
    }
}

// ── No orders placeholder ─────────────────────────────────────────────────────

@Composable
fun NoOrdersPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "🏍️", fontSize = 56.sp)
        Text(
            text = "No orders yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown
        )
        Text(
            text = "Stay online to start\nreceiving orders",
            fontSize = 14.sp,
            color = DeepBrown.copy(alpha = 0.45f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

// ── Quick actions ─────────────────────────────────────────────────────────────

@Composable
fun RiderQuickActions(onHistoryClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Quick Actions",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RiderActionCard(
                emoji = "📜",
                label = "Delivery History",
                background = Color(0xFFE8F5E9),
                onClick = onHistoryClick,
                modifier = Modifier.weight(1f)
            )
            RiderActionCard(
                emoji = "💬",
                label = "Messages",
                background = Color(0xFFFCE4EC),
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            RiderActionCard(
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
fun RiderActionCard(
    emoji: String,
    label: String,
    background: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
}