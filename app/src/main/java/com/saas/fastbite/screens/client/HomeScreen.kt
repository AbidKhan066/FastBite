package com.saas.fastbite.screens.client

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.navigationBars
import com.saas.fastbite.ui.theme.Cream
import com.saas.fastbite.ui.theme.DeepBrown
import com.saas.fastbite.ui.theme.WarmAmber

// ── Data models ──────────────────────────────────────────────────────────────

data class Category(val emoji: String, val label: String)
data class Restaurant(
    val name: String,
    val cuisine: String,
    val rating: String,
    val deliveryTime: String,
    val emoji: String,
    val tag: String? = null
)
data class PromoCard(val title: String, val subtitle: String, val emoji: String, val color: Color)

val categories = listOf(
    Category("🍔", "Burgers"),
    Category("🍕", "Pizza"),
    Category("🍗", "Chicken"),
    Category("🌮", "Wraps"),
    Category("🍜", "Noodles"),
    Category("🧁", "Desserts"),
    Category("🥗", "Healthy"),
    Category("🥤", "Drinks"),
)

val featuredRestaurants = listOf(
    Restaurant("Burger Lab", "American · Burgers", "4.8", "20 min", "🍔", "Popular"),
    Restaurant("Pizza House", "Italian · Pizza", "4.6", "25 min", "🍕", "New"),
    Restaurant("Crispy Wings", "Chicken · Grills", "4.7", "15 min", "🍗"),
    Restaurant("Wrap Republic", "Mexican · Wraps", "4.5", "18 min", "🌮", "Deal"),
    Restaurant("Noodle Bar", "Chinese · Asian", "4.4", "30 min", "🍜"),
)

val nearbyRestaurants = listOf(
    Restaurant("The Grill House", "BBQ · Steaks", "4.9", "10 min", "🥩", "Top Rated"),
    Restaurant("Sweet Tooth", "Desserts · Cakes", "4.6", "12 min", "🧁"),
    Restaurant("Green Bowl", "Healthy · Salads", "4.5", "20 min", "🥗"),
    Restaurant("Drink Hub", "Juices · Shakes", "4.3", "8 min", "🥤"),
)

val promoCards = listOf(
    PromoCard("50% OFF", "First order discount", "🎉", Color(0xFFC08552)),
    PromoCard("Free Delivery", "Orders above Rs.500", "🛵", Color(0xFF8C5A3C)),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun ClientHomeScreen(onRestaurantClick: () -> Unit = {}) {
    var selectedCategory by remember { mutableStateOf("Burgers") }
    var searchQuery by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // ── Top bar
            item {
                TopBar()
            }

            // ── Search bar
            item {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
            }

            // ── Promo banners
            item {
                PromoBanners()
            }

            // ── Categories
            item {
                SectionTitle("Categories")
                CategoriesRow(
                    selected = selectedCategory,
                    onSelect = { selectedCategory = it }
                )
            }

            // ── Featured restaurants
            item {
                SectionTitle("Featured")
                FeaturedRestaurantsRow(
                    restaurants = featuredRestaurants,
                    onClick = onRestaurantClick
                )
            }

            // ── Nearby restaurants
            item {
                SectionTitle("Nearby You")
            }
            items(nearbyRestaurants) { restaurant ->
                NearbyRestaurantCard(
                    restaurant = restaurant,
                    onClick = onRestaurantClick
                )
            }
        }
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Good Morning 👋",
                fontSize = 13.sp,
                color = DeepBrown.copy(alpha = 0.5f)
            )
            Text(
                text = "What are you craving?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Notification bell
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = DeepBrown,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Avatar
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(WarmAmber),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "A", fontSize = 16.sp, color = Cream, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ── Search bar ────────────────────────────────────────────────────────────────

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        placeholder = {
            Text(
                text = "Search restaurants or food...",
                color = DeepBrown.copy(alpha = 0.35f),
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = WarmAmber,
                modifier = Modifier.size(20.dp)
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

// ── Promo banners ─────────────────────────────────────────────────────────────

@Composable
fun PromoBanners() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(promoCards) { promo ->
            Box(
                modifier = Modifier
                    .width(260.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(promo.color)
                    .clickable { }
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = promo.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Cream
                    )
                    Text(
                        text = promo.subtitle,
                        fontSize = 13.sp,
                        color = Cream.copy(alpha = 0.8f)
                    )
                }
                Text(
                    text = promo.emoji,
                    fontSize = 40.sp,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
}

// ── Section title ─────────────────────────────────────────────────────────────

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = DeepBrown,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    )
}

// ── Categories row ────────────────────────────────────────────────────────────

@Composable
fun CategoriesRow(selected: String, onSelect: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        items(categories) { category ->
            val isSelected = selected == category.label
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onSelect(category.label) }
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) WarmAmber
                            else Color.White
                        )
                        .border(
                            width = if (isSelected) 0.dp else 1.dp,
                            color = DeepBrown.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = category.emoji, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = category.label,
                    fontSize = 11.sp,
                    color = if (isSelected) WarmAmber else DeepBrown.copy(alpha = 0.6f),
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

// ── Featured restaurants row ──────────────────────────────────────────────────

@Composable
fun FeaturedRestaurantsRow(
    restaurants: List<Restaurant>,
    onClick: () -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        items(restaurants) { restaurant ->
            FeaturedRestaurantCard(restaurant = restaurant, onClick = onClick)
        }
    }
}

@Composable
fun FeaturedRestaurantCard(restaurant: Restaurant, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable { onClick() }
    ) {
        Column {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(WarmAmber.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = restaurant.emoji, fontSize = 48.sp)

                // Tag badge
                restaurant.tag?.let {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(WarmAmber)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = it,
                            fontSize = 10.sp,
                            color = Cream,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = restaurant.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBrown,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = restaurant.cuisine,
                    fontSize = 11.sp,
                    color = DeepBrown.copy(alpha = 0.5f),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = WarmAmber,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = restaurant.rating,
                            fontSize = 11.sp,
                            color = DeepBrown,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = restaurant.deliveryTime,
                        fontSize = 11.sp,
                        color = DeepBrown.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

// ── Nearby restaurant card ────────────────────────────────────────────────────

@Composable
fun NearbyRestaurantCard(restaurant: Restaurant, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Emoji box
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(WarmAmber.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = restaurant.emoji, fontSize = 30.sp)
        }

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = restaurant.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBrown
                )
                restaurant.tag?.let {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(WarmAmber.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = it,
                            fontSize = 10.sp,
                            color = WarmAmber,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            Text(
                text = restaurant.cuisine,
                fontSize = 12.sp,
                color = DeepBrown.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = WarmAmber,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = restaurant.rating,
                        fontSize = 12.sp,
                        color = DeepBrown,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "•",
                    fontSize = 12.sp,
                    color = DeepBrown.copy(alpha = 0.3f)
                )
                Text(
                    text = restaurant.deliveryTime,
                    fontSize = 12.sp,
                    color = DeepBrown.copy(alpha = 0.5f)
                )
            }
        }

        // Arrow
        Text(
            text = "›",
            fontSize = 22.sp,
            color = DeepBrown.copy(alpha = 0.25f),
            fontWeight = FontWeight.Light
        )
    }
}