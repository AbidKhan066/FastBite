package com.saas.fastbite.screens.client

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saas.fastbite.ui.theme.Cream
import com.saas.fastbite.ui.theme.DeepBrown
import com.saas.fastbite.ui.theme.WarmAmber

// ── Data models ───────────────────────────────────────────────────────────────

data class MenuItem(
    val name: String,
    val description: String,
    val price: Int,
    val emoji: String,
    val isPopular: Boolean = false,
    val category: String
)

data class ComboItem(
    val name: String,
    val items: String,
    val price: Int,
    val originalPrice: Int,
    val emoji: String
)

val menuCategories = listOf("All", "Burgers", "Combos", "Sides", "Drinks")

val menuItems = listOf(
    MenuItem("Classic Beef Burger", "Juicy beef patty, lettuce, tomato, special sauce", 350, "🍔", true, "Burgers"),
    MenuItem("Double Smash Burger", "Double patty, cheddar cheese, caramelised onions", 550, "🍔", true, "Burgers"),
    MenuItem("Crispy Chicken Burger", "Crispy fried chicken, coleslaw, mayo", 400, "🍗", false, "Burgers"),
    MenuItem("Zinger Stack", "Double zinger fillet, jalapeños, pepper mayo", 480, "🌶️", false, "Burgers"),
    MenuItem("Loaded Fries", "Crispy fries topped with cheese sauce & jalapeños", 250, "🍟", true, "Sides"),
    MenuItem("Onion Rings", "Golden crispy onion rings with dipping sauce", 180, "🧅", false, "Sides"),
    MenuItem("Chocolate Shake", "Thick creamy chocolate milkshake", 220, "🥤", false, "Drinks"),
    MenuItem("Fresh Lemonade", "Freshly squeezed lemonade with mint", 150, "🍋", false, "Drinks"),
)

val comboItems = listOf(
    ComboItem("Burger Combo", "Burger + Fries + Drink", 650, 780, "🍔"),
    ComboItem("Family Feast", "4 Burgers + 2 Fries + 4 Drinks", 2200, 2800, "🎉"),
    ComboItem("Snack Pack", "Loaded Fries + Onion Rings + Drink", 480, 600, "🍟"),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun RestaurantDetailScreen(
    onBack: () -> Unit = {},
    onCartClick: () -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val cartItems = remember { mutableStateMapOf<String, Int>() }
    val cartTotal = cartItems.entries.sumOf { (name, qty) ->
        val price = menuItems.find { it.name == name }?.price
            ?: comboItems.find { it.name == name }?.price ?: 0
        price * qty
    }
    val cartCount = cartItems.values.sum()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = if (cartCount > 0) 100.dp else 24.dp)
        ) {
            // ── Hero section
            item {
                RestaurantHero(onBack = onBack)
            }

            // ── Restaurant info
            item {
                RestaurantInfo()
            }

            // ── Category tabs
            item {
                CategoryTabs(
                    selected = selectedCategory,
                    onSelect = { selectedCategory = it }
                )
            }

            // ── Combos section
            if (selectedCategory == "All" || selectedCategory == "Combos") {
                item {
                    SectionTitle("🔥 Combo Deals")
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        items(comboItems) { combo ->
                            ComboCard(
                                combo = combo,
                                qty = cartItems[combo.name] ?: 0,
                                onAdd = { cartItems[combo.name] = (cartItems[combo.name] ?: 0) + 1 },
                                onRemove = {
                                    val current = cartItems[combo.name] ?: 0
                                    if (current > 1) cartItems[combo.name] = current - 1
                                    else cartItems.remove(combo.name)
                                }
                            )
                        }
                    }
                }
            }

            // ── Menu items
            if (selectedCategory == "All" || selectedCategory != "Combos") {
                val filtered = if (selectedCategory == "All") menuItems
                else menuItems.filter { it.category == selectedCategory }

                item {
                    SectionTitle("Menu")
                }
                items(filtered) { item ->
                    MenuItemCard(
                        item = item,
                        qty = cartItems[item.name] ?: 0,
                        onAdd = { cartItems[item.name] = (cartItems[item.name] ?: 0) + 1 },
                        onRemove = {
                            val current = cartItems[item.name] ?: 0
                            if (current > 1) cartItems[item.name] = current - 1
                            else cartItems.remove(item.name)
                        }
                    )
                }
            }
        }

        // ── Floating cart bar
        if (cartCount > 0) {
            CartBar(
                count = cartCount,
                total = cartTotal,
                onClick = onCartClick,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

// ── Hero ──────────────────────────────────────────────────────────────────────

@Composable
fun RestaurantHero(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(WarmAmber.copy(alpha = 0.15f))
    ) {
        // Big emoji as hero image
        Text(
            text = "🍔",
            fontSize = 96.sp,
            modifier = Modifier.align(Alignment.Center)
        )

        // Top actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Back button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f))
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

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favourite",
                        tint = DeepBrown,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = DeepBrown,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// ── Restaurant info ───────────────────────────────────────────────────────────

@Composable
fun RestaurantInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = "Burger Lab",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBrown
                )
                Text(
                    text = "American · Burgers · Fast Food",
                    fontSize = 13.sp,
                    color = DeepBrown.copy(alpha = 0.5f)
                )
            }

            // Open badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF4CAF50).copy(alpha = 0.12f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Open",
                    fontSize = 12.sp,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Stats row
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatChip(icon = "⭐", value = "4.8", label = "Rating")
            StatChip(icon = "🕐", value = "20 min", label = "Delivery")
            StatChip(icon = "🛵", value = "Free", label = "Delivery fee")
            StatChip(icon = "📦", value = "Rs.200", label = "Min. order")
        }
    }
}

@Composable
fun StatChip(icon: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 16.sp)
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = DeepBrown.copy(alpha = 0.4f)
        )
    }
}

// ── Category tabs ─────────────────────────────────────────────────────────────

@Composable
fun CategoryTabs(selected: String, onSelect: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 12.dp)
    ) {
        items(menuCategories) { category ->
            val isSelected = selected == category
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) WarmAmber else Cream
                    )
                    .border(
                        width = if (isSelected) 0.dp else 1.dp,
                        color = DeepBrown.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onSelect(category) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = category,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) Cream else DeepBrown.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// ── Combo card ────────────────────────────────────────────────────────────────

@Composable
fun ComboCard(
    combo: ComboItem,
    qty: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = WarmAmber.copy(alpha = 0.2f),
                shape = RoundedCornerShape(18.dp)
            )
    ) {
        Column {
            // Emoji hero
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(WarmAmber.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = combo.emoji, fontSize = 44.sp)
                // Savings badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(WarmAmber)
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Save Rs.${combo.originalPrice - combo.price}",
                        fontSize = 9.sp,
                        color = Cream,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = combo.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBrown
                )
                Text(
                    text = combo.items,
                    fontSize = 11.sp,
                    color = DeepBrown.copy(alpha = 0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Rs.${combo.price}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepBrown
                        )
                        Text(
                            text = "Rs.${combo.originalPrice}",
                            fontSize = 10.sp,
                            color = DeepBrown.copy(alpha = 0.35f)
                        )
                    }
                    QuantityControl(qty = qty, onAdd = onAdd, onRemove = onRemove)
                }
            }
        }
    }
}

// ── Menu item card ────────────────────────────────────────────────────────────

@Composable
fun MenuItemCard(
    item: MenuItem,
    qty: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Emoji
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(WarmAmber.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = item.emoji, fontSize = 32.sp)
            if (item.isPopular) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(WarmAmber)
                        .padding(vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Popular",
                        fontSize = 8.sp,
                        color = Cream,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.description,
                fontSize = 11.sp,
                color = DeepBrown.copy(alpha = 0.5f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Rs.${item.price}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = WarmAmber
            )
        }

        // Quantity control
        QuantityControl(qty = qty, onAdd = onAdd, onRemove = onRemove)
    }
}

// ── Quantity control ──────────────────────────────────────────────────────────

@Composable
fun QuantityControl(qty: Int, onAdd: () -> Unit, onRemove: () -> Unit) {
    if (qty == 0) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(WarmAmber)
                .clickable { onAdd() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Cream,
                modifier = Modifier.size(18.dp)
            )
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DeepBrown.copy(alpha = 0.08f))
                    .clickable { onRemove() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "−",
                    fontSize = 18.sp,
                    color = DeepBrown,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "$qty",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown,
                modifier = Modifier.widthIn(min = 16.dp)
            )
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(WarmAmber)
                    .clickable { onAdd() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    fontSize = 16.sp,
                    color = Cream,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ── Cart bar ──────────────────────────────────────────────────────────────────

@Composable
fun CartBar(
    count: Int,
    total: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(DeepBrown)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(WarmAmber),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$count",
                        fontSize = 13.sp,
                        color = Cream,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "View Cart",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Cream
                )
            }
            Text(
                text = "Rs.$total",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = WarmAmber
            )
        }
    }
}