package com.alexisvassquez.brittrip.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alexisvassquez.brittrip.ui.theme.NavyBlue
import com.alexisvassquez.brittrip.ui.theme.SubtleGray

/*
* HistoryScreen
* Registers user's recent conversions
*/

@Composable
fun HistoryScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🕑", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No conversions yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = NavyBlue
            )
            Text(
                text = "Your recent conversions will appear here",
                fontSize = 13.sp,
                color = SubtleGray
            )
        }
    }
}