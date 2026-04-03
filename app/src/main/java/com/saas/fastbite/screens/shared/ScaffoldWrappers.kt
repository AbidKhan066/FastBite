package com.saas.fastbite.screens.shared

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.saas.fastbite.screens.client.ClientHomeScreen
import com.saas.fastbite.screens.client.ClientProfileScreen
import com.saas.fastbite.screens.client.OrderHistoryScreen
import com.saas.fastbite.screens.client.RestaurantDetailScreen
import com.saas.fastbite.screens.restaurant.MenuManagerScreen
import com.saas.fastbite.screens.restaurant.RestaurantAnalyticsScreen
import com.saas.fastbite.screens.restaurant.RestaurantDashboardScreen
import com.saas.fastbite.screens.restaurant.RestaurantProfileScreen
import com.saas.fastbite.screens.restaurant.TokenWalletScreen
import com.saas.fastbite.screens.shared.ChatScreen
import com.saas.fastbite.screens.rider.RiderDashboardScreen
import com.saas.fastbite.screens.rider.RiderHistoryScreen
import com.saas.fastbite.screens.rider.RiderOrderDetailScreen

// ── Client scaffold ───────────────────────────────────────────────────────────

@Composable
fun ClientScaffold(navController: NavHostController) {
    var currentRoute by remember { mutableStateOf("client_home") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Content
        when (currentRoute) {
            "client_home" -> ClientHomeScreen(
                onRestaurantClick = {
                    navController.navigate("restaurant_detail")
                }
            )
            "restaurant_detail" -> RestaurantDetailScreen(
                onBack = { currentRoute = "client_home" },
                onCartClick = { navController.navigate("cart") }
            )
            "order_tracking" -> OrderHistoryScreen(
                onBack = { currentRoute = "client_home" },
                onReorder = { navController.navigate("restaurant_detail") },
                onTrack = { navController.navigate("order_tracking") }
            )
            "client_profile" -> ClientProfileScreen(
                onBack = { currentRoute = "client_home" },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Bottom nav
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BottomNavBar(
                items = clientNavItems,
                currentRoute = currentRoute,
                onItemClick = { route -> currentRoute = route }
            )
        }
    }
}

// ── Restaurant scaffold ───────────────────────────────────────────────────────

@Composable
fun RestaurantScaffold(navController: NavHostController) {
    var currentRoute by remember { mutableStateOf("restaurant_dashboard") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Content
        when (currentRoute) {
            "restaurant_dashboard" -> RestaurantDashboardScreen(
                onLiveOrdersClick = { currentRoute = "live_orders" },
                onMenuClick = { currentRoute = "menu_manager" },
                onTokensClick = { currentRoute = "token_wallet" },
                onAnalyticsClick = { navController.navigate("restaurant_analytics") },
                onChatClick = { currentRoute = "restaurant_chat" }
            )
            "live_orders" -> RestaurantDashboardScreen(
                onLiveOrdersClick = {},
                onMenuClick = { currentRoute = "menu_manager" },
                onTokensClick = { currentRoute = "token_wallet" },
                onAnalyticsClick = { navController.navigate("restaurant_analytics") },
                onChatClick = { currentRoute = "restaurant_chat" }
            )
            "menu_manager" -> MenuManagerScreen(
                onBack = { currentRoute = "restaurant_dashboard" }
            )
            "token_wallet" -> TokenWalletScreen(
                onBack = { currentRoute = "restaurant_dashboard" }
            )
            "restaurant_chat" -> ChatScreen(
                onBack = { currentRoute = "restaurant_dashboard" },
                chatWithName = "Rider · Ali Raza",
                chatWithEmoji = "🏍️",
                orderId = "FB041"
            )
            "restaurant_profile" -> RestaurantProfileScreen(
                onBack = { currentRoute = "restaurant_dashboard" },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Bottom nav
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BottomNavBar(
                items = restaurantNavItems,
                currentRoute = currentRoute,
                onItemClick = { route -> currentRoute = route }
            )
        }
    }
}

// ── Rider scaffold ────────────────────────────────────────────────────────────

@Composable
fun RiderScaffold(navController: NavHostController) {
    var currentRoute by remember { mutableStateOf("rider_dashboard") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Content
        when (currentRoute) {
            "rider_dashboard" -> RiderDashboardScreen(
                onOrderClick = { currentRoute = "rider_order_detail" },
                onHistoryClick = { currentRoute = "rider_history" },
                onChatClick = { currentRoute = "rider_chat" }
            )
            "rider_order_detail" -> RiderOrderDetailScreen(
                onBack = { currentRoute = "rider_dashboard" },
                onChatWithCustomer = { currentRoute = "rider_chat" },
                onChatWithRestaurant = { currentRoute = "rider_chat" }
            )
            "rider_chat" -> ChatScreen(
                onBack = { currentRoute = "rider_dashboard" },
                chatWithName = "Ahmed Khan",
                chatWithEmoji = "👤",
                orderId = "FB041"
            )
            "rider_history" -> RiderHistoryScreen(
                onBack = { currentRoute = "rider_dashboard" }
            )
            "rider_profile" -> ChatScreen(
                onBack = { currentRoute = "rider_dashboard" },
                chatWithName = "Support",
                chatWithEmoji = "🆘",
                orderId = ""
            )
        }

        // Bottom nav
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BottomNavBar(
                items = riderNavItems,
                currentRoute = currentRoute,
                onItemClick = { route -> currentRoute = route }
            )
        }
    }
}