package com.saas.fastbite.screens.rider

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

data class DeliveryRecord(
    val id: String,
    val orderId: String,
    val customerName: String,
    val restaurantName: String,
    val address: String,
    val items: String,
    val earning: Int,
    val distance: String,
    val duration: String,
    val date: String,
    val time: String,
    val rating: Float? = null
)

data class EarningStat(
    val label: String,
    val value: String,
    val emoji: String,
    val background: Color
)

val deliveryRecords = listOf(
    DeliveryRecord(
        "d1", "FB041", "Ahmed Khan", "Burger Lab",
        "Block 5, Gulshan-e-Iqbal",
        "2x Burger, 1x Fries, 1x Shake",
        160, "2.4 km", "18 min",
        "29 Mar 2026", "10:45 AM", 5.0f
    ),
    DeliveryRecord(
        "d2", "FB039", "Sara Malik", "Pizza House",
        "North Nazimabad, Block H",
        "1x Family Feast",
        220, "3.8 km", "25 min",
        "29 Mar 2026", "12:10 PM", 4.0f
    ),
    DeliveryRecord(
        "d3", "FB037", "Usman Raza", "Crispy Wings",
        "Gulshan Block 13D",
        "3x Zinger, 2x Drink",
        140, "1.9 km", "14 min",
        "28 Mar 2026", "02:30 PM", 5.0f
    ),
    DeliveryRecord(
        "d4", "FB035", "Hina Akram", "Noodle Bar",
        "PECHS Block 2",
        "2x Noodle Bowl, 1x Soup",
        180, "3.1 km", "22 min",
        "28 Mar 2026", "06:15 PM", 4.0f
    ),
    DeliveryRecord(
        "d5", "FB033", "Bilal Siddiqui", "Burger Lab",
        "DHA Phase 4",
        "1x Double Smash, 1x Fries",
        200, "4.2 km", "28 min",
        "27 Mar 2026", "01:00 PM", null
    ),
    DeliveryRecord(
        "d6", "FB031", "Ayesha Noor", "Wrap Republic",
        "Clifton Block 5",
        "2x Wrap, 2x Drink",
        160, "2.7 km", "19 min",
        "27 Mar 2026", "04:45 PM", 5.0f
    ),
    DeliveryRecord(
        "d7", "FB028", "Kamran Ali", "Green Bowl",
        "Karachi University Town",
        "1x Salad Bowl, 1x Juice",
        120, "1.5 km", "12 min",
        "26 Mar 2026", "11:30 AM", 4.0f
    ),
    DeliveryRecord(
        "d8", "FB025", "Fatima Zahra", "Burger Lab",
        "Gulistan-e-Jauhar",
        "1x Burger Combo",
        160, "2.2 km", "16 min",
        "26 Mar 2026", "07:00 PM", 5.0f
    ),
)

val earningStats = listOf(
    EarningStat("Today",      "Rs.380",  "📅", Color(0xFFE3F2FD)),
    EarningStat("This Week",  "Rs.1,340","📆", Color(0xFFE8F5E9)),
    EarningStat("This Month", "Rs.4,820","🗓️", Color(0xFFFFF3E0)),
    EarningStat("Total",      "Rs.18,600","💰", Color(0xFFFCE4EC)),
)

val filterOptions = listOf("All", "Today", "This Week", "This Month")

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun RiderHistoryScreen(onBack: () -> Unit = {}) {
    var selectedFilter by remember { mutableStateOf("All") }
    var expandedId by remember { mutableStateOf<String?>(null) }

    val filteredRecords = when (selectedFilter) {
        "Today"      -> deliveryRecords.filter { it.date == "29 Mar 2026" }
        "This Week"  -> deliveryRecords
        "This Month" -> deliveryRecords
        else         -> deliveryRecords
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
            item { HistoryTopBar(onBack = onBack) }

            // ── Earning stats
            item { EarningStatsRow() }

            // ── Performance card
            item { PerformanceCard() }

            // ── Filter row
            item {
                FilterRow(
                    selected = selectedFilter,
                    onSelect = { selectedFilter = it }
                )
            }

            // ── Records count
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Deliveries",
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
                            text = "${filteredRecords.size} trips",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = WarmAmber
                        )
                    }
                }
            }

            // ── Delivery records
            if (filteredRecords.isEmpty()) {
                item { EmptyHistoryPlaceholder() }
            } else {
                items(filteredRecords, key = { it.id }) { record ->
                    DeliveryRecordCard(
                        record = record,
                        isExpanded = expandedId == record.id,
                        onToggle = {
                            expandedId = if (expandedId == record.id) null
                            else record.id
                        }
                    )
                }
            }
        }
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
fun HistoryTopBar(onBack: () -> Unit) {
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
                text = "Delivery History",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFE8F5E9))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text = "38 total",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2E7D32)
            )
        }
    }
}

// ── Earning stats row ─────────────────────────────────────────────────────────

@Composable
fun EarningStatsRow() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        items(earningStats) { stat ->
            EarningStatCard(stat = stat)
        }
    }
}

@Composable
fun EarningStatCard(stat: EarningStat) {
    Column(
        modifier = Modifier
            .width(110.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(stat.background)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = stat.emoji, fontSize = 18.sp)
        Text(
            text = stat.value,
            fontSize = 15.sp,
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

// ── Performance card ──────────────────────────────────────────────────────────

@Composable
fun PerformanceCard() {
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animatedProgress.animateTo(1f, tween(900, easing = EaseOut))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(DeepBrown)
            .padding(20.dp)
    ) {
        // Decorative circle
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.04f))
                .align(Alignment.TopEnd)
                .offset(x = 20.dp, y = (-20).dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⚡ Performance",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Cream
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(WarmAmber.copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "This Week",
                        fontSize = 11.sp,
                        color = WarmAmber,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PerformanceStat("38", "Deliveries", Cream)
                PerformanceStatDivider()
                PerformanceStat("4.9 ⭐", "Rating", WarmAmber)
                PerformanceStatDivider()
                PerformanceStat("96%", "On Time", Color(0xFF4CAF50))
                PerformanceStatDivider()
                PerformanceStat("142 km", "Distance", Cream.copy(alpha = 0.7f))
            }

            // Rating bar
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Acceptance Rate",
                        fontSize = 12.sp,
                        color = Cream.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "94%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarmAmber
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Cream.copy(alpha = 0.1f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.94f * animatedProgress.value)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(WarmAmber)
                    )
                }
            }
        }
    }
}

@Composable
fun PerformanceStat(value: String, label: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = Cream.copy(alpha = 0.4f)
        )
    }
}

@Composable
fun PerformanceStatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(32.dp)
            .background(Cream.copy(alpha = 0.1f))
    )
}

// ── Filter row ────────────────────────────────────────────────────────────────

@Composable
fun FilterRow(selected: String, onSelect: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(filterOptions) { filter ->
            val isSelected = selected == filter
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) WarmAmber else Color.White)
                    .border(
                        1.dp,
                        if (isSelected) WarmAmber else DeepBrown.copy(alpha = 0.1f),
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { onSelect(filter) }
                    .padding(horizontal = 16.dp, vertical = 9.dp)
            ) {
                Text(
                    text = filter,
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

// ── Delivery record card ──────────────────────────────────────────────────────

@Composable
fun DeliveryRecordCard(
    record: DeliveryRecord,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Status circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "✅", fontSize = 22.sp)
            }

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = record.customerName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepBrown
                    )
                    record.rating?.let {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(WarmAmber.copy(alpha = 0.12f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "⭐ ${"%.0f".format(it)}",
                                fontSize = 10.sp,
                                color = WarmAmber,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                Text(
                    text = "${record.restaurantName} · ${record.date}",
                    fontSize = 12.sp,
                    color = DeepBrown.copy(alpha = 0.45f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MiniChip("📍 ${record.distance}")
                    MiniChip("⏱ ${record.duration}")
                    MiniChip("🕐 ${record.time}")
                }
            }

            // Earning
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Rs.${record.earning}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = "#${record.orderId}",
                    fontSize = 10.sp,
                    color = DeepBrown.copy(alpha = 0.35f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isExpanded) "▲" else "▼",
                    fontSize = 12.sp,
                    color = DeepBrown.copy(alpha = 0.3f)
                )
            }
        }

        // ── Expanded detail
        if (isExpanded) {
            HorizontalDivider(
                color = DeepBrown.copy(alpha = 0.06f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 14.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Route
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Cream)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RouteRow(
                        dot = WarmAmber,
                        label = "Picked up from",
                        value = record.restaurantName
                    )
                    Box(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .width(2.dp)
                            .height(14.dp)
                            .background(DeepBrown.copy(alpha = 0.12f))
                    )
                    RouteRow(
                        dot = DeepBrown,
                        label = "Delivered to",
                        value = record.address
                    )
                }

                // Items
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(WarmAmber.copy(alpha = 0.06f))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🧾", fontSize = 13.sp)
                    Text(
                        text = record.items,
                        fontSize = 12.sp,
                        color = DeepBrown.copy(alpha = 0.6f)
                    )
                }

                // Earning breakdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    EarningBreakdownChip(
                        label = "Base Pay",
                        value = "Rs.100",
                        modifier = Modifier.weight(1f)
                    )
                    EarningBreakdownChip(
                        label = "Distance",
                        value = "Rs.${record.earning - 100}",
                        modifier = Modifier.weight(1f)
                    )
                    EarningBreakdownChip(
                        label = "Total",
                        value = "Rs.${record.earning}",
                        isHighlighted = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Rating display
                record.rating?.let { rating ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Customer Rating",
                            fontSize = 12.sp,
                            color = DeepBrown.copy(alpha = 0.5f)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            repeat(5) { index ->
                                Text(
                                    text = if (index < rating.toInt()) "⭐" else "☆",
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                } ?: run {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(DeepBrown.copy(alpha = 0.05f))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No rating received",
                            fontSize = 12.sp,
                            color = DeepBrown.copy(alpha = 0.35f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RouteRow(dot: Color, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(9.dp)
                .clip(CircleShape)
                .background(dot)
        )
        Column {
            Text(
                text = label,
                fontSize = 10.sp,
                color = DeepBrown.copy(alpha = 0.4f)
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = DeepBrown
            )
        }
    }
}

@Composable
fun MiniChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(DeepBrown.copy(alpha = 0.06f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            color = DeepBrown.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun EarningBreakdownChip(
    label: String,
    value: String,
    isHighlighted: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isHighlighted) Color(0xFF4CAF50).copy(alpha = 0.1f)
                else Cream
            )
            .border(
                1.dp,
                if (isHighlighted) Color(0xFF4CAF50).copy(alpha = 0.3f)
                else Color.Transparent,
                RoundedCornerShape(10.dp)
            )
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isHighlighted) Color(0xFF2E7D32) else DeepBrown
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = DeepBrown.copy(alpha = 0.45f),
            textAlign = TextAlign.Center
        )
    }
}

// ── Empty placeholder ─────────────────────────────────────────────────────────

@Composable
fun EmptyHistoryPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "📭", fontSize = 56.sp)
        Text(
            text = "No deliveries yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown
        )
        Text(
            text = "Your completed deliveries\nwill show up here",
            fontSize = 14.sp,
            color = DeepBrown.copy(alpha = 0.45f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}