package com.class1c.smilecare.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.navigation.SmileCareNavHost
import com.class1c.smilecare.ui.theme.SmileCareTheme

@Composable
fun SmileCareApp(role: UserRole = UserRole.PATIENT) {
    SmileCareTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SmileCareNavHost(role = role)
        }
    }
}
