package com.saas.fastbite.screens.auth

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saas.fastbite.ui.theme.Cream
import com.saas.fastbite.ui.theme.DeepBrown
import com.saas.fastbite.ui.theme.WarmAmber

enum class UserRole { CLIENT, RESTAURANT, RIDER }

data class RoleOption(
    val role: UserRole,
    val emoji: String,
    val title: String,
    val description: String
)

val roleOptions = listOf(
    RoleOption(
        role = UserRole.CLIENT,
        emoji = "🛵",
        title = "I'm a Customer",
        description = "Order food from your\nfavourite restaurants"
    ),
    RoleOption(
        role = UserRole.RESTAURANT,
        emoji = "🍽️",
        title = "I'm a Restaurant",
        description = "Manage orders, menu\nand token wallet"
    ),
    RoleOption(
        role = UserRole.RIDER,
        emoji = "🏍️",
        title = "I'm a Rider",
        description = "Deliver orders and\nearn on the go"
    )
)

@Composable
fun RoleSelectorScreen(onRoleSelected: (UserRole) -> Unit) {
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {

        // 🔽 Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(72.dp))

            // Header icon
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(WarmAmber),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🍔", fontSize = 32.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Who are you?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Select your role to get\nthe right experience",
                fontSize = 15.sp,
                color = DeepBrown.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                roleOptions.forEach { option ->
                    RoleCard(
                        option = option,
                        isSelected = selectedRole == option.role,
                        onClick = { selectedRole = option.role }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // 🔽 Fixed bottom button
        Button(
            onClick = {
                selectedRole?.let { onRoleSelected(it) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 28.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = WarmAmber,
                contentColor = Cream,
                disabledContainerColor = WarmAmber.copy(alpha = 0.3f),
                disabledContentColor = Cream.copy(alpha = 0.5f)
            ),
            enabled = selectedRole != null
        ) {
            Text(
                text = "Continue",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RoleCard(
    option: RoleOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = tween(200),
        label = "cardScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) WarmAmber.copy(alpha = 0.08f)
                else Color.White
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) WarmAmber
                else DeepBrown.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isSelected) WarmAmber.copy(alpha = 0.15f)
                        else DeepBrown.copy(alpha = 0.05f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = option.emoji, fontSize = 28.sp)
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = option.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) DeepBrown else DeepBrown.copy(alpha = 0.85f)
                )
                Text(
                    text = option.description,
                    fontSize = 13.sp,
                    color = DeepBrown.copy(alpha = 0.5f),
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        if (isSelected) WarmAmber
                        else Color.Transparent
                    )
                    .border(
                        width = 2.dp,
                        color = if (isSelected) WarmAmber
                        else DeepBrown.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Text(
                        text = "✓",
                        fontSize = 12.sp,
                        color = Cream,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}