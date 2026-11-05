package com.class1c.smilecare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.class1c.smilecare.ui.SmileCareApp
import dagger.hilt.android.AndroidEntryPoint

/** Required so Hilt can inject into this Activity and any `hiltViewModel()` it hosts. */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmileCareApp()
        }
    }
}
