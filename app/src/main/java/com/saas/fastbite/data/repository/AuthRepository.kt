package com.saas.fastbite.data.repository

import com.saas.fastbite.data.model.Profile
import com.saas.fastbite.data.model.UserRole
import com.saas.fastbite.data.remote.SupabaseConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val client: SupabaseClient) {

    suspend fun signUp(email: String, password: String, fullName: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            
            val userId = client.auth.currentUserOrNull()?.id ?: return@withContext Result.failure(Exception("Signup failed"))
            
            // Create profile
            val profile = Profile(id = userId, fullName = fullName)
            client.postgrest["profiles"].insert(profile)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProfile(): Result<Profile?> = withContext(Dispatchers.IO) {
        try {
            val userId = client.auth.currentUserOrNull()?.id ?: return@withContext Result.success(null)
            val profile = client.postgrest["profiles"]
                .select { 
                    filter { 
                        eq("id", userId) 
                    } 
                }.decodeSingleOrNull<Profile>()
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateRole(userId: String, role: UserRole): Result<Unit> = withContext(Dispatchers.IO) {
         try {
            client.postgrest["profiles"].update(
                mapOf("role" to role.name.lowercase())
            ) {
                filter {
                    eq("id", userId)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUserId(): String? = client.auth.currentUserOrNull()?.id
}
