package com.saas.fastbite.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class UserRole {
    @SerialName("client") CLIENT,
    @SerialName("restaurant_owner") RESTAURANT_OWNER,
    @SerialName("rider") RIDER
}

@Serializable
data class Profile(
    val id: String,
    @SerialName("full_name") val fullName: String? = null,
    val role: UserRole = UserRole.CLIENT,
    @SerialName("created_at") val createdAt: String? = null
)

@Serializable
data class Restaurant(
    val id: String? = null,
    @SerialName("owner_id") val ownerId: String,
    val name: String,
    val address: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerialName("is_open") val isOpen: Boolean = true,
    @SerialName("token_balance") val tokenBalance: Int = 0,
    @SerialName("logo_url") val logoUrl: String? = null
)

@Serializable
enum class OrderStatus {
    @SerialName("pending") PENDING,
    @SerialName("confirmed") CONFIRMED,
    @SerialName("preparing") PREPARING,
    @SerialName("out_for_delivery") OUT_FOR_DELIVERY,
    @SerialName("delivered") DELIVERED,
    @SerialName("cancelled") CANCELLED
}

@Serializable
data class Order(
    val id: String? = null,
    @SerialName("client_id") val clientId: String,
    @SerialName("restaurant_id") val restaurantId: String,
    @SerialName("rider_id") val riderId: String? = null,
    val status: OrderStatus = OrderStatus.PENDING,
    @SerialName("total_amount") val totalAmount: Double,
    @SerialName("delivery_lat") val deliveryLat: Double? = null,
    @SerialName("delivery_lng") val deliveryLng: Double? = null,
    @SerialName("created_at") val createdAt: String? = null
)
