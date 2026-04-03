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
import com.saas.fastbite.data.remote.supabaseClient
import com.saas.fastbite.data.repository.AuthRepository
import com.saas.fastbite.screens.auth.AuthViewModel
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel

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
                    val authRepository = remember { AuthRepository(supabaseClient) }
                    val authViewModel: AuthViewModel = viewModel { AuthViewModel(authRepository) }
                    
                    NavGraph(
                        navController = navController,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}