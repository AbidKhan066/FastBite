package com.saas.fastbite.screens.restaurant

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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

data class TokenBundle(
    val id: String,
    val tokens: Int,
    val price: Int,
    val bonus: Int = 0,
    val tag: String? = null,
    val isPopular: Boolean = false
)

data class TokenTransaction(
    val id: String,
    val type: TransactionType,
    val description: String,
    val tokens: Int,
    val date: String,
    val time: String
)

enum class TransactionType { PURCHASE, DEDUCTION, BONUS }

val tokenBundles = listOf(
    TokenBundle("b1", 50,   500,  0,    null,      false),
    TokenBundle("b2", 120,  1000, 20,   "Popular", true),
    TokenBundle("b3", 250,  2000, 50,   "Best Value", false),
    TokenBundle("b4", 600,  4500, 100,  "Pro",     false),
)

val tokenHistory = listOf(
    TokenTransaction("t1",  TransactionType.PURCHASE,  "Purchased 120 tokens",       +120, "28 Mar 2026", "10:30 AM"),
    TokenTransaction("t2",  TransactionType.DEDUCTION, "Order #FB041 deducted",       -1,  "28 Mar 2026", "10:45 AM"),
    TokenTransaction("t3",  TransactionType.DEDUCTION, "Order #FB042 deducted",       -1,  "28 Mar 2026", "11:02 AM"),
    TokenTransaction("t4",  TransactionType.BONUS,     "Bonus tokens credited",       +20, "27 Mar 2026", "09:00 AM"),
    TokenTransaction("t5",  TransactionType.DEDUCTION, "Order #FB038 deducted",       -1,  "27 Mar 2026", "01:15 PM"),
    TokenTransaction("t6",  TransactionType.DEDUCTION, "Order #FB039 deducted",       -1,  "27 Mar 2026", "02:30 PM"),
    TokenTransaction("t7",  TransactionType.PURCHASE,  "Purchased 50 tokens",        +50,  "26 Mar 2026", "03:00 PM"),
    TokenTransaction("t8",  TransactionType.DEDUCTION, "Order #FB031 deducted",       -1,  "26 Mar 2026", "04:10 PM"),
    TokenTransaction("t9",  TransactionType.DEDUCTION, "Order #FB032 deducted",       -1,  "25 Mar 2026", "12:00 PM"),
    TokenTransaction("t10", TransactionType.DEDUCTION, "Order #FB033 deducted",       -1,  "25 Mar 2026", "01:45 PM"),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun TokenWalletScreen(onBack: () -> Unit = {}) {
    var balance by remember { mutableIntStateOf(142) }
    var selectedBundle by remember { mutableStateOf<TokenBundle?>(null) }
    var showPurchaseDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }

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
            item { TokenTopBar(onBack = onBack) }

            // ── Balance card
            item { TokenBalanceCard(balance = balance) }

            // ── Usage info row
            item { TokenUsageInfo() }

            // ── Tabs
            item {
                TokenTabs(
                    selected = selectedTab,
                    onSelect = { selectedTab = it }
                )
            }

            if (selectedTab == 0) {
                // ── Buy tokens
                item {
                    Text(
                        text = "Choose a Bundle",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepBrown,
                        modifier = Modifier.padding(
                            horizontal = 20.dp,
                            vertical = 12.dp
                        )
                    )
                }
                items(tokenBundles) { bundle ->
                    TokenBundleCard(
                        bundle = bundle,
                        isSelected = selectedBundle?.id == bundle.id,
                        onClick = {
                            selectedBundle = bundle
                            showPurchaseDialog = true
                        }
                    )
                }
            } else {
                // ── Transaction history
                item {
                    Text(
                        text = "Transaction History",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepBrown,
                        modifier = Modifier.padding(
                            horizontal = 20.dp,
                            vertical = 12.dp
                        )
                    )
                }
                items(tokenHistory) { tx ->
                    TransactionRow(transaction = tx)
                }
            }
        }
    }

    // ── Purchase confirm dialog
    if (showPurchaseDialog && selectedBundle != null) {
        PurchaseConfirmDialog(
            bundle = selectedBundle!!,
            onConfirm = {
                balance += selectedBundle!!.tokens + selectedBundle!!.bonus
                showPurchaseDialog = false
                showSuccessDialog = true
            },
            onDismiss = {
                showPurchaseDialog = false
                selectedBundle = null
            }
        )
    }

    // ── Success dialog
    if (showSuccessDialog) {
        PurchaseSuccessDialog(
            tokens = (selectedBundle?.tokens ?: 0) + (selectedBundle?.bonus ?: 0),
            onDismiss = {
                showSuccessDialog = false
                selectedBundle = null
            }
        )
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
fun TokenTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
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
            text = "Token Wallet",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown
        )
    }
}

// ── Balance card ──────────────────────────────────────────────────────────────

@Composable
fun TokenBalanceCard(balance: Int) {
    // Coin spin animation
    val infiniteTransition = rememberInfiniteTransition(label = "coin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "coinRotation"
    )

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
                .size(120.dp)
                .clip(CircleShape)
                .background(WarmAmber.copy(alpha = 0.07f))
                .align(Alignment.TopEnd)
                .offset(x = 30.dp, y = (-30).dp)
        )
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(WarmAmber.copy(alpha = 0.05f))
                .align(Alignment.BottomStart)
                .offset(x = (-20).dp, y = 20.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Available Balance",
                        fontSize = 13.sp,
                        color = Cream.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "🪙",
                            fontSize = 32.sp,
                            modifier = Modifier.rotate(rotation)
                        )
                        Text(
                            text = "$balance",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            color = WarmAmber
                        )
                    }
                    Text(
                        text = "Tokens",
                        fontSize = 14.sp,
                        color = Cream.copy(alpha = 0.5f)
                    )
                }

                // Quick stats
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MiniStatPill(label = "Used today", value = "2")
                    MiniStatPill(label = "This month", value = "58")
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Progress bar
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Usage this month",
                        fontSize = 11.sp,
                        color = Cream.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "58 / 200",
                        fontSize = 11.sp,
                        color = Cream.copy(alpha = 0.5f)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Cream.copy(alpha = 0.1f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.29f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(3.dp))
                            .background(WarmAmber)
                    )
                }
            }
        }
    }
}

@Composable
fun MiniStatPill(label: String, value: String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Cream.copy(alpha = 0.07f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = WarmAmber
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = Cream.copy(alpha = 0.4f)
        )
    }
}

// ── Usage info ────────────────────────────────────────────────────────────────

@Composable
fun TokenUsageInfo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        UsageInfoChip(
            emoji = "📦",
            text = "1 token per order",
            modifier = Modifier.weight(1f)
        )
        UsageInfoChip(
            emoji = "⚠️",
            text = "Low balance alert at 10",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun UsageInfoChip(emoji: String, text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = emoji, fontSize = 16.sp)
        Text(
            text = text,
            fontSize = 11.sp,
            color = DeepBrown.copy(alpha = 0.65f),
            lineHeight = 16.sp
        )
    }
}

// ── Tabs ──────────────────────────────────────────────────────────────────────

@Composable
fun TokenTabs(selected: Int, onSelect: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(4.dp)
    ) {
        listOf("Buy Tokens", "History").forEachIndexed { index, label ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selected == index) WarmAmber
                        else Color.Transparent
                    )
                    .clickable { onSelect(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = if (selected == index) FontWeight.Bold
                    else FontWeight.Normal,
                    color = if (selected == index) Cream
                    else DeepBrown.copy(alpha = 0.5f)
                )
            }
        }
    }
}

// ── Token bundle card ─────────────────────────────────────────────────────────

@Composable
fun TokenBundleCard(
    bundle: TokenBundle,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (bundle.isPopular) DeepBrown
                else Color.White
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) WarmAmber
                else if (bundle.isPopular) Color.Transparent
                else DeepBrown.copy(alpha = 0.08f),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Coin icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (bundle.isPopular) WarmAmber.copy(alpha = 0.2f)
                        else WarmAmber.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🪙", fontSize = 26.sp)
            }

            // Token info
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${bundle.tokens} Tokens",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (bundle.isPopular) WarmAmber else DeepBrown
                    )
                    bundle.tag?.let { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(WarmAmber)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 10.sp,
                                color = Cream,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                if (bundle.bonus > 0) {
                    Text(
                        text = "+ ${bundle.bonus} bonus tokens free!",
                        fontSize = 12.sp,
                        color = if (bundle.isPopular)
                            Color(0xFF4CAF50)
                        else
                            Color(0xFF2E7D32),
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = "Rs.${bundle.price}",
                    fontSize = 13.sp,
                    color = if (bundle.isPopular)
                        Cream.copy(alpha = 0.6f)
                    else
                        DeepBrown.copy(alpha = 0.5f)
                )
            }

            // Price + arrow
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Rs.${bundle.price}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (bundle.isPopular) WarmAmber else DeepBrown
                )
                if (bundle.bonus > 0) {
                    Text(
                        text = "${bundle.tokens + bundle.bonus} total",
                        fontSize = 10.sp,
                        color = if (bundle.isPopular)
                            Cream.copy(alpha = 0.4f)
                        else
                            DeepBrown.copy(alpha = 0.35f)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "›",
                    fontSize = 22.sp,
                    color = if (bundle.isPopular)
                        WarmAmber.copy(alpha = 0.6f)
                    else
                        DeepBrown.copy(alpha = 0.25f)
                )
            }
        }
    }
}

// ── Transaction row ───────────────────────────────────────────────────────────

@Composable
fun TransactionRow(transaction: TokenTransaction) {
    val isPositive = transaction.tokens > 0
    val color = when (transaction.type) {
        TransactionType.PURCHASE -> Color(0xFF1565C0)
        TransactionType.DEDUCTION -> Color(0xFFE53935)
        TransactionType.BONUS -> Color(0xFF2E7D32)
    }
    val emoji = when (transaction.type) {
        TransactionType.PURCHASE -> "💳"
        TransactionType.DEDUCTION -> "📦"
        TransactionType.BONUS -> "🎁"
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
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 18.sp)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.description,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = DeepBrown
            )
            Text(
                text = "${transaction.date} · ${transaction.time}",
                fontSize = 11.sp,
                color = DeepBrown.copy(alpha = 0.4f)
            )
        }

        Text(
            text = "${if (isPositive) "+" else ""}${transaction.tokens}",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

// ── Purchase confirm dialog ───────────────────────────────────────────────────

@Composable
fun PurchaseConfirmDialog(
    bundle: TokenBundle,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
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
            Text(text = "🪙", fontSize = 44.sp)

            Text(
                text = "Confirm Purchase",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )

            // Summary box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Cream)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryRow("Tokens", "${bundle.tokens}")
                if (bundle.bonus > 0) {
                    SummaryRow("Bonus", "+${bundle.bonus} free", valueColor = Color(0xFF2E7D32))
                }
                SummaryRow(
                    "Total Tokens",
                    "${bundle.tokens + bundle.bonus}",
                    bold = true
                )
                HorizontalDivider(color = DeepBrown.copy(alpha = 0.07f))
                SummaryRow(
                    "Amount to Pay",
                    "Rs.${bundle.price}",
                    bold = true,
                    valueColor = WarmAmber
                )
            }

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
                    text = "Pay Rs.${bundle.price}",
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

@Composable
fun SummaryRow(
    label: String,
    value: String,
    bold: Boolean = false,
    valueColor: Color = DeepBrown
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = DeepBrown.copy(alpha = 0.6f),
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.SemiBold,
            color = valueColor
        )
    }
}

// ── Purchase success dialog ───────────────────────────────────────────────────

@Composable
fun PurchaseSuccessDialog(tokens: Int, onDismiss: () -> Unit) {
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
            Text(text = "🎉", fontSize = 52.sp)

            Text(
                text = "Tokens Added!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(WarmAmber.copy(alpha = 0.1f))
                    .border(
                        1.dp,
                        WarmAmber.copy(alpha = 0.3f),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "+$tokens 🪙",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = WarmAmber,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = "Your wallet has been topped up\nsuccessfully.",
                fontSize = 13.sp,
                color = DeepBrown.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

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
                    text = "Great!",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}