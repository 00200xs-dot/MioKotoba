package com.akira.miokotoba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.akira.miokotoba.ui.MainScreen
import com.akira.miokotoba.ui.theme.MioKotobaTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MioKotobaTheme {
                MainScreen()
            }
        }
    }
}
