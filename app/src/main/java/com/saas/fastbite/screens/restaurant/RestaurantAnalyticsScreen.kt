package com.saas.fastbite.screens.restaurant

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
import androidx.compose.material.icons.filled.TrendingUp
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
import com.saas.fastbite.ui.theme.Cream
import com.saas.fastbite.ui.theme.DeepBrown
import com.saas.fastbite.ui.theme.WarmAmber

// ── Data models ───────────────────────────────────────────────────────────────

data class DailyBar(val day: String, val orders: Int, val revenue: Int)
data class TopItem(val name: String, val emoji: String, val orders: Int, val revenue: Int)
data class AnalyticsStat(
    val label: String,
    val value: String,
    val change: String,
    val isPositive: Boolean,
    val emoji: String,
    val background: Color
)

val weeklyData = listOf(
    DailyBar("Mon", 18, 14400),
    DailyBar("Tue", 24, 19200),
    DailyBar("Wed", 20, 16000),
    DailyBar("Thu", 30, 24000),
    DailyBar("Fri", 38, 30400),
    DailyBar("Sat", 45, 36000),
    DailyBar("Sun", 28, 22400),
)

val topItems = listOf(
    TopItem("Classic Beef Burger", "🍔", 142, 49700),
    TopItem("Burger Combo",        "🎉", 98,  63700),
    TopItem("Loaded Fries",        "🍟", 87,  21750),
    TopItem("Chocolate Shake",     "🥤", 64,  14080),
    TopItem("Double Smash Burger", "🍔", 52,  28600),
)

val analyticsStats = listOf(
    AnalyticsStat("Total Orders",   "203",        "+12%",  true,  "📦", Color(0xFFE3F2FD)),
    AnalyticsStat("Revenue",        "Rs.1,62,400","+ 8%",  true,  "💰", Color(0xFFE8F5E9)),
    AnalyticsStat("Tokens Used",    "203",        "= 0%",  true,  "🪙", Color(0xFFFFF8E1)),
    AnalyticsStat("Avg. Order",     "Rs.800",     "+ 4%",  true,  "🧾", Color(0xFFFCE4EC)),
    AnalyticsStat("Avg. Rating",    "4.8 ⭐",     "+ 0.1", true,  "⭐", Color(0xFFFFF3E0)),
    AnalyticsStat("Cancelled",      "6",          "- 3",   false, "❌", Color(0xFFFFEBEE)),
)

val periodOptions = listOf("Today", "This Week", "This Month", "All Time")

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun RestaurantAnalyticsScreen(onBack: () -> Unit = {}) {
    var selectedPeriod by remember { mutableStateOf("This Week") }

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
            item { AnalyticsTopBar(onBack = onBack) }

            // ── Period selector
            item {
                PeriodSelector(
                    selected = selectedPeriod,
                    onSelect = { selectedPeriod = it }
                )
            }

            // ── Summary stats grid
            item { AnalyticsSummaryGrid() }

            // ── Bar chart
            item {
                AnalyticsSectionTitle(
                    title = "Orders & Revenue",
                    subtitle = "Daily breakdown"
                )
                OrdersBarChart()
            }

            // ── Revenue breakdown
            item {
                AnalyticsSectionTitle(
                    title = "Revenue Breakdown",
                    subtitle = "This week"
                )
                RevenueBreakdownCard()
            }

            // ── Top selling items
            item {
                AnalyticsSectionTitle(
                    title = "🔥 Top Selling Items",
                    subtitle = "By order count"
                )
            }
            items(topItems.take(5)) { item ->
                TopItemRow(
                    item = item,
                    maxOrders = topItems.maxOf { it.orders }
                )
            }

            // ── Order status breakdown
            item {
                AnalyticsSectionTitle(
                    title = "Order Status",
                    subtitle = "This week"
                )
                OrderStatusBreakdown()
            }

            // ── Token usage
            item {
                AnalyticsSectionTitle(
                    title = "🪙 Token Usage",
                    subtitle = "Monthly"
                )
                TokenUsageCard()
            }
        }
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
fun AnalyticsTopBar(onBack: () -> Unit) {
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
                text = "Analytics",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(WarmAmber.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.TrendingUp,
                contentDescription = null,
                tint = WarmAmber,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ── Period selector ───────────────────────────────────────────────────────────

@Composable
fun PeriodSelector(selected: String, onSelect: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        items(periodOptions) { period ->
            val isSelected = selected == period
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) WarmAmber else Color.White)
                    .border(
                        1.dp,
                        if (isSelected) WarmAmber else DeepBrown.copy(alpha = 0.1f),
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { onSelect(period) }
                    .padding(horizontal = 16.dp, vertical = 9.dp)
            ) {
                Text(
                    text = period,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold
                    else FontWeight.Normal,
                    color = if (isSelected) Cream
                    else DeepBrown.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// ── Summary stats grid ────────────────────────────────────────────────────────

@Composable
fun AnalyticsSummaryGrid() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        analyticsStats.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { stat ->
                    AnalyticsStatCard(
                        stat = stat,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun AnalyticsStatCard(stat: AnalyticsStat, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(stat.background)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stat.emoji, fontSize = 20.sp)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        if (stat.isPositive) Color(0xFF4CAF50).copy(alpha = 0.15f)
                        else Color(0xFFE53935).copy(alpha = 0.12f)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = stat.change,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (stat.isPositive) Color(0xFF2E7D32)
                    else Color(0xFFE53935)
                )
            }
        }
        Text(
            text = stat.value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown
        )
        Text(
            text = stat.label,
            fontSize = 11.sp,
            color = DeepBrown.copy(alpha = 0.5f)
        )
    }
}

// ── Section title ─────────────────────────────────────────────────────────────

@Composable
fun AnalyticsSectionTitle(title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown
        )
        Text(
            text = subtitle,
            fontSize = 11.sp,
            color = DeepBrown.copy(alpha = 0.4f)
        )
    }
}

// ── Orders bar chart ──────────────────────────────────────────────────────────

@Composable
fun OrdersBarChart() {
    val maxOrders = weeklyData.maxOf { it.orders }
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 900, easing = EaseOut)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        // Y-axis labels + bars
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            weeklyData.forEach { bar ->
                val fraction = (bar.orders.toFloat() / maxOrders) * animatedProgress.value
                val isToday = bar.day == "Sat"

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    // Order count on top
                    Text(
                        text = "${bar.orders}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isToday) WarmAmber else DeepBrown.copy(alpha = 0.5f),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    // Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(fraction)
                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                            .background(
                                if (isToday) WarmAmber
                                else WarmAmber.copy(alpha = 0.25f)
                            )
                    )
                }
            }
        }

        HorizontalDivider(
            color = DeepBrown.copy(alpha = 0.06f),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Day labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            weeklyData.forEach { bar ->
                val isToday = bar.day == "Sat"
                Text(
                    text = bar.day,
                    fontSize = 11.sp,
                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                    color = if (isToday) WarmAmber else DeepBrown.copy(alpha = 0.4f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Legend
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(WarmAmber)
                )
                Text(
                    text = "Highest day",
                    fontSize = 11.sp,
                    color = DeepBrown.copy(alpha = 0.5f)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(WarmAmber.copy(alpha = 0.25f))
                )
                Text(
                    text = "Other days",
                    fontSize = 11.sp,
                    color = DeepBrown.copy(alpha = 0.5f)
                )
            }
        }
    }
}

// ── Revenue breakdown card ────────────────────────────────────────────────────

@Composable
fun RevenueBreakdownCard() {
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animatedProgress.animateTo(1f, tween(900, easing = EaseOut))
    }

    val segments = listOf(
        Triple("Burgers",  0.52f, WarmAmber),
        Triple("Combos",   0.28f, DeepBrown),
        Triple("Sides",    0.12f, Color(0xFFC08552).copy(alpha = 0.6f)),
        Triple("Drinks",   0.08f, DeepBrown.copy(alpha = 0.35f)),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Segmented bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(7.dp))
        ) {
            segments.forEach { (_, fraction, color) ->
                Box(
                    modifier = Modifier
                        .weight(fraction * animatedProgress.value + 0.001f)
                        .fillMaxHeight()
                        .background(color)
                )
            }
        }

        // Segment legend
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            segments.forEach { (label, fraction, color) ->
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
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                        Text(
                            text = label,
                            fontSize = 13.sp,
                            color = DeepBrown.copy(alpha = 0.7f)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Progress bar mini
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(DeepBrown.copy(alpha = 0.06f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction * animatedProgress.value)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(color)
                            )
                        }
                        Text(
                            text = "${(fraction * 100).toInt()}%",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DeepBrown
                        )
                    }
                }
            }
        }

        HorizontalDivider(color = DeepBrown.copy(alpha = 0.06f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total Revenue",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
            Text(
                text = "Rs.1,62,400",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = WarmAmber
            )
        }
    }
}

// ── Top item row ──────────────────────────────────────────────────────────────

@Composable
fun TopItemRow(item: TopItem, maxOrders: Int) {
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(item.name) {
        animatedProgress.animateTo(
            targetValue = item.orders.toFloat() / maxOrders,
            animationSpec = tween(700, easing = EaseOut)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Emoji
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(WarmAmber.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = item.emoji, fontSize = 22.sp)
        }

        // Info + bar
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepBrown
                )
                Text(
                    text = "${item.orders} orders",
                    fontSize = 12.sp,
                    color = WarmAmber,
                    fontWeight = FontWeight.SemiBold
                )
            }
            // Animated bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(DeepBrown.copy(alpha = 0.06f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress.value)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(3.dp))
                        .background(WarmAmber)
                )
            }
            Text(
                text = "Rs.${item.revenue.toString()
                    .reversed().chunked(3).joinToString(",").reversed()}",
                fontSize = 11.sp,
                color = DeepBrown.copy(alpha = 0.45f)
            )
        }
    }
}

// ── Order status breakdown ────────────────────────────────────────────────────

@Composable
fun OrderStatusBreakdown() {
    val statuses = listOf(
        Triple("Delivered",  189, Color(0xFF4CAF50)),
        Triple("Cancelled",  6,   Color(0xFFE53935)),
        Triple("Pending",    8,   WarmAmber),
    )
    val total = statuses.sumOf { it.second }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        statuses.forEach { (label, count, color) ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.1f))
                        .border(2.dp, color.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$count",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = DeepBrown.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${(count * 100 / total)}%",
                    fontSize = 11.sp,
                    color = color,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ── Token usage card ──────────────────────────────────────────────────────────

@Composable
fun TokenUsageCard() {
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animatedProgress.animateTo(1f, tween(900, easing = EaseOut))
    }

    val used = 203
    val purchased = 345
    val remaining = purchased - used
    val fraction = used.toFloat() / purchased

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(DeepBrown)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "🪙 Tokens This Month",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Cream
                )
                Text(
                    text = "March 2026",
                    fontSize = 11.sp,
                    color = Cream.copy(alpha = 0.4f)
                )
            }
            Text(
                text = "$remaining left",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = WarmAmber
            )
        }

        // Big numbers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            TokenStat("Purchased", "$purchased", Cream)
            TokenStat("Used",      "$used",      WarmAmber)
            TokenStat("Remaining", "$remaining", Color(0xFF4CAF50))
        }

        // Animated progress bar
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Cream.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction * animatedProgress.value)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(5.dp))
                        .background(WarmAmber)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${(fraction * 100).toInt()}% used",
                    fontSize = 11.sp,
                    color = Cream.copy(alpha = 0.45f)
                )
                Text(
                    text = "1 token = 1 order",
                    fontSize = 11.sp,
                    color = Cream.copy(alpha = 0.45f)
                )
            }
        }
    }
}

@Composable
fun TokenStat(label: String, value: String, color: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Cream.copy(alpha = 0.4f)
        )
    }
}