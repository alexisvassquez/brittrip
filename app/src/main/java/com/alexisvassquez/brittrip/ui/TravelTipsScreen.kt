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
* TravelTipsScreen
* This is a skeleton for now
* Will also include tab for travel tips catering
* to US/UK travelers
*/

@Composable
fun TravelTipsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("\uD83C\uDDEC\uD83C\uDDE7", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Travel Tips",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = NavyBlue
            )
            Text (
                text = "Coming soon - tips for US/UK travellers",
                fontSize = 13.sp,
                color = SubtleGray
            )
        }
    }
}