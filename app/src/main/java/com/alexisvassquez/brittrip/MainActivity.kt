package com.alexisvassquez.brittrip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alexisvassquez.brittrip.ui.ConverterScreen
import com.alexisvassquez.brittrip.ui.theme.BritTripTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BritTripTheme {
                ConverterScreen()
            }
        }
    }
}