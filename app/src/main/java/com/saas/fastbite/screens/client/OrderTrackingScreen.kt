package com.saas.fastbite.screens.client

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubble
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
import com.saas.fastbite.ui.theme.Cream
import com.saas.fastbite.ui.theme.DeepBrown
import com.saas.fastbite.ui.theme.WarmAmber
import kotlinx.coroutines.delay

// ── Data models ───────────────────────────────────────────────────────────────

enum class OrderStatus {
    PLACED, CONFIRMED, PREPARING, PICKED_UP, ON_THE_WAY, DELIVERED
}

data class TrackingStep(
    val status: OrderStatus,
    val label: String,
    val description: String,
    val emoji: String,
    val estimatedTime: String
)

val trackingSteps = listOf(
    TrackingStep(OrderStatus.PLACED,     "Order Placed",      "Your order has been sent",         "📋", "Just now"),
    TrackingStep(OrderStatus.CONFIRMED,  "Order Confirmed",   "Restaurant accepted your order",   "✅", "2 min ago"),
    TrackingStep(OrderStatus.PREPARING,  "Preparing",         "Chef is cooking your food",        "👨‍🍳", "5 min ago"),
    TrackingStep(OrderStatus.PICKED_UP,  "Picked Up",         "Rider has your order",             "🏍️", "~5 min"),
    TrackingStep(OrderStatus.ON_THE_WAY, "On the Way",        "Rider is heading to you",          "🛵", "~10 min"),
    TrackingStep(OrderStatus.DELIVERED,  "Delivered",         "Enjoy your meal!",                 "🎉", ""),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun OrderTrackingScreen(
    onBack: () -> Unit = {},
    onChatClick: () -> Unit = {}
) {
    var currentStatus by remember { mutableStateOf(OrderStatus.CONFIRMED) }
    var showRatingSheet by remember { mutableStateOf(false) }

    // Simulate order progression for demo
    LaunchedEffect(Unit) {
        delay(2000); currentStatus = OrderStatus.PREPARING
        delay(3000); currentStatus = OrderStatus.PICKED_UP
        delay(3000); currentStatus = OrderStatus.ON_THE_WAY
        delay(4000); currentStatus = OrderStatus.DELIVERED
        delay(500);  showRatingSheet = true
    }

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
            item { TrackingTopBar(onBack = onBack) }

            // ── Hero status card
            item { HeroStatusCard(currentStatus = currentStatus) }

            // ── Map placeholder
            item { MapPlaceholder() }

            // ── Rider info
            if (currentStatus >= OrderStatus.PICKED_UP) {
                item { RiderInfoCard(onChatClick = onChatClick) }
            }

            // ── Tracking steps
            item { TrackingStepsCard(currentStatus = currentStatus) }

            // ── Order summary
            item { OrderSummaryCard() }
        }
    }

    // ── Rating bottom sheet
    if (showRatingSheet) {
        RatingBottomSheet(
            onSubmit = { showRatingSheet = false },
            onDismiss = { showRatingSheet = false }
        )
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
fun TrackingTopBar(onBack: () -> Unit) {
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
                text = "Track Order",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
        }
        Text(
            text = "#FB2341",
            fontSize = 13.sp,
            color = WarmAmber,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ── Hero status card ──────────────────────────────────────────────────────────

@Composable
fun HeroStatusCard(currentStatus: OrderStatus) {
    val step = trackingSteps.find { it.status == currentStatus } ?: trackingSteps[0]
    val isDelivered = currentStatus == OrderStatus.DELIVERED

    // Pulse animation for active status
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isDelivered) 1f else 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(if (isDelivered) Color(0xFF4CAF50) else WarmAmber)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = step.emoji,
                fontSize = 48.sp,
                modifier = Modifier.scale(pulse)
            )
            Text(
                text = step.label,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = step.description,
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )
            if (step.estimatedTime.isNotEmpty() && !isDelivered) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "⏱ ETA ${step.estimatedTime}",
                        fontSize = 13.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// ── Map placeholder ───────────────────────────────────────────────────────────

@Composable
fun MapPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFE8F5E9))
            .border(
                width = 1.dp,
                color = DeepBrown.copy(alpha = 0.07f),
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        // Fake map grid lines
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(4) {
                HorizontalDivider(
                    color = Color(0xFF4CAF50).copy(alpha = 0.15f),
                    thickness = 1.dp
                )
            }
        }

        // Fake road
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(40.dp)
                .background(Color(0xFFBDBDBD).copy(alpha = 0.4f))
        )

        // Rider pin
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(x = (-30).dp, y = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(WarmAmber),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🏍️", fontSize = 18.sp)
            }
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(6.dp)
                    .background(WarmAmber)
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(WarmAmber)
            )
        }

        // Destination pin
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(x = 40.dp, y = (-20).dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(DeepBrown),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🏠", fontSize = 18.sp)
            }
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(6.dp)
                    .background(DeepBrown)
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(DeepBrown)
            )
        }

        // Map label
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
                fontSize = 10.sp,
                color = DeepBrown.copy(alpha = 0.5f)
            )
        }
    }
}

// ── Rider info card ───────────────────────────────────────────────────────────

@Composable
fun RiderInfoCard(onChatClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(WarmAmber.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "👨", fontSize = 28.sp)
        }

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Ali Raza",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
            Text(
                text = "Your delivery rider",
                fontSize = 12.sp,
                color = DeepBrown.copy(alpha = 0.5f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "⭐", fontSize = 11.sp)
                Text(
                    text = "4.9 · Honda CB150",
                    fontSize = 11.sp,
                    color = DeepBrown.copy(alpha = 0.5f)
                )
            }
        }

        // Action buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ActionCircleButton(
                icon = Icons.Default.Call,
                background = Color(0xFFE8F5E9),
                tint = Color(0xFF2E7D32),
                onClick = {}
            )
            ActionCircleButton(
                icon = Icons.Default.ChatBubble,
                background = WarmAmber.copy(alpha = 0.12f),
                tint = WarmAmber,
                onClick = onChatClick
            )
        }
    }
}

@Composable
fun ActionCircleButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    background: Color,
    tint: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(background)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(18.dp)
        )
    }
}

// ── Tracking steps ────────────────────────────────────────────────────────────

@Composable
fun TrackingStepsCard(currentStatus: OrderStatus) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Text(
            text = "Order Progress",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        trackingSteps.forEachIndexed { index, step ->
            val isDone = step.status <= currentStatus
            val isActive = step.status == currentStatus
            val isLast = index == trackingSteps.size - 1

            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Step indicator column
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(32.dp)
                ) {
                    // Circle
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isActive -> WarmAmber
                                    isDone -> Color(0xFF4CAF50)
                                    else -> DeepBrown.copy(alpha = 0.08f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isDone && !isActive) "✓" else step.emoji,
                            fontSize = if (isDone && !isActive) 14.sp else 14.sp,
                            color = if (isDone || isActive) Color.White
                            else DeepBrown.copy(alpha = 0.3f)
                        )
                    }

                    // Connector line
                    if (!isLast) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(32.dp)
                                .background(
                                    if (isDone) Color(0xFF4CAF50).copy(alpha = 0.4f)
                                    else DeepBrown.copy(alpha = 0.08f)
                                )
                        )
                    }
                }

                // Text info
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 6.dp, bottom = if (!isLast) 20.dp else 0.dp)
                ) {
                    Text(
                        text = step.label,
                        fontSize = 14.sp,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                        color = when {
                            isActive -> DeepBrown
                            isDone -> DeepBrown.copy(alpha = 0.7f)
                            else -> DeepBrown.copy(alpha = 0.3f)
                        }
                    )
                    Text(
                        text = if (isDone) step.description else "Waiting...",
                        fontSize = 12.sp,
                        color = DeepBrown.copy(alpha = if (isDone) 0.5f else 0.25f)
                    )
                }

                // Time
                if (step.estimatedTime.isNotEmpty()) {
                    Text(
                        text = step.estimatedTime,
                        fontSize = 11.sp,
                        color = if (isActive) WarmAmber else DeepBrown.copy(alpha = 0.3f),
                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
        }
    }
}

// ── Order summary ─────────────────────────────────────────────────────────────

@Composable
fun OrderSummaryCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Order Summary",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown
        )

        listOf(
            Triple("🍔", "Classic Beef Burger × 2", "Rs.700"),
            Triple("🍟", "Loaded Fries × 1", "Rs.250"),
            Triple("🎉", "Burger Combo × 1", "Rs.650"),
            Triple("🥤", "Chocolate Shake × 1", "Rs.220"),
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
                text = "Total Paid",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
            Text(
                text = "Rs.1,870",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = WarmAmber
            )
        }
    }
}

// ── Rating bottom sheet ───────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingBottomSheet(
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    var rating by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "🎉", fontSize = 44.sp)

            Text(
                text = "Order Delivered!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )

            Text(
                text = "How was your experience?",
                fontSize = 14.sp,
                color = DeepBrown.copy(alpha = 0.5f)
            )

            // Star rating
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(5) { index ->
                    Text(
                        text = if (index < rating) "⭐" else "☆",
                        fontSize = 36.sp,
                        color = if (index < rating) WarmAmber else DeepBrown.copy(alpha = 0.2f),
                        modifier = Modifier.clickable { rating = index + 1 }
                    )
                }
            }

            // Comment
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Leave a comment (optional)",
                        color = DeepBrown.copy(alpha = 0.3f),
                        fontSize = 13.sp
                    )
                },
                minLines = 2,
                maxLines = 3,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = WarmAmber,
                    unfocusedBorderColor = DeepBrown.copy(alpha = 0.12f),
                    focusedTextColor = DeepBrown,
                    unfocusedTextColor = DeepBrown,
                    cursorColor = WarmAmber,
                    focusedContainerColor = Cream,
                    unfocusedContainerColor = Cream
                )
            )

            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                enabled = rating > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = WarmAmber,
                    contentColor = Cream,
                    disabledContainerColor = WarmAmber.copy(alpha = 0.3f),
                    disabledContentColor = Cream.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = "Submit Review",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            TextButton(onClick = onDismiss) {
                Text(
                    text = "Skip for now",
                    color = DeepBrown.copy(alpha = 0.35f),
                    fontSize = 13.sp
                )
            }
        }
    }
}