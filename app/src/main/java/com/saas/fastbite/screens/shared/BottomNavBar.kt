package com.saas.fastbite.screens.shared

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saas.fastbite.ui.theme.Cream
import com.saas.fastbite.ui.theme.DeepBrown
import com.saas.fastbite.ui.theme.WarmAmber
import com.saas.fastbite.screens.restaurant.RestaurantProfileScreen
// ── Data models ───────────────────────────────────────────────────────────────

data class NavItem(
    val route: String,
    val emoji: String,
    val label: String,
    val badge: Int = 0
)

// ── Client nav items ──────────────────────────────────────────────────────────

val clientNavItems = listOf(
    NavItem("client_home",       "🏠", "Home"),
    NavItem("restaurant_detail", "🍔", "Explore"),
    NavItem("order_tracking",    "📦", "Orders",  badge = 1),
    NavItem("client_profile",    "👤", "Profile"),
)

// ── Restaurant nav items ──────────────────────────────────────────────────────

val restaurantNavItems = listOf(
    NavItem("restaurant_dashboard", "📊", "Dashboard"),
    NavItem("live_orders",          "🔴", "Orders",  badge = 4),
    NavItem("menu_manager",         "🍔", "Menu"),
    NavItem("token_wallet",         "🪙", "Tokens"),
    NavItem("restaurant_profile",   "👤", "Profile"),
)

// ── Rider nav items ───────────────────────────────────────────────────────────

val riderNavItems = listOf(
    NavItem("rider_dashboard",    "🏠",  "Home"),
    NavItem("rider_order_detail", "📋",  "Current",   badge = 2),
    NavItem("rider_chat",         "💬",  "Chat"),
    NavItem("rider_history",      "📜",  "History"),
    NavItem("rider_profile",      "👤",  "Profile"),
)

// ── Bottom nav bar ────────────────────────────────────────────────────────────

@Composable
fun BottomNavBar(
    items: List<NavItem>,
    currentRoute: String,
    onItemClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                NavBarItem(
                    item = item,
                    isSelected = currentRoute == item.route,
                    onClick = { onItemClick(item.route) }
                )
            }
        }
    }
}

// ── Nav bar item ──────────────────────────────────────────────────────────────

@Composable
fun NavBarItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "navScale"
    )

    val bgColor by animateColorAsState(
        targetValue = if (isSelected) WarmAmber.copy(alpha = 0.12f)
        else Color.Transparent,
        animationSpec = tween(250),
        label = "navBg"
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            // Emoji + badge
            Box(contentAlignment = Alignment.TopEnd) {
                Text(
                    text = item.emoji,
                    fontSize = 22.sp
                )
                if (item.badge > 0) {
                    Box(
                        modifier = Modifier
                            .offset(x = 6.dp, y = (-4).dp)
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE53935)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (item.badge > 9) "9+" else "${item.badge}",
                            fontSize = 8.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Label
            Text(
                text = item.label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) WarmAmber else DeepBrown.copy(alpha = 0.45f)
            )

            // Active dot
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) WarmAmber else Color.Transparent
                    )
            )
        }
    }
}
