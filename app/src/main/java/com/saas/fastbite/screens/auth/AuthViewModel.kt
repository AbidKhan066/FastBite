package com.saas.fastbite.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saas.fastbite.data.model.UserRole
import com.saas.fastbite.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
    object RoleRequired : AuthState()
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun loginWithPhone(phone: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            // MOCK: For this demo, we will use a fixed password and map phone to email
            // In a real app, you would use repository.client.auth.signInWith(OTP)
            val email = "${phone}@fastbite.com"
            val password = "Password123!" 
            
            val result = repository.signIn(email, password)
            if (result.isSuccess) {
                checkProfile()
            } else {
                // If user doesn't exist, try signing up
                val signUpResult = repository.signUp(email, password, "FastBite User")
                if (signUpResult.isSuccess) {
                    _authState.value = AuthState.RoleRequired
                } else {
                    _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Auth failed")
                }
            }
        }
    }

    fun checkProfile() {
        viewModelScope.launch {
            val profileResult = repository.getProfile()
            val profile = profileResult.getOrNull()
            if (profile != null) {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.RoleRequired
            }
        }
    }

    fun selectRole(role: UserRole) {
        viewModelScope.launch {
            val userId = repository.getCurrentUserId() ?: return@launch
            val result = repository.updateRole(userId, role)
            if (result.isSuccess) {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Failed to update role")
            }
        }
    }
}
