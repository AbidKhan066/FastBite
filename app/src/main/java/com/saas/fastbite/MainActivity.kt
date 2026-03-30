package com.saas.fastbite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.saas.fastbite.navigation.NavGraph
import com.saas.fastbite.ui.theme.FastBiteTheme
import androidx.compose.material3.Surface
import com.saas.fastbite.ui.theme.Cream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            FastBiteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Cream
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }
}