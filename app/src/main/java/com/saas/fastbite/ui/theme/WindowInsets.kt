package com.saas.fastbite.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun systemNavigationBarPadding(): PaddingValues {
    val navBar = WindowInsets.navigationBars.asPaddingValues()
    return PaddingValues(
        bottom = navBar.calculateBottomPadding() + 16.dp
    )
}

@Composable
fun statusBarPadding(): PaddingValues {
    val statusBar = WindowInsets.statusBars.asPaddingValues()
    return PaddingValues(
        top = statusBar.calculateTopPadding()
    )
}