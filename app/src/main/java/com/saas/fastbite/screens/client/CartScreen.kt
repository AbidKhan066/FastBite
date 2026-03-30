package com.saas.fastbite.screens.client

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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

data class CartItemModel(
    val name: String,
    val emoji: String,
    val price: Int,
    var qty: Int
)

enum class PaymentMethod { COD, ONLINE }

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun CartScreen(
    onBack: () -> Unit = {},
    onOrderPlaced: () -> Unit = {}
) {
    val cartItems = remember {
        mutableStateListOf(
            CartItemModel("Classic Beef Burger", "🍔", 350, 2),
            CartItemModel("Loaded Fries", "🍟", 250, 1),
            CartItemModel("Burger Combo", "🎉", 650, 1),
            CartItemModel("Chocolate Shake", "🥤", 220, 1),
        )
    }

    var selectedPayment by remember { mutableStateOf(PaymentMethod.COD) }
    var selectedAddress by remember { mutableStateOf("Home — Block 5, Gulshan-e-Iqbal") }
    var showAddressDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var promoCode by remember { mutableStateOf("") }
    var promoApplied by remember { mutableStateOf(false) }

    val subtotal = cartItems.sumOf { it.price * it.qty }
    val deliveryFee = 50
    val discount = if (promoApplied) (subtotal * 0.1).toInt() else 0
    val total = subtotal + deliveryFee - discount

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
            item { CartTopBar(onBack = onBack) }

            // ── Empty state
            if (cartItems.isEmpty()) {
                item { EmptyCart() }
            } else {

                // ── Delivery address
                item {
                    SectionCard(title = "📍 Delivery Address") {
                        AddressRow(
                            address = selectedAddress,
                            onClick = { showAddressDialog = true }
                        )
                    }
                }

                // ── Cart items
                item {
                    SectionCard(title = "🛒 Your Order") {}
                }
                items(cartItems, key = { it.name }) { item ->
                    CartItemRow(
                        item = item,
                        onAdd = { item.qty++ },
                        onRemove = {
                            if (item.qty > 1) item.qty--
                            else cartItems.remove(item)
                        },
                        onDelete = { cartItems.remove(item) }
                    )
                }

                // ── Promo code
                item {
                    SectionCard(title = "🎟️ Promo Code") {
                        PromoRow(
                            code = promoCode,
                            onCodeChange = { promoCode = it },
                            applied = promoApplied,
                            onApply = {
                                if (promoCode.uppercase() == "FASTBITE10")
                                    promoApplied = true
                            }
                        )
                    }
                }

                // ── Payment method
                item {
                    SectionCard(title = "💳 Payment Method") {
                        PaymentSelector(
                            selected = selectedPayment,
                            onSelect = { selectedPayment = it }
                        )
                    }
                }

                // ── Bill summary
                item {
                    SectionCard(title = "🧾 Bill Summary") {
                        BillSummary(
                            subtotal = subtotal,
                            deliveryFee = deliveryFee,
                            discount = discount,
                            total = total
                        )
                    }
                }
            }
        }

        // ── Place order button
        if (cartItems.isNotEmpty()) {
            PlaceOrderButton(
                total = total,
                onClick = { showConfirmDialog = true },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    // ── Address picker dialog
    if (showAddressDialog) {
        AddressPickerDialog(
            current = selectedAddress,
            onSelect = {
                selectedAddress = it
                showAddressDialog = false
            },
            onDismiss = { showAddressDialog = false }
        )
    }

    // ── Order confirm dialog
    if (showConfirmDialog) {
        OrderConfirmDialog(
            total = total,
            payment = selectedPayment,
            onConfirm = {
                showConfirmDialog = false
                onOrderPlaced()
            },
            onDismiss = { showConfirmDialog = false }
        )
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
fun CartTopBar(onBack: () -> Unit) {
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
            text = "Your Cart",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown
        )
    }
}

// ── Section card wrapper ──────────────────────────────────────────────────────

@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown
        )
        content()
    }
}

// ── Address row ───────────────────────────────────────────────────────────────

@Composable
fun AddressRow(address: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Cream)
            .clickable { onClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = address,
            fontSize = 13.sp,
            color = DeepBrown.copy(alpha = 0.8f),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Change",
            fontSize = 13.sp,
            color = WarmAmber,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ── Cart item row ─────────────────────────────────────────────────────────────

@Composable
fun CartItemRow(
    item: CartItemModel,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
    onDelete: () -> Unit
) {
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
                .size(54.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(WarmAmber.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = item.emoji, fontSize = 26.sp)
        }

        // Name + price
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = DeepBrown
            )
            Text(
                text = "Rs.${item.price} each",
                fontSize = 12.sp,
                color = DeepBrown.copy(alpha = 0.45f)
            )
        }

        // Qty + total
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "Rs.${item.price * item.qty}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = WarmAmber
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Delete if qty = 1
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(
                            if (item.qty == 1) Color(0xFFFFEBEB)
                            else DeepBrown.copy(alpha = 0.07f)
                        )
                        .clickable { onRemove() },
                    contentAlignment = Alignment.Center
                ) {
                    if (item.qty == 1) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove",
                            tint = Color(0xFFE53935),
                            modifier = Modifier.size(14.dp)
                        )
                    } else {
                        Text(
                            text = "−",
                            fontSize = 16.sp,
                            color = DeepBrown,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = "${item.qty}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBrown
                )
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(WarmAmber)
                        .clickable { onAdd() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Cream,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

// ── Promo code row ────────────────────────────────────────────────────────────

@Composable
fun PromoRow(
    code: String,
    onCodeChange: (String) -> Unit,
    applied: Boolean,
    onApply: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = code,
            onValueChange = onCodeChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    text = "Enter promo code",
                    fontSize = 13.sp,
                    color = DeepBrown.copy(alpha = 0.3f)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            enabled = !applied,
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
            onClick = onApply,
            enabled = !applied && code.isNotEmpty(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (applied) Color(0xFF4CAF50) else WarmAmber,
                contentColor = Cream,
                disabledContainerColor = DeepBrown.copy(alpha = 0.1f),
                disabledContentColor = DeepBrown.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = if (applied) "✓ Applied" else "Apply",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
    if (applied) {
        Text(
            text = "🎉 10% discount applied!",
            fontSize = 12.sp,
            color = Color(0xFF2E7D32),
            fontWeight = FontWeight.Medium
        )
    }
}

// ── Payment selector ──────────────────────────────────────────────────────────

@Composable
fun PaymentSelector(
    selected: PaymentMethod,
    onSelect: (PaymentMethod) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PaymentOption(
            emoji = "💵",
            label = "Cash on Delivery",
            isSelected = selected == PaymentMethod.COD,
            onClick = { onSelect(PaymentMethod.COD) },
            modifier = Modifier.weight(1f)
        )
        PaymentOption(
            emoji = "💳",
            label = "Online Payment",
            isSelected = selected == PaymentMethod.ONLINE,
            onClick = { onSelect(PaymentMethod.ONLINE) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PaymentOption(
    emoji: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isSelected) WarmAmber.copy(alpha = 0.1f) else Cream
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) WarmAmber else DeepBrown.copy(alpha = 0.1f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) DeepBrown else DeepBrown.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}

// ── Bill summary ──────────────────────────────────────────────────────────────

@Composable
fun BillSummary(subtotal: Int, deliveryFee: Int, discount: Int, total: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        BillRow("Subtotal", "Rs.$subtotal")
        BillRow("Delivery Fee", "Rs.$deliveryFee")
        if (discount > 0) {
            BillRow("Discount", "− Rs.$discount", valueColor = Color(0xFF2E7D32))
        }
        HorizontalDivider(
            color = DeepBrown.copy(alpha = 0.07f),
            thickness = 1.dp
        )
        BillRow(
            label = "Total",
            value = "Rs.$total",
            labelWeight = FontWeight.Bold,
            valueWeight = FontWeight.Bold,
            valueColor = WarmAmber,
            fontSize = 16
        )
    }
}

@Composable
fun BillRow(
    label: String,
    value: String,
    labelWeight: FontWeight = FontWeight.Normal,
    valueWeight: FontWeight = FontWeight.SemiBold,
    valueColor: Color = DeepBrown,
    fontSize: Int = 14
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = fontSize.sp,
            fontWeight = labelWeight,
            color = DeepBrown.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            fontSize = fontSize.sp,
            fontWeight = valueWeight,
            color = valueColor
        )
    }
}

// ── Place order button ────────────────────────────────────────────────────────

@Composable
fun PlaceOrderButton(
    total: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(WarmAmber)
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Place Order",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Cream
            )
            Text(
                text = "Rs.$total",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Cream
            )
        }
    }
}

// ── Empty cart ────────────────────────────────────────────────────────────────

@Composable
fun EmptyCart() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "🛒", fontSize = 64.sp)
        Text(
            text = "Your cart is empty",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown
        )
        Text(
            text = "Add items from a restaurant\nto get started",
            fontSize = 14.sp,
            color = DeepBrown.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

// ── Address picker dialog ─────────────────────────────────────────────────────

@Composable
fun AddressPickerDialog(
    current: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val addresses = listOf(
        "Home — Block 5, Gulshan-e-Iqbal",
        "Office — I.I. Chundrigar Road, Karachi",
        "Family — North Nazimabad, Block H"
    )

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
                text = "Select Address",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
            addresses.forEach { address ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (address == current) WarmAmber.copy(alpha = 0.08f)
                            else Cream
                        )
                        .border(
                            width = if (address == current) 2.dp else 1.dp,
                            color = if (address == current) WarmAmber
                            else DeepBrown.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelect(address) }
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "📍", fontSize = 18.sp)
                    Text(
                        text = address,
                        fontSize = 13.sp,
                        color = DeepBrown,
                        modifier = Modifier.weight(1f),
                        lineHeight = 18.sp
                    )
                    if (address == current) {
                        Text(text = "✓", color = WarmAmber, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ── Order confirm dialog ──────────────────────────────────────────────────────

@Composable
fun OrderConfirmDialog(
    total: Int,
    payment: PaymentMethod,
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
            Text(text = "🍔", fontSize = 48.sp)
            Text(
                text = "Confirm Order?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
            Text(
                text = "You'll be paying Rs.$total via ${
                    if (payment == PaymentMethod.COD) "Cash on Delivery"
                    else "Online Payment"
                }",
                fontSize = 13.sp,
                color = DeepBrown.copy(alpha = 0.55f),
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
                    containerColor = WarmAmber,
                    contentColor = Cream
                )
            ) {
                Text(
                    text = "Yes, Place Order",
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