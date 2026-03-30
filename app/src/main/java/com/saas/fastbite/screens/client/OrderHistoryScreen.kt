package com.saas.fastbite.screens.client

import androidx.compose.animation.core.*
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.saas.fastbite.ui.theme.Cream
import com.saas.fastbite.ui.theme.DeepBrown
import com.saas.fastbite.ui.theme.WarmAmber

// ── Data models ───────────────────────────────────────────────────────────────

enum class OrderHistoryStatus { DELIVERED, CANCELLED, PENDING }

data class OrderHistoryItem(
    val id: String,
    val orderId: String,
    val restaurantName: String,
    val restaurantEmoji: String,
    val items: List<String>,
    val total: Int,
    val date: String,
    val time: String,
    val status: OrderHistoryStatus,
    val rating: Int? = null,
    val paymentMethod: String = "COD"
)

val orderHistoryList = listOf(
    OrderHistoryItem(
        "o1", "FB041", "Burger Lab", "🍔",
        listOf("Classic Beef Burger × 2", "Loaded Fries × 1", "Chocolate Shake × 1"),
        1170, "29 Mar 2026", "10:45 AM",
        OrderHistoryStatus.DELIVERED, 5, "COD"
    ),
    OrderHistoryItem(
        "o2", "FB039", "Pizza House", "🍕",
        listOf("Margherita Pizza × 1", "Garlic Bread × 2"),
        980, "29 Mar 2026", "01:10 PM",
        OrderHistoryStatus.DELIVERED, 4, "Online"
    ),
    OrderHistoryItem(
        "o3", "FB037", "Crispy Wings", "🍗",
        listOf("Zinger Stack × 2", "Onion Rings × 1", "Drink × 2"),
        1440, "28 Mar 2026", "07:30 PM",
        OrderHistoryStatus.CANCELLED, null, "COD"
    ),
    OrderHistoryItem(
        "o4", "FB035", "Noodle Bar", "🍜",
        listOf("Noodle Bowl × 2", "Spring Rolls × 1"),
        850, "28 Mar 2026", "12:00 PM",
        OrderHistoryStatus.DELIVERED, 5, "Online"
    ),
    OrderHistoryItem(
        "o5", "FB033", "Burger Lab", "🍔",
        listOf("Burger Combo × 1", "Double Smash × 1"),
        1200, "27 Mar 2026", "03:15 PM",
        OrderHistoryStatus.DELIVERED, null, "COD"
    ),
    OrderHistoryItem(
        "o6", "FB031", "Wrap Republic", "🌮",
        listOf("Chicken Wrap × 2", "Fresh Lemonade × 2"),
        760, "27 Mar 2026", "06:45 PM",
        OrderHistoryStatus.DELIVERED, 4, "Online"
    ),
    OrderHistoryItem(
        "o7", "FB028", "Green Bowl", "🥗",
        listOf("Caesar Salad × 1", "Juice × 1"),
        480, "26 Mar 2026", "01:00 PM",
        OrderHistoryStatus.CANCELLED, null, "COD"
    ),
    OrderHistoryItem(
        "o8", "FB025", "Burger Lab", "🍔",
        listOf("Family Feast × 1"),
        2200, "26 Mar 2026", "08:00 PM",
        OrderHistoryStatus.DELIVERED, 5, "Online"
    ),
)

val historyFilters = listOf("All", "Delivered", "Cancelled")

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun OrderHistoryScreen(
    onBack: () -> Unit = {},
    onReorder: () -> Unit = {},
    onTrack: () -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var expandedId by remember { mutableStateOf<String?>(null) }
    var ratingOrderId by remember { mutableStateOf<String?>(null) }
    var ratings by remember { mutableStateOf(mapOf<String, Int>()) }

    val filteredOrders = orderHistoryList.filter { order ->
        val matchesFilter = when (selectedFilter) {
            "Delivered"  -> order.status == OrderHistoryStatus.DELIVERED
            "Cancelled"  -> order.status == OrderHistoryStatus.CANCELLED
            else         -> true
        }
        val matchesSearch = searchQuery.isEmpty() ||
                order.restaurantName.contains(searchQuery, ignoreCase = true) ||
                order.orderId.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesSearch
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
            item { OrderHistoryTopBar(onBack = onBack) }

            // ── Summary stats
            item { OrderHistorySummaryRow(orders = orderHistoryList) }

            // ── Search
            item {
                OrderHistorySearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
            }

            // ── Filter tabs
            item {
                OrderHistoryFilterRow(
                    selected = selectedFilter,
                    onSelect = { selectedFilter = it },
                    orders = orderHistoryList
                )
            }

            // ── Count
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Orders",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepBrown
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(WarmAmber.copy(alpha = 0.12f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${filteredOrders.size} orders",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = WarmAmber
                        )
                    }
                }
            }

            // ── Orders
            if (filteredOrders.isEmpty()) {
                item { EmptyOrderHistory() }
            } else {
                items(filteredOrders, key = { it.id }) { order ->
                    OrderHistoryCard(
                        order = order,
                        isExpanded = expandedId == order.id,
                        userRating = ratings[order.id] ?: order.rating,
                        onToggle = {
                            expandedId =
                                if (expandedId == order.id) null else order.id
                        },
                        onReorder = onReorder,
                        onTrack = onTrack,
                        onRateClick = { ratingOrderId = order.id }
                    )
                }
            }
        }
    }

    // ── Rating dialog
    ratingOrderId?.let { orderId ->
        val order = orderHistoryList.find { it.id == orderId }
        order?.let {
            RateOrderDialog(
                order = it,
                onSubmit = { rating ->
                    ratings = ratings + (orderId to rating)
                    ratingOrderId = null
                },
                onDismiss = { ratingOrderId = null }
            )
        }
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
fun OrderHistoryTopBar(onBack: () -> Unit) {
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
                text = "Order History",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(WarmAmber.copy(alpha = 0.12f))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text = "${orderHistoryList.size} total",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = WarmAmber
            )
        }
    }
}

// ── Summary row ───────────────────────────────────────────────────────────────

@Composable
fun OrderHistorySummaryRow(orders: List<OrderHistoryItem>) {
    val delivered = orders.count { it.status == OrderHistoryStatus.DELIVERED }
    val cancelled = orders.count { it.status == OrderHistoryStatus.CANCELLED }
    val totalSpent = orders
        .filter { it.status == OrderHistoryStatus.DELIVERED }
        .sumOf { it.total }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OrderSummaryChip(
            emoji = "✅",
            value = "$delivered",
            label = "Delivered",
            background = Color(0xFFE8F5E9),
            modifier = Modifier.weight(1f)
        )
        OrderSummaryChip(
            emoji = "❌",
            value = "$cancelled",
            label = "Cancelled",
            background = Color(0xFFFFEBEE),
            modifier = Modifier.weight(1f)
        )
        OrderSummaryChip(
            emoji = "💰",
            value = "Rs.${totalSpent.toString()
                .reversed().chunked(3)
                .joinToString(",").reversed()}",
            label = "Total Spent",
            background = Color(0xFFFFF3E0),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun OrderSummaryChip(
    emoji: String,
    value: String,
    label: String,
    background: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(background)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = emoji, fontSize = 18.sp)
        Text(
            text = value,
            fontSize = 14.sp,
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

// ── Search bar ────────────────────────────────────────────────────────────────

@Composable
fun OrderHistorySearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        placeholder = {
            Text(
                text = "Search by restaurant or order ID...",
                color = DeepBrown.copy(alpha = 0.3f),
                fontSize = 13.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = WarmAmber,
                modifier = Modifier.size(18.dp)
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = WarmAmber,
            unfocusedBorderColor = DeepBrown.copy(alpha = 0.1f),
            focusedTextColor = DeepBrown,
            unfocusedTextColor = DeepBrown,
            cursorColor = WarmAmber,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

// ── Filter row ────────────────────────────────────────────────────────────────

@Composable
fun OrderHistoryFilterRow(
    selected: String,
    onSelect: (String) -> Unit,
    orders: List<OrderHistoryItem>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(4.dp)
    ) {
        historyFilters.forEach { filter ->
            val isSelected = selected == filter
            val count = when (filter) {
                "Delivered" -> orders.count { it.status == OrderHistoryStatus.DELIVERED }
                "Cancelled" -> orders.count { it.status == OrderHistoryStatus.CANCELLED }
                else        -> orders.size
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) WarmAmber else Color.Transparent)
                    .clickable { onSelect(filter) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = filter,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold
                        else FontWeight.Normal,
                        color = if (isSelected) Cream
                        else DeepBrown.copy(alpha = 0.5f)
                    )
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Cream.copy(alpha = 0.25f)
                                else DeepBrown.copy(alpha = 0.08f)
                            )
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "$count",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Cream
                            else DeepBrown.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

// ── Order history card ────────────────────────────────────────────────────────

@Composable
fun OrderHistoryCard(
    order: OrderHistoryItem,
    isExpanded: Boolean,
    userRating: Int?,
    onToggle: () -> Unit,
    onReorder: () -> Unit,
    onTrack: () -> Unit,
    onRateClick: () -> Unit
) {
    val isDelivered = order.status == OrderHistoryStatus.DELIVERED
    val isCancelled = order.status == OrderHistoryStatus.CANCELLED

    val statusColor = when (order.status) {
        OrderHistoryStatus.DELIVERED -> Color(0xFF4CAF50)
        OrderHistoryStatus.CANCELLED -> Color(0xFFE53935)
        OrderHistoryStatus.PENDING   -> WarmAmber
    }

    val statusLabel = when (order.status) {
        OrderHistoryStatus.DELIVERED -> "Delivered"
        OrderHistoryStatus.CANCELLED -> "Cancelled"
        OrderHistoryStatus.PENDING   -> "Pending"
    }

    val statusEmoji = when (order.status) {
        OrderHistoryStatus.DELIVERED -> "✅"
        OrderHistoryStatus.CANCELLED -> "❌"
        OrderHistoryStatus.PENDING   -> "⏳"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .clickable { onToggle() }
    ) {
        // ── Main row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Restaurant emoji
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(WarmAmber.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = order.restaurantEmoji, fontSize = 26.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = order.restaurantName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepBrown
                        )
                        Text(
                            text = "${order.date} · ${order.time}",
                            fontSize = 11.sp,
                            color = DeepBrown.copy(alpha = 0.4f)
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(statusColor.copy(alpha = 0.1f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "$statusEmoji $statusLabel",
                                fontSize = 10.sp,
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

                Spacer(modifier = Modifier.height(8.dp))

                // Items preview
                Text(
                    text = order.items.take(2).joinToString(", ") +
                            if (order.items.size > 2) " +${order.items.size - 2} more" else "",
                    fontSize = 12.sp,
                    color = DeepBrown.copy(alpha = 0.5f),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Payment chip
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DeepBrown.copy(alpha = 0.06f))
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (order.paymentMethod == "COD")
                                    "💵 COD" else "💳 Online",
                                fontSize = 10.sp,
                                color = DeepBrown.copy(alpha = 0.5f)
                            )
                        }
                        // Order ID
                        Text(
                            text = "#${order.orderId}",
                            fontSize = 10.sp,
                            color = DeepBrown.copy(alpha = 0.3f)
                        )
                    }
                    Text(
                        text = if (isExpanded) "▲ Less" else "▼ More",
                        fontSize = 11.sp,
                        color = DeepBrown.copy(alpha = 0.3f)
                    )
                }
            }
        }

        // ── Expanded section
        if (isExpanded) {
            HorizontalDivider(
                color = DeepBrown.copy(alpha = 0.06f),
                modifier = Modifier.padding(horizontal = 14.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // All items
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Cream)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    order.items.forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(WarmAmber)
                            )
                            Text(
                                text = item,
                                fontSize = 13.sp,
                                color = DeepBrown.copy(alpha = 0.7f)
                            )
                        }
                    }
                    HorizontalDivider(color = DeepBrown.copy(alpha = 0.06f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepBrown
                        )
                        Text(
                            text = "Rs.${order.total}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = WarmAmber
                        )
                    }
                }

                // Rating row (if delivered)
                if (isDelivered) {
                    if (userRating != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Your Rating",
                                fontSize = 13.sp,
                                color = DeepBrown.copy(alpha = 0.5f)
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                repeat(5) { i ->
                                    Text(
                                        text = if (i < userRating) "⭐" else "☆",
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(WarmAmber.copy(alpha = 0.08f))
                                .border(
                                    1.dp,
                                    WarmAmber.copy(alpha = 0.2f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onRateClick() }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "⭐", fontSize = 16.sp)
                                Text(
                                    text = "Rate this order",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = WarmAmber
                                )
                            }
                        }
                    }
                }

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Reorder button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(WarmAmber)
                            .clickable { onReorder() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🔁 Reorder",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Cream
                        )
                    }

                    if (!isCancelled) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(DeepBrown.copy(alpha = 0.08f))
                                .clickable { onTrack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🧾 Receipt",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DeepBrown
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Rate order dialog ─────────────────────────────────────────────────────────

@Composable
fun RateOrderDialog(
    order: OrderHistoryItem,
    onSubmit: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var rating by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(text = order.restaurantEmoji, fontSize = 44.sp)

            Text(
                text = "Rate ${order.restaurantName}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Order #${order.orderId} · ${order.date}",
                fontSize = 12.sp,
                color = DeepBrown.copy(alpha = 0.4f)
            )

            // Stars
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(5) { index ->
                    Text(
                        text = if (index < rating) "⭐" else "☆",
                        fontSize = 36.sp,
                        modifier = Modifier.clickable { rating = index + 1 }
                    )
                }
            }

            // Rating label
            if (rating > 0) {
                Text(
                    text = when (rating) {
                        1 -> "😞 Poor"
                        2 -> "😐 Fair"
                        3 -> "🙂 Good"
                        4 -> "😊 Great"
                        5 -> "🤩 Excellent!"
                        else -> ""
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WarmAmber
                )
            }

            // Comment
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Share your experience (optional)",
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
                onClick = { if (rating > 0) onSubmit(rating) },
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
                    text = "Submit Rating",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            TextButton(onClick = onDismiss) {
                Text(
                    text = "Maybe later",
                    color = DeepBrown.copy(alpha = 0.4f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

// ── Empty state ───────────────────────────────────────────────────────────────

@Composable
fun EmptyOrderHistory() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "🛒", fontSize = 56.sp)
        Text(
            text = "No orders found",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown
        )
        Text(
            text = "Your order history will\nappear here",
            fontSize = 14.sp,
            color = DeepBrown.copy(alpha = 0.45f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}