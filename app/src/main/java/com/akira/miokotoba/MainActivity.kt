package com.akira.miokotoba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
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