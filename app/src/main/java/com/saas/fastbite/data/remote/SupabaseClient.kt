package com.saas.fastbite.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

object SupabaseConfig {
    const val URL = "https://qgfrwqrigurhwcmzjdwu.supabase.co"
    const val ANON_KEY = "sb_publishable_hl_Xk7kAtCu_dtG4ZOe0Dw_ex7CBb-i"
}

val supabaseClient = createSupabaseClient(
    supabaseUrl = SupabaseConfig.URL,
    supabaseKey = SupabaseConfig.ANON_KEY
) {
    install(Auth)
    install(Postgrest)
    install(Realtime)
    install(Storage)
}
