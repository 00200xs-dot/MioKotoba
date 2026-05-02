package com.akira.miokotoba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

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

//SAMSUNG S23 Ultra Preview
@Preview(showBackground = true, device = "spec:width=384dp,height=824dp,dpi=500")
@Composable
fun WordCardPreview() {
    MainScreen()
}

//占位符
@Composable
fun PlaceholderPage(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.headlineMedium)
    }
}
