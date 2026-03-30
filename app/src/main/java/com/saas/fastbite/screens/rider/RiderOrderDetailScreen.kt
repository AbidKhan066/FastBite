package com.saas.fastbite.screens.rider

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.saas.fastbite.ui.theme.Cream
import com.saas.fastbite.ui.theme.DeepBrown
import com.saas.fastbite.ui.theme.WarmAmber

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun RiderOrderDetailScreen(
    onBack: () -> Unit = {},
    onChatWithCustomer: () -> Unit = {},
    onChatWithRestaurant: () -> Unit = {}
) {
    var currentStatus by remember { mutableStateOf(RiderOrderStatus.ASSIGNED) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showDeliveredDialog by remember { mutableStateOf(false) }

    val nextStatusLabel = when (currentStatus) {
        RiderOrderStatus.ASSIGNED   -> "Mark as Picked Up 🛵"
        RiderOrderStatus.PICKED_UP  -> "Mark On the Way 🏍️"
        RiderOrderStatus.ON_THE_WAY -> "Mark as Delivered ✅"
        RiderOrderStatus.DELIVERED  -> "Order Delivered 🎉"
    }

    val statusColor = when (currentStatus) {
        RiderOrderStatus.ASSIGNED   -> Color(0xFF1565C0)
        RiderOrderStatus.PICKED_UP  -> WarmAmber
        RiderOrderStatus.ON_THE_WAY -> Color(0xFF6A1B9A)
        RiderOrderStatus.DELIVERED  -> Color(0xFF2E7D32)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 110.dp)
        ) {
            // ── Top bar
            item { RiderOrderTopBar(onBack = onBack, orderId = "FB041") }

            // ── Status hero
            item { RiderStatusHero(currentStatus = currentStatus) }

            // ── Map section
            item { RiderMapSection(currentStatus = currentStatus) }

            // ── Customer info
            item {
                ContactCard(
                    emoji = "👤",
                    title = "Ahmed Khan",
                    subtitle = "Block 5, Gulshan-e-Iqbal, Karachi",
                    tag = "Customer",
                    tagColor = Color(0xFF1565C0),
                    onCallClick = {},
                    onChatClick = onChatWithCustomer
                )
            }

            // ── Restaurant info
            item {
                ContactCard(
                    emoji = "🍔",
                    title = "Burger Lab",
                    subtitle = "Main Shahrae Faisal, Karachi",
                    tag = "Restaurant",
                    tagColor = WarmAmber,
                    onCallClick = {},
                    onChatClick = onChatWithRestaurant
                )
            }

            // ── Order items
            item { RiderOrderItemsCard() }

            // ── Payment info
            item { RiderPaymentCard() }

            // ── Tracking timeline
            item { RiderTrackingTimeline(currentStatus = currentStatus) }
        }

        // ── Bottom status button
        if (currentStatus != RiderOrderStatus.DELIVERED) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(statusColor)
                    .clickable { showConfirmDialog = true }
                    .padding(horizontal = 24.dp, vertical = 18.dp)
            ) {
                Text(
                    text = nextStatusLabel,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF2E7D32))
                    .padding(horizontal = 24.dp, vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✅ Order Delivered",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }

    // ── Status confirm dialog
    if (showConfirmDialog) {
        StatusUpdateDialog(
            currentStatus = currentStatus,
            nextLabel = nextStatusLabel,
            onConfirm = {
                val next = when (currentStatus) {
                    RiderOrderStatus.ASSIGNED   -> RiderOrderStatus.PICKED_UP
                    RiderOrderStatus.PICKED_UP  -> RiderOrderStatus.ON_THE_WAY
                    RiderOrderStatus.ON_THE_WAY -> RiderOrderStatus.DELIVERED
                    RiderOrderStatus.DELIVERED  -> RiderOrderStatus.DELIVERED
                }
                currentStatus = next
                showConfirmDialog = false
                if (next == RiderOrderStatus.DELIVERED) {
                    showDeliveredDialog = true
                }
            },
            onDismiss = { showConfirmDialog = false }
        )
    }

    // ── Delivered celebration dialog
    if (showDeliveredDialog) {
        DeliveredCelebrationDialog(
            onDismiss = {
                showDeliveredDialog = false
                onBack()
            }
        )
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
fun RiderOrderTopBar(onBack: () -> Unit, orderId: String) {
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
                text = "Order Detail",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
        }
        Text(
            text = "#$orderId",
            fontSize = 14.sp,
            color = WarmAmber,
            fontWeight = FontWeight.Bold
        )
    }
}

// ── Status hero ───────────────────────────────────────────────────────────────

@Composable
fun RiderStatusHero(currentStatus: RiderOrderStatus) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (currentStatus != RiderOrderStatus.DELIVERED) 1.07f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "statusPulse"
    )

    val bgColor = when (currentStatus) {
        RiderOrderStatus.ASSIGNED   -> Color(0xFF1565C0)
        RiderOrderStatus.PICKED_UP  -> DeepBrown
        RiderOrderStatus.ON_THE_WAY -> Color(0xFF4A148C)
        RiderOrderStatus.DELIVERED  -> Color(0xFF2E7D32)
    }

    val emoji = when (currentStatus) {
        RiderOrderStatus.ASSIGNED   -> "📋"
        RiderOrderStatus.PICKED_UP  -> "🛵"
        RiderOrderStatus.ON_THE_WAY -> "🏍️"
        RiderOrderStatus.DELIVERED  -> "🎉"
    }

    val label = when (currentStatus) {
        RiderOrderStatus.ASSIGNED   -> "Order Assigned"
        RiderOrderStatus.PICKED_UP  -> "Order Picked Up"
        RiderOrderStatus.ON_THE_WAY -> "On the Way"
        RiderOrderStatus.DELIVERED  -> "Delivered!"
    }

    val subtitle = when (currentStatus) {
        RiderOrderStatus.ASSIGNED   -> "Head to restaurant for pickup"
        RiderOrderStatus.PICKED_UP  -> "Heading to the customer"
        RiderOrderStatus.ON_THE_WAY -> "Almost there, keep going!"
        RiderOrderStatus.DELIVERED  -> "Great work! Order complete"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(bgColor)
            .padding(22.dp),
        contentAlignment = Alignment.Center
    ) {
        // Decorative circles
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.05f))
                .align(Alignment.TopEnd)
                .offset(x = 20.dp, y = (-20).dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = emoji,
                fontSize = 44.sp,
                modifier = Modifier.scale(pulse)
            )
            Text(
                text = label,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            // ETA chip
            if (currentStatus != RiderOrderStatus.DELIVERED) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⏱", fontSize = 13.sp)
                    Text(
                        text = "ETA 12 min",
                        fontSize = 13.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(14.dp)
                            .background(Color.White.copy(alpha = 0.3f))
                    )
                    Text(text = "📍", fontSize = 13.sp)
                    Text(
                        text = "2.4 km away",
                        fontSize = 13.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// ── Map section ───────────────────────────────────────────────────────────────

@Composable
fun RiderMapSection(currentStatus: RiderOrderStatus) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFE8F5E9))
            .border(
                1.dp,
                DeepBrown.copy(alpha = 0.07f),
                RoundedCornerShape(20.dp)
            )
    ) {
        // Fake map grid
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(5) {
                HorizontalDivider(
                    color = Color(0xFF4CAF50).copy(alpha = 0.12f),
                    thickness = 1.dp
                )
            }
        }
        repeat(4) { i ->
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(Color(0xFF4CAF50).copy(alpha = 0.12f))
                    .offset(x = (80 * (i + 1)).dp)
            )
        }

        // Route line
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(WarmAmber.copy(alpha = 0.6f))
                .align(Alignment.Center)
        )

        // Restaurant pin
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-80).dp, y = (-10).dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(WarmAmber),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🍔", fontSize = 18.sp)
            }
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(8.dp)
                    .background(WarmAmber)
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(WarmAmber)
            )
        }

        // Rider pin (animated)
        val infiniteTransition = rememberInfiniteTransition(label = "riderPin")
        val riderY by infiniteTransition.animateFloat(
            initialValue = -2f,
            targetValue = 2f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, easing = EaseInOut),
                repeatMode = RepeatMode.Reverse
            ),
            label = "riderPinY"
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = riderY.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(DeepBrown),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🏍️", fontSize = 18.sp)
            }
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(8.dp)
                    .background(DeepBrown)
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(DeepBrown)
            )
        }

        // Customer pin
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = 80.dp, y = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1565C0)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🏠", fontSize = 18.sp)
            }
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(8.dp)
                    .background(Color(0xFF1565C0))
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1565C0))
            )
        }

        // Legend
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.9f))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendDot(color = WarmAmber, label = "Restaurant")
            LegendDot(color = DeepBrown, label = "You")
            LegendDot(color = Color(0xFF1565C0), label = "Customer")
        }

        // Full map label
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.9f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Live Map (coming soon)",
                fontSize = 9.sp,
                color = DeepBrown.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun LegendDot(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            fontSize = 9.sp,
            color = DeepBrown.copy(alpha = 0.6f)
        )
    }
}

// ── Contact card ──────────────────────────────────────────────────────────────

@Composable
fun ContactCard(
    emoji: String,
    title: String,
    subtitle: String,
    tag: String,
    tagColor: Color,
    onCallClick: () -> Unit,
    onChatClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(tagColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 24.sp)
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBrown
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(tagColor.copy(alpha = 0.1f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = tag,
                        fontSize = 10.sp,
                        color = tagColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = DeepBrown.copy(alpha = 0.5f),
                lineHeight = 17.sp
            )
        }

        // Call button
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9))
                .clickable { onCallClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Call",
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(17.dp)
            )
        }

        // Chat button
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(WarmAmber.copy(alpha = 0.12f))
                .clickable { onChatClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ChatBubble,
                contentDescription = "Chat",
                tint = WarmAmber,
                modifier = Modifier.size(17.dp)
            )
        }
    }
}

// ── Order items card ──────────────────────────────────────────────────────────

@Composable
fun RiderOrderItemsCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "🧾 Order Items",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown
        )

        listOf(
            Triple("🍔", "Classic Beef Burger × 2", "Rs.700"),
            Triple("🍟", "Loaded Fries × 1",        "Rs.250"),
            Triple("🥤", "Chocolate Shake × 1",     "Rs.220"),
        ).forEach { (emoji, name, price) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = emoji, fontSize = 16.sp)
                    Text(
                        text = name,
                        fontSize = 13.sp,
                        color = DeepBrown.copy(alpha = 0.7f)
                    )
                }
                Text(
                    text = price,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepBrown
                )
            }
        }

        HorizontalDivider(color = DeepBrown.copy(alpha = 0.07f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
            Text(
                text = "Rs.1,170",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = WarmAmber
            )
        }
    }
}

// ── Payment card ──────────────────────────────────────────────────────────────

@Composable
fun RiderPaymentCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "💵", fontSize = 20.sp)
            }
            Column {
                Text(
                    text = "Payment Method",
                    fontSize = 12.sp,
                    color = DeepBrown.copy(alpha = 0.45f)
                )
                Text(
                    text = "Cash on Delivery",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBrown
                )
            }
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE8F5E9))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(
                text = "Collect Cash",
                fontSize = 12.sp,
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ── Tracking timeline ─────────────────────────────────────────────────────────

@Composable
fun RiderTrackingTimeline(currentStatus: RiderOrderStatus) {
    val steps = listOf(
        Triple(RiderOrderStatus.ASSIGNED,   "📋", "Order Assigned"),
        Triple(RiderOrderStatus.PICKED_UP,  "🛵", "Picked Up"),
        Triple(RiderOrderStatus.ON_THE_WAY, "🏍️", "On the Way"),
        Triple(RiderOrderStatus.DELIVERED,  "✅", "Delivered"),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Text(
            text = "Progress",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown,
            modifier = Modifier.padding(bottom = 14.dp)
        )

        steps.forEachIndexed { index, (status, emoji, label) ->
            val isDone = status <= currentStatus
            val isActive = status == currentStatus
            val isLast = index == steps.size - 1

            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Indicator
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isActive -> WarmAmber
                                    isDone   -> Color(0xFF4CAF50)
                                    else     -> DeepBrown.copy(alpha = 0.07f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isDone && !isActive) "✓" else emoji,
                            fontSize = 13.sp,
                            color = if (isDone || isActive) Color.White
                            else DeepBrown.copy(alpha = 0.3f)
                        )
                    }
                    if (!isLast) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(30.dp)
                                .background(
                                    if (isDone) Color(0xFF4CAF50).copy(alpha = 0.4f)
                                    else DeepBrown.copy(alpha = 0.07f)
                                )
                        )
                    }
                }

                // Label
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            top = 6.dp,
                            bottom = if (!isLast) 20.dp else 0.dp
                        )
                ) {
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = if (isActive) FontWeight.Bold
                        else FontWeight.Medium,
                        color = when {
                            isActive -> DeepBrown
                            isDone   -> DeepBrown.copy(alpha = 0.6f)
                            else     -> DeepBrown.copy(alpha = 0.25f)
                        }
                    )
                }
            }
        }
    }
}

// ── Status update dialog ──────────────────────────────────────────────────────

@Composable
fun StatusUpdateDialog(
    currentStatus: RiderOrderStatus,
    nextLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val emoji = when (currentStatus) {
        RiderOrderStatus.ASSIGNED   -> "🛵"
        RiderOrderStatus.PICKED_UP  -> "🏍️"
        RiderOrderStatus.ON_THE_WAY -> "✅"
        RiderOrderStatus.DELIVERED  -> "🎉"
    }

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
            Text(text = emoji, fontSize = 44.sp)
            Text(
                text = "Update Status?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
            Text(
                text = nextLabel,
                fontSize = 14.sp,
                color = DeepBrown.copy(alpha = 0.55f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WarmAmber,
                    contentColor = Cream
                )
            ) {
                Text(
                    text = "Confirm",
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

// ── Delivered celebration dialog ──────────────────────────────────────────────

@Composable
fun DeliveredCelebrationDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "🎉", fontSize = 56.sp)
            Text(
                text = "Order Delivered!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
            Text(
                text = "Great job! You've successfully\ndelivered Order #FB041.",
                fontSize = 13.sp,
                color = DeepBrown.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            // Earnings chip
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFE8F5E9))
                    .border(
                        1.dp,
                        Color(0xFF4CAF50).copy(alpha = 0.3f),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "+ Rs.160 earned",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        text = "Delivery earnings added",
                        fontSize = 11.sp,
                        color = Color(0xFF2E7D32).copy(alpha = 0.6f)
                    )
                }
            }

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WarmAmber,
                    contentColor = Cream
                )
            ) {
                Text(
                    text = "Back to Dashboard",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}