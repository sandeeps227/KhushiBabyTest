package com.example.khushibabytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.khushibabytest.ui.components.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@Composable
fun MApp() {
    MaterialTheme {
        AppNavigation()
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MApp()
        }
    }
}
