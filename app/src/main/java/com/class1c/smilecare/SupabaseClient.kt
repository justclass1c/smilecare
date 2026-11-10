package com.class1c.smilecare

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

object SupabaseConfig {
    const val URL = BuildConfig.SUPABASE_URL
    const val ANON_KEY = BuildConfig.SUPABASE_ANON_KEY
}

val supabase = createSupabaseClient(
    supabaseUrl = SupabaseConfig.URL,
    supabaseKey = SupabaseConfig.ANON_KEY
) {
   install(Postgrest)
    defaultSerializer = KotlinXSerializer(Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    })
}