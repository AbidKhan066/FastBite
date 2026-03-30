package com.saas.fastbite.screens.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saas.fastbite.ui.theme.Cream
import com.saas.fastbite.ui.theme.DeepBrown
import com.saas.fastbite.ui.theme.WarmAmber
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.navigationBars
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {

    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf(listOf("", "", "", "")) }
    var showOtp by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val focusRequesters = remember { List(4) { FocusRequester() } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Logo / brand
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(WarmAmber),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🍔",
                    fontSize = 36.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "FastBite",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBrown
            )

            Text(
                text = "Food at your speed",
                fontSize = 14.sp,
                color = DeepBrown.copy(alpha = 0.5f),
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(56.dp))

            // Animated switch between phone and OTP
            AnimatedContent(
                targetState = showOtp,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(400)
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(400)
                    )
                },
                label = "authContent"
            ) { otpVisible ->
                if (!otpVisible) {
                    PhoneSection(
                        phone = phone,
                        onPhoneChange = { if (it.length <= 11) phone = it },
                        onSendOtp = {
                            if (phone.length >= 10) {
                                isLoading = true
                                showOtp = true
                                isLoading = false
                            }
                        },
                        isLoading = isLoading
                    )
                } else {
                    OtpSection(
                        phone = phone,
                        otp = otp,
                        focusRequesters = focusRequesters,
                        onOtpChange = { index, value ->
                            if (value.length <= 1) {
                                otp = otp.toMutableList().also { it[index] = value }
                                if (value.isNotEmpty() && index < 3) {
                                    focusRequesters[index + 1].requestFocus()
                                }
                            }
                        },
                        onVerify = {
                            val fullOtp = otp.joinToString("")
                            if (fullOtp.length == 4) onLoginSuccess()
                        },
                        onBack = {
                            showOtp = false
                            otp = listOf("", "", "", "")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PhoneSection(
    phone: String,
    onPhoneChange: (String) -> Unit,
    onSendOtp: () -> Unit,
    isLoading: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Enter your number",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown,
            textAlign = TextAlign.Center
        )

        Text(
            text = "We'll send a 4-digit OTP\nto verify your identity",
            fontSize = 14.sp,
            color = DeepBrown.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Phone input
        OutlinedTextField(
            value = phone,
            onValueChange = onPhoneChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "03XX XXXXXXX",
                    color = DeepBrown.copy(alpha = 0.3f)
                )
            },
            prefix = {
                Text(
                    text = "+92  |  ",
                    color = DeepBrown.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Medium
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = WarmAmber,
                unfocusedBorderColor = DeepBrown.copy(alpha = 0.15f),
                focusedTextColor = DeepBrown,
                unfocusedTextColor = DeepBrown,
                cursorColor = WarmAmber,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onSendOtp,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = WarmAmber,
                contentColor = Cream
            ),
            enabled = phone.length >= 10 && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Cream,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Send OTP",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun OtpSection(
    phone: String,
    otp: List<String>,
    focusRequesters: List<FocusRequester>,
    onOtpChange: (Int, String) -> Unit,
    onVerify: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Verify OTP",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBrown,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Enter the 4-digit code sent to\n+92 $phone",
            fontSize = 14.sp,
            color = DeepBrown.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // OTP boxes
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            otp.forEachIndexed { index, value ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { onOtpChange(index, it) },
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                        .focusRequester(focusRequesters[index]),
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepBrown
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = WarmAmber,
                        unfocusedBorderColor = if (value.isNotEmpty())
                            WarmAmber.copy(alpha = 0.5f)
                        else
                            DeepBrown.copy(alpha = 0.15f),
                        cursorColor = WarmAmber,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onVerify,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = WarmAmber,
                contentColor = Cream
            ),
            enabled = otp.all { it.isNotEmpty() }
        ) {
            Text(
                text = "Verify & Continue",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Resend + back
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Didn't receive it? ",
                fontSize = 13.sp,
                color = DeepBrown.copy(alpha = 0.5f)
            )
            Text(
                text = "Resend",
                fontSize = 13.sp,
                color = WarmAmber,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { }
            )
        }

        TextButton(onClick = onBack) {
            Text(
                text = "← Change number",
                fontSize = 13.sp,
                color = DeepBrown.copy(alpha = 0.4f)
            )
        }
    }
}