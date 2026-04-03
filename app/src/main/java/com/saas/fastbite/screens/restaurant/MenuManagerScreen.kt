package com.saas.fastbite.screens.restaurant

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.saas.fastbite.ui.theme.Cream
import com.saas.fastbite.ui.theme.DeepBrown
import com.saas.fastbite.ui.theme.WarmAmber
import com.saas.fastbite.screens.shared.AddDialogField

// ── Data models ───────────────────────────────────────────────────────────────

data class MenuItemEntry(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val category: String,
    val emoji: String,
    var isAvailable: Boolean = true
)

data class ComboEntry(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val originalPrice: Int,
    val emoji: String,
    var isAvailable: Boolean = true
)

val menuCategories2 = listOf("All", "Burgers", "Sides", "Drinks", "Desserts")

val sampleMenuItems = mutableListOf(
    MenuItemEntry("m1", "Classic Beef Burger",   "Juicy beef patty, lettuce, tomato",     350, "Burgers",  "🍔", true),
    MenuItemEntry("m2", "Double Smash Burger",   "Double patty, cheddar, caramelised onions", 550, "Burgers", "🍔", true),
    MenuItemEntry("m3", "Crispy Chicken Burger", "Crispy fillet, coleslaw, mayo",         400, "Burgers",  "🍗", true),
    MenuItemEntry("m4", "Zinger Stack",          "Double zinger, jalapeños, pepper mayo", 480, "Burgers",  "🌶️", false),
    MenuItemEntry("m5", "Loaded Fries",          "Cheese sauce & jalapeños",              250, "Sides",    "🍟", true),
    MenuItemEntry("m6", "Onion Rings",           "Golden crispy with dipping sauce",      180, "Sides",    "🧅", true),
    MenuItemEntry("m7", "Chocolate Shake",       "Thick creamy milkshake",                220, "Drinks",   "🥤", true),
    MenuItemEntry("m8", "Fresh Lemonade",        "Freshly squeezed with mint",            150, "Drinks",   "🍋", true),
)

val sampleCombos = mutableListOf(
    ComboEntry("c1", "Burger Combo",   "Burger + Fries + Drink",          650, 780,  "🍔", true),
    ComboEntry("c2", "Family Feast",   "4 Burgers + 2 Fries + 4 Drinks", 2200, 2800, "🎉", true),
    ComboEntry("c3", "Snack Pack",     "Loaded Fries + Rings + Drink",    480, 600,  "🍟", false),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun MenuManagerScreen(onBack: () -> Unit = {}) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedCategory by remember { mutableStateOf("All") }
    val menuItems = remember { mutableStateListOf(*sampleMenuItems.toTypedArray()) }
    val combos = remember { mutableStateListOf(*sampleCombos.toTypedArray()) }
    var showAddItemDialog by remember { mutableStateOf(false) }
    var showAddComboDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<MenuItemEntry?>(null) }
    var comboToDelete by remember { mutableStateOf<ComboEntry?>(null) }

    val filteredItems = if (selectedCategory == "All") menuItems
    else menuItems.filter { it.category == selectedCategory }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // ── Top bar
            item { MenuManagerTopBar(onBack = onBack) }

            // ── Summary chips
            item {
                MenuSummaryChips(
                    totalItems = menuItems.size,
                    availableItems = menuItems.count { it.isAvailable },
                    totalCombos = combos.size
                )
            }

            // ── Tabs
            item {
                MenuTabs(
                    selected = selectedTab,
                    onSelect = { selectedTab = it }
                )
            }

            if (selectedTab == 0) {
                // ── Category filter
                item {
                    CategoryFilterRow(
                        categories = menuCategories2,
                        selected = selectedCategory,
                        onSelect = { selectedCategory = it }
                    )
                }

                // ── Menu items
                item {
                    SectionHeader(
                        title = "Menu Items",
                        count = filteredItems.size
                    )
                }
                items(filteredItems, key = { it.id }) { item ->
                    MenuItemManageCard(
                        item = item,
                        onToggleAvailability = {
                            val index = menuItems.indexOfFirst { it.id == item.id }
                            if (index != -1) {
                                menuItems[index] = menuItems[index].copy(
                                    isAvailable = !menuItems[index].isAvailable
                                )
                            }
                        },
                        onDelete = { itemToDelete = item }
                    )
                }
            } else {
                // ── Combos
                item {
                    SectionHeader(
                        title = "Combo Deals",
                        count = combos.size
                    )
                }
                items(combos, key = { it.id }) { combo ->
                    ComboManageCard(
                        combo = combo,
                        onToggleAvailability = {
                            val index = combos.indexOfFirst { it.id == combo.id }
                            if (index != -1) {
                                combos[index] = combos[index].copy(
                                    isAvailable = !combos[index].isAvailable
                                )
                            }
                        },
                        onDelete = { comboToDelete = combo }
                    )
                }
            }
        }

        // ── FAB
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .size(58.dp)
                .clip(CircleShape)
                .background(WarmAmber)
                .clickable {
                    if (selectedTab == 0) showAddItemDialog = true
                    else showAddComboDialog = true
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Cream,
                modifier = Modifier.size(28.dp)
            )
        }
    }

    // ── Add item dialog
    if (showAddItemDialog) {
        AddMenuItemDialog(
            onAdd = { item ->
                menuItems.add(item)
                showAddItemDialog = false
            },
            onDismiss = { showAddItemDialog = false }
        )
    }

    // ── Add combo dialog
    if (showAddComboDialog) {
        AddComboDialog(
            onAdd = { combo ->
                combos.add(combo)
                showAddComboDialog = false
            },
            onDismiss = { showAddComboDialog = false }
        )
    }

    // ── Delete item confirm
    itemToDelete?.let { item ->
        DeleteConfirmDialog(
            name = item.name,
            onConfirm = {
                menuItems.removeIf { it.id == item.id }
                itemToDelete = null
            },
            onDismiss = { itemToDelete = null }
        )
    }

    // ── Delete combo confirm
    comboToDelete?.let { combo ->
        DeleteConfirmDialog(
            name = combo.name,
            onConfirm = {
                combos.removeIf { it.id == combo.id }
                comboToDelete = null
            },
            onDismiss = { comboToDelete = null }
        )
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
fun MenuManagerTopBar(onBack: () -> Unit) {
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
                text = "Menu Manager",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = DeepBrown,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ── Summary chips ─────────────────────────────────────────────────────────────

@Composable
fun MenuSummaryChips(totalItems: Int, availableItems: Int, totalCombos: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SummaryChip("🍔", "$totalItems Items", Color(0xFFE3F2FD), Modifier.weight(1f))
        SummaryChip("✅", "$availableItems Active", Color(0xFFE8F5E9), Modifier.weight(1f))
        SummaryChip("🎁", "$totalCombos Combos", Color(0xFFFFF3E0), Modifier.weight(1f))
    }
}

@Composable
fun SummaryChip(
    emoji: String,
    label: String,
    background: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = emoji, fontSize = 14.sp)
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = DeepBrown
        )
    }
}

// ── Tabs ──────────────────────────────────────────────────────────────────────

@Composable
fun MenuTabs(selected: Int, onSelect: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(4.dp)
    ) {
        listOf("Menu Items", "Combos").forEachIndexed { index, label ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (selected == index) WarmAmber else Color.Transparent)
                    .clickable { onSelect(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = if (selected == index) FontWeight.Bold else FontWeight.Normal,
                    color = if (selected == index) Cream else DeepBrown.copy(alpha = 0.5f)
                )
            }
        }
    }
}

// ── Category filter ───────────────────────────────────────────────────────────

@Composable
fun CategoryFilterRow(
    categories: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = selected == category
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) WarmAmber else Color.White)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) WarmAmber else DeepBrown.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onSelect(category) }
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Text(
                    text = category,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) Cream else DeepBrown.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// ── Section header ────────────────────────────────────────────────────────────

@Composable
fun SectionHeader(title: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown
        )
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(WarmAmber.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = "$count",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = WarmAmber
            )
        }
    }
}

// ── Menu item manage card ─────────────────────────────────────────────────────

@Composable
fun MenuItemManageCard(
    item: MenuItemEntry,
    onToggleAvailability: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp))
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
                .background(
                    if (item.isAvailable) WarmAmber.copy(alpha = 0.1f)
                    else DeepBrown.copy(alpha = 0.05f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.emoji,
                fontSize = 26.sp,
                color = if (item.isAvailable) Color.Unspecified
                else Color.Gray
            )
        }

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (item.isAvailable) DeepBrown
                else DeepBrown.copy(alpha = 0.4f)
            )
            Text(
                text = item.category,
                fontSize = 11.sp,
                color = DeepBrown.copy(alpha = 0.4f)
            )
            Text(
                text = "Rs.${item.price}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (item.isAvailable) WarmAmber
                else DeepBrown.copy(alpha = 0.3f)
            )
        }

        // Actions
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Availability switch
            Switch(
                checked = item.isAvailable,
                onCheckedChange = { onToggleAvailability() },
                modifier = Modifier.height(24.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = WarmAmber,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = DeepBrown.copy(alpha = 0.2f)
                )
            )

            // Delete
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFEBEB))
                    .clickable { onDelete() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

// ── Combo manage card ─────────────────────────────────────────────────────────

@Composable
fun ComboManageCard(
    combo: ComboEntry,
    onToggleAvailability: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (combo.isAvailable) WarmAmber.copy(alpha = 0.1f)
                    else DeepBrown.copy(alpha = 0.05f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = combo.emoji, fontSize = 26.sp)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = combo.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (combo.isAvailable) DeepBrown
                else DeepBrown.copy(alpha = 0.4f)
            )
            Text(
                text = combo.description,
                fontSize = 11.sp,
                color = DeepBrown.copy(alpha = 0.4f),
                lineHeight = 15.sp
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rs.${combo.price}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (combo.isAvailable) WarmAmber
                    else DeepBrown.copy(alpha = 0.3f)
                )
                Text(
                    text = "Rs.${combo.originalPrice}",
                    fontSize = 11.sp,
                    color = DeepBrown.copy(alpha = 0.3f)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Switch(
                checked = combo.isAvailable,
                onCheckedChange = { onToggleAvailability() },
                modifier = Modifier.height(24.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = WarmAmber,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = DeepBrown.copy(alpha = 0.2f)
                )
            )
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFEBEB))
                    .clickable { onDelete() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

// ── Add menu item dialog ──────────────────────────────────────────────────────

@Composable
fun AddMenuItemDialog(
    onAdd: (MenuItemEntry) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Burgers") }
    var emoji by remember { mutableStateOf("🍔") }

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
                text = "Add Menu Item",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )

            // Emoji picker row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("🍔", "🍗", "🍕", "🌮", "🍟", "🥤", "🧁", "🌶️").forEach { e ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (emoji == e) WarmAmber.copy(alpha = 0.2f)
                                else Cream
                            )
                            .border(
                                1.dp,
                                if (emoji == e) WarmAmber else Color.Transparent,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { emoji = e },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = e, fontSize = 18.sp)
                    }
                }
            }

            AddDialogField("Item Name", name, { name = it })
            AddDialogField("Description", description, { description = it })
            AddDialogField("Price (Rs.)", price, { price = it }, KeyboardType.Number)

            // Category selector
            Text(
                text = "Category",
                fontSize = 13.sp,
                color = DeepBrown.copy(alpha = 0.5f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Burgers", "Sides", "Drinks", "Desserts").forEach { cat ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (category == cat) WarmAmber
                                else Cream
                            )
                            .clickable { category = cat }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = cat,
                            fontSize = 11.sp,
                            fontWeight = if (category == cat) FontWeight.Bold
                            else FontWeight.Normal,
                            color = if (category == cat) Cream
                            else DeepBrown.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = DeepBrown
                    )
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        if (name.isNotEmpty() && price.isNotEmpty()) {
                            onAdd(
                                MenuItemEntry(
                                    id = "m${System.currentTimeMillis()}",
                                    name = name,
                                    description = description,
                                    price = price.toIntOrNull() ?: 0,
                                    category = category,
                                    emoji = emoji,
                                    isAvailable = true
                                )
                            )
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WarmAmber,
                        contentColor = Cream
                    ),
                    enabled = name.isNotEmpty() && price.isNotEmpty()
                ) {
                    Text("Add Item", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// ── Add combo dialog ──────────────────────────────────────────────────────────

@Composable
fun AddComboDialog(
    onAdd: (ComboEntry) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var originalPrice by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("🎉") }

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
                text = "Add Combo Deal",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("🎉", "🍔", "🍟", "🍗", "🥤", "🎁", "🌮", "🏆").forEach { e ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (emoji == e) WarmAmber.copy(alpha = 0.2f)
                                else Cream
                            )
                            .border(
                                1.dp,
                                if (emoji == e) WarmAmber else Color.Transparent,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { emoji = e },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = e, fontSize = 18.sp)
                    }
                }
            }

            AddDialogField("Combo Name", name, { name = it })
            AddDialogField("Items Included", description, { description = it })
            AddDialogField("Combo Price (Rs.)", price, { price = it }, KeyboardType.Number)
            AddDialogField("Original Price (Rs.)", originalPrice, { originalPrice = it }, KeyboardType.Number)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepBrown)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        if (name.isNotEmpty() && price.isNotEmpty()) {
                            onAdd(
                                ComboEntry(
                                    id = "c${System.currentTimeMillis()}",
                                    name = name,
                                    description = description,
                                    price = price.toIntOrNull() ?: 0,
                                    originalPrice = originalPrice.toIntOrNull() ?: 0,
                                    emoji = emoji,
                                    isAvailable = true
                                )
                            )
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WarmAmber,
                        contentColor = Cream
                    ),
                    enabled = name.isNotEmpty() && price.isNotEmpty()
                ) {
                    Text("Add Combo", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}


// ── Delete confirm dialog ─────────────────────────────────────────────────────

@Composable
fun DeleteConfirmDialog(
    name: String,
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
            Text(text = "🗑️", fontSize = 40.sp)
            Text(
                text = "Delete Item?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
            Text(
                text = "\"$name\" will be permanently\nremoved from your menu.",
                fontSize = 13.sp,
                color = DeepBrown.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepBrown)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935),
                        contentColor = Color.White
                    )
                ) {
                    Text("Delete", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}