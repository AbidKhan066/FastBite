package com.saas.fastbite.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.saas.fastbite.screens.auth.LoginScreen
import com.saas.fastbite.screens.auth.RoleSelectorScreen
import com.saas.fastbite.screens.auth.UserRole
import com.saas.fastbite.screens.client.CartScreen
import com.saas.fastbite.screens.client.OrderTrackingScreen
import com.saas.fastbite.screens.client.RestaurantDetailScreen
import com.saas.fastbite.screens.onboarding.OnboardingScreen
import com.saas.fastbite.screens.restaurant.MenuManagerScreen
import com.saas.fastbite.screens.restaurant.TokenWalletScreen
import com.saas.fastbite.screens.rider.RiderOrderDetailScreen
import com.saas.fastbite.screens.restaurant.RestaurantAnalyticsScreen
import com.saas.fastbite.screens.shared.ChatScreen
import com.saas.fastbite.screens.rider.RiderHistoryScreen
import com.saas.fastbite.screens.client.OrderHistoryScreen
import com.saas.fastbite.screens.client.ClientProfileScreen
import com.saas.fastbite.screens.shared.ClientScaffold
import com.saas.fastbite.screens.shared.RestaurantScaffold
import com.saas.fastbite.screens.shared.RiderScaffold
// ── All routes ────────────────────────────────────────────────────────────────

sealed class Screen(val route: String) {
    // Shared
    object Onboarding           : Screen("onboarding")
    object Login                : Screen("login")
    object RoleSelector         : Screen("role_selector")

    // Client
    object ClientHome           : Screen("client_home")
    object RestaurantDetail     : Screen("restaurant_detail")
    object Cart                 : Screen("cart")
    object OrderTracking        : Screen("order_tracking")
    object OrderHistory         : Screen("order_history")
    object ClientProfile        : Screen("client_profile")

    // Restaurant
    object RestaurantDashboard  : Screen("restaurant_dashboard")
    object LiveOrders           : Screen("live_orders")
    object MenuManager          : Screen("menu_manager")
    object TokenWallet          : Screen("token_wallet")
    object RestaurantAnalytics  : Screen("restaurant_analytics")
    object RestaurantChat       : Screen("restaurant_chat")
    object RestaurantProfile    : Screen("restaurant_profile")

    // Rider
    object RiderDashboard       : Screen("rider_dashboard")
    object RiderOrderDetail     : Screen("rider_order_detail")
    object RiderChat            : Screen("rider_chat")
    object RiderHistory         : Screen("rider_history")
    object RiderProfile         : Screen("rider_profile")
}

// ── NavGraph ──────────────────────────────────────────────────────────────────

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route
    ) {

        // ── Shared ────────────────────────────────────────────────────────────

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.RoleSelector.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.RoleSelector.route) {
            RoleSelectorScreen(
                onRoleSelected = { role ->
                    when (role) {
                        UserRole.CLIENT -> {
                            navController.navigate(Screen.ClientHome.route) {
                                popUpTo(Screen.RoleSelector.route) { inclusive = true }
                            }
                        }
                        UserRole.RESTAURANT -> {
                            navController.navigate(Screen.RestaurantDashboard.route) {
                                popUpTo(Screen.RoleSelector.route) { inclusive = true }
                            }
                        }
                        UserRole.RIDER -> {
                            navController.navigate(Screen.RiderDashboard.route) {
                                popUpTo(Screen.RoleSelector.route) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        // ── Client ────────────────────────────────────────────────────────────

        composable(Screen.ClientHome.route) {
            ClientScaffold(navController = navController)
        }

        composable(Screen.RestaurantDetail.route) {
            RestaurantDetailScreen(
                onBack = { navController.popBackStack() },
                onCartClick = {
                    navController.navigate(Screen.Cart.route)
                }
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                onBack = { navController.popBackStack() },
                onOrderPlaced = {
                    navController.navigate(Screen.OrderTracking.route) {
                        popUpTo(Screen.Cart.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.OrderTracking.route) {
            OrderTrackingScreen(
                onBack = { navController.popBackStack() },
                onChatClick = { }
            )
        }

        composable(Screen.OrderHistory.route) {
            OrderHistoryScreen(
                onBack = { navController.popBackStack() },
                onReorder = {
                    navController.navigate(Screen.ClientHome.route)
                },
                onTrack = {
                    navController.navigate(Screen.OrderTracking.route)
                }
            )
        }

        composable(Screen.ClientProfile.route) {
            ClientProfileScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ── Restaurant ────────────────────────────────────────────────────────

        composable(Screen.RestaurantDashboard.route) {
            RestaurantScaffold(navController = navController)
        }

        composable(Screen.LiveOrders.route) {
            // LiveOrdersScreen — coming soon
        }

        composable(Screen.MenuManager.route) {
            MenuManagerScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.TokenWallet.route) {
            TokenWalletScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.RestaurantAnalytics.route) {
            RestaurantAnalyticsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.RestaurantChat.route) {
            ChatScreen(
                onBack = { navController.popBackStack() },
                chatWithName = "Rider · Ali Raza",
                chatWithEmoji = "🏍️",
                orderId = "FB041"
            )
        }

        composable(Screen.RestaurantProfile.route) {
            // RestaurantProfileScreen — coming soon
        }

        // ── Rider ─────────────────────────────────────────────────────────────

        composable(Screen.RiderDashboard.route) {
            RiderScaffold(navController = navController)
        }

        composable(Screen.RiderOrderDetail.route) {
            RiderOrderDetailScreen(
                onBack = { navController.popBackStack() },
                onChatWithCustomer = {
                    navController.navigate(Screen.RiderChat.route)
                },
                onChatWithRestaurant = {
                    navController.navigate(Screen.RiderChat.route)
                }
            )
        }

        composable(Screen.RiderChat.route) {
            ChatScreen(
                onBack = { navController.popBackStack() },
                chatWithName = "Ahmed Khan",
                chatWithEmoji = "👤",
                orderId = "FB041"
            )
        }

        composable(Screen.RiderHistory.route) {
            RiderHistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.RiderProfile.route) {
            // RiderProfileScreen — coming soon
        }
    }
}