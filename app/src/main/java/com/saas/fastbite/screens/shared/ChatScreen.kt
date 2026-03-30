package com.saas.fastbite.screens.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saas.fastbite.ui.theme.Cream
import com.saas.fastbite.ui.theme.DeepBrown
import com.saas.fastbite.ui.theme.WarmAmber
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ── Data models ───────────────────────────────────────────────────────────────

data class ChatMessage(
    val id: String,
    val text: String,
    val isMine: Boolean,
    val time: String,
    val isRead: Boolean = true
)

data class QuickReply(val text: String)

val quickReplies = listOf(
    QuickReply("On my way! 🛵"),
    QuickReply("Almost there ⏱"),
    QuickReply("Order is ready 🍔"),
    QuickReply("5 min away 📍"),
    QuickReply("Please wait 🙏"),
    QuickReply("Thank you! 😊"),
)

val sampleMessages = listOf(
    ChatMessage("1", "Hi! Your order has been confirmed.", false, "10:30 AM"),
    ChatMessage("2", "Great! How long will it take?", true, "10:31 AM"),
    ChatMessage("3", "Around 20 minutes. We're preparing it now 👨‍🍳", false, "10:31 AM"),
    ChatMessage("4", "Okay, sounds good!", true, "10:32 AM"),
    ChatMessage("5", "Your rider Ali has picked up the order 🛵", false, "10:45 AM"),
    ChatMessage("6", "Perfect, I'll be waiting at the gate.", true, "10:46 AM"),
    ChatMessage("7", "On my way! 🛵", false, "10:47 AM"),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun ChatScreen(
    onBack: () -> Unit = {},
    chatWithName: String = "Burger Lab",
    chatWithEmoji: String = "🍔",
    orderId: String = "FB041"
) {
    val messages = remember { mutableStateListOf(*sampleMessages.toTypedArray()) }
    var inputText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Auto scroll to bottom
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Simulate typing indicator
    fun sendMessage() {
        if (inputText.isBlank()) return
        val newMsg = ChatMessage(
            id = System.currentTimeMillis().toString(),
            text = inputText.trim(),
            isMine = true,
            time = "Now"
        )
        messages.add(newMsg)
        inputText = ""

        // Simulate reply
        scope.launch {
            isTyping = true
            delay(1500)
            isTyping = false
            messages.add(
                ChatMessage(
                    id = System.currentTimeMillis().toString(),
                    text = getAutoReply(newMsg.text),
                    isMine = false,
                    time = "Now"
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Top bar
            ChatTopBar(
                name = chatWithName,
                emoji = chatWithEmoji,
                orderId = orderId,
                onBack = onBack
            )

            // ── Order info chip
            OrderInfoChip(orderId = orderId)

            // ── Messages list
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 10.dp
                ),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Date separator
                item {
                    DateSeparator(label = "Today")
                }

                items(messages, key = { it.id }) { message ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically(
                            initialOffsetY = { it / 2 }
                        )
                    ) {
                        ChatBubble(message = message)
                    }
                }

                // Typing indicator
                if (isTyping) {
                    item {
                        TypingIndicator(name = chatWithName)
                    }
                }
            }

            // ── Quick replies
            QuickRepliesRow(
                replies = quickReplies,
                onSelect = { reply ->
                    inputText = reply.text
                }
            )

            // ── Input bar
            ChatInputBar(
                text = inputText,
                onTextChange = { inputText = it },
                onSend = { sendMessage() }
            )
        }
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
fun ChatTopBar(
    name: String,
    emoji: String,
    orderId: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Back
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Cream)
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = DeepBrown,
                modifier = Modifier.size(18.dp)
            )
        }

        // Avatar
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(WarmAmber.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 22.sp)
        }

        // Name + status
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                )
                Text(
                    text = "Online · Order #$orderId",
                    fontSize = 11.sp,
                    color = DeepBrown.copy(alpha = 0.45f)
                )
            }
        }

        // Call button
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9))
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Call",
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// ── Order info chip ───────────────────────────────────────────────────────────

@Composable
fun OrderInfoChip(orderId: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(WarmAmber.copy(alpha = 0.08f))
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "📦", fontSize = 14.sp)
            Text(
                text = "This chat is linked to Order #$orderId",
                fontSize = 12.sp,
                color = DeepBrown.copy(alpha = 0.6f),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "View",
                fontSize = 12.sp,
                color = WarmAmber,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ── Date separator ────────────────────────────────────────────────────────────

@Composable
fun DateSeparator(label: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(DeepBrown.copy(alpha = 0.07f))
                .padding(horizontal = 14.dp, vertical = 5.dp)
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                color = DeepBrown.copy(alpha = 0.5f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ── Chat bubble ───────────────────────────────────────────────────────────────

@Composable
fun ChatBubble(message: ChatMessage) {
    val isMine = message.isMine

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMine) {
            // Avatar for other
            Box(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(WarmAmber.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🍔", fontSize = 14.sp)
            }
        }

        Column(
            horizontalAlignment = if (isMine) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 260.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (isMine) 18.dp else 4.dp,
                            bottomEnd = if (isMine) 4.dp else 18.dp
                        )
                    )
                    .background(
                        if (isMine) WarmAmber else Color.White
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = if (isMine) Cream else DeepBrown,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            // Time + read receipt
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message.time,
                    fontSize = 10.sp,
                    color = DeepBrown.copy(alpha = 0.35f)
                )
                if (isMine) {
                    Text(
                        text = if (message.isRead) "✓✓" else "✓",
                        fontSize = 10.sp,
                        color = if (message.isRead) WarmAmber
                        else DeepBrown.copy(alpha = 0.35f)
                    )
                }
            }
        }

        if (isMine) {
            // Avatar for me
            Box(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(WarmAmber),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "A",
                    fontSize = 12.sp,
                    color = Cream,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ── Typing indicator ──────────────────────────────────────────────────────────

@Composable
fun TypingIndicator(name: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "typing")
    val dot1 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(400),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(0)
        ), label = "d1"
    )
    val dot2 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(400),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(150)
        ), label = "d2"
    )
    val dot3 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(400),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(300)
        ), label = "d3"
    )

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(WarmAmber.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🍔", fontSize = 14.sp)
        }

        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 18.dp
                    )
                )
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(dot1, dot2, dot3).forEach { alpha ->
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(DeepBrown.copy(alpha = 0.3f + alpha * 0.5f))
                    )
                }
            }
        }
    }
}

// ── Quick replies row ─────────────────────────────────────────────────────────

@Composable
fun QuickRepliesRow(
    replies: List<QuickReply>,
    onSelect: (QuickReply) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Scrollable quick replies
            androidx.compose.foundation.lazy.LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(replies) { reply ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(WarmAmber.copy(alpha = 0.1f))
                            .border(
                                width = 1.dp,
                                color = WarmAmber.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { onSelect(reply) }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = reply.text,
                            fontSize = 12.sp,
                            color = DeepBrown.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

// ── Chat input bar ────────────────────────────────────────────────────────────

@Composable
fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Text field
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    text = "Type a message...",
                    fontSize = 14.sp,
                    color = DeepBrown.copy(alpha = 0.3f)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { onSend() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = WarmAmber,
                unfocusedBorderColor = DeepBrown.copy(alpha = 0.1f),
                focusedTextColor = DeepBrown,
                unfocusedTextColor = DeepBrown,
                cursorColor = WarmAmber,
                focusedContainerColor = Cream,
                unfocusedContainerColor = Cream
            )
        )

        // Send button
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (text.isNotBlank()) WarmAmber else DeepBrown.copy(alpha = 0.1f))
                .clickable(enabled = text.isNotBlank()) { onSend() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = if (text.isNotBlank()) Cream else DeepBrown.copy(alpha = 0.3f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ── Auto reply helper ─────────────────────────────────────────────────────────

fun getAutoReply(message: String): String {
    return when {
        message.contains("where", ignoreCase = true) ||
                message.contains("location", ignoreCase = true) ->
            "I'm on my way! Almost there 📍"
        message.contains("how long", ignoreCase = true) ||
                message.contains("time", ignoreCase = true) ->
            "Around 5 more minutes, hang tight! ⏱"
        message.contains("thank", ignoreCase = true) ->
            "You're welcome! Enjoy your meal 😊🍔"
        message.contains("ok", ignoreCase = true) ||
                message.contains("okay", ignoreCase = true) ->
            "Great! See you soon 🛵"
        message.contains("cancel", ignoreCase = true) ->
            "I'm sorry, please contact support for cancellation 🙏"
        else -> "Got it! 👍"
    }
}