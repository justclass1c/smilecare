package com.class1c.smilecare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.class1c.smilecare.di.UserSession
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.ui.SmileCareApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/** Required so Hilt can inject into this Activity and any `hiltViewModel()` it hosts. */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var userSession: UserSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentUser by userSession.currentUser.collectAsStateWithLifecycle()
            SmileCareApp(role = currentUser?.role ?: UserRole.PATIENT) // login as patient by default
        }
    }
}
