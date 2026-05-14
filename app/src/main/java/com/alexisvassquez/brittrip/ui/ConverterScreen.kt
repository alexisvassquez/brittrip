package com.alexisvassquez.brittrip.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.HistoricalChange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexisvassquez.brittrip.ui.theme.*
import com.alexisvassquez.brittrip.converter.Category
import com.alexisvassquez.brittrip.converter.ConversionUnit
import com.alexisvassquez.brittrip.data.CurrencyState
import com.alexisvassquez.brittrip.data.QuickPick
import com.alexisvassquez.brittrip.data.QuickPicks
import com.alexisvassquez.brittrip.ui.theme.BritTripTheme

/*
* BritTrip Converter Screen
* Branding and color scheme is defined in ui/theme
* London Underground inspired
* Screen is CRUD and converts US units to UK
*/

@Composable
fun ConverterScreen(viewModel: ConverterViewModel = viewModel()) {
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val fromUnit by viewModel.fromUnit.collectAsStateWithLifecycle()
    val toUnit by viewModel.toUnit.collectAsStateWithLifecycle()
    val inputValue by viewModel.inputValue.collectAsStateWithLifecycle()
    val outputValue by viewModel.outputValue.collectAsStateWithLifecycle()
    val currencyState by viewModel.currencyState.collectAsStateWithLifecycle()
    val quickPicks by viewModel.quickPicks.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = OffWhite
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // header
            BritTripHeader()

            // Category tabs
            CategoryTabs(
                selected = selectedCategory,
                onSelected = viewModel::onCategorySelected
            )

            // Main converter body
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // From field
                ConverterField(
                    label = "From • ${if (selectedCategory == Category.CURRENCY) "USD" else "US"}",
                    value = inputValue,
                    unit = fromUnit,
                    onValueChange = viewModel::onInputChanged,
                    onUnitChange = viewModel::onFromUnitChanged,
                    units = selectedCategory.units,
                    isInput = true
                )

                // Swap button
                SwapButton(onClick = viewModel::onSwapUnits)

                // To field
                ConverterField(
                    label = "To • ${if (selectedCategory == Category.CURRENCY) "GBP" else "UK"}",
                    value = outputValue,
                    unit = toUnit,
                    onValueChange = {},
                    onUnitChange = viewModel::onToUnitChanged,
                    units = selectedCategory.units,
                    isInput = false
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quick picks
                if (quickPicks.isNotEmpty()) {
                    QuickPicksRow(
                        picks = quickPicks,
                        fromUnit = fromUnit,
                        onPickSelected = viewModel::onQuickPickSelected
                    )
                }

                // Currency live rate badge
                if (selectedCategory == Category.CURRENCY) {
                    CurrencyBadge(state = currencyState)
                }
            }
        }
    }
}

// Header
@Composable
fun BritTripHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavyBlue)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Roundel(modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "BritTrip",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "US • UK TRAVELER'S KIT",
                    color = Color(0xFF7BA3C8),
                    fontSize = 10.sp,
                    letterSpacing = 1.sp
                )
            }
        }
        // UK flag emoji
        Text(text = "\uD83C\uDDEC\uD83C\uDDE7", fontSize = 24.sp)
    }
}

// Roundel
// CSS free, pure Compose
@Composable
fun Roundel(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        // outer ring
        // thicker stroke for fatter circle
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(Color.Transparent)
                .padding(6.dp)
        )
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = CircleShape,
            color = Color.Transparent,
            border = androidx.compose.foundation.BorderStroke(7.dp, TubeRed)
        ) {}
        // Navy horizontal bar across the middle
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(22.dp)
                .background(NavyBlue)
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            // top red borderline
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(TubeRed)
                    .align(Alignment.TopCenter)
            )
            // "BritTrip" text in bar
            // centered in box
            Text(
                text = "BritTrip",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)
            )
            // bottom red borderline
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(TubeRed)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

// Category tabs
@Composable
fun CategoryTabs(
    selected: Category,
    onSelected: (Category) -> Unit
) {
    PrimaryScrollableTabRow(
        selectedTabIndex = Category.entries.indexOf(selected),
        containerColor = NavyBlue,
        contentColor = Color.White,
        edgePadding = 4.dp,
    ) {
        Category.entries.forEach { category ->
            Tab(
                selected = selected == category,
                onClick = { onSelected(category) },
                text = {
                    Text(
                        text = category.label,
                        fontSize = 12.sp,
                        color = if (selected == category) Color.White else MutedBlue
                    )
                }
            )
        }
    }
}

// Converter field
// shared for input and output
@Composable
fun ConverterField(
    label: String,
    value: String,
    unit: ConversionUnit,
    onValueChange: (String) -> Unit,
    onUnitChange: (ConversionUnit) -> Unit,
    units: List<ConversionUnit>,
    isInput: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(13.dp)) {
            Text(
                text = label,
                fontSize = 10.sp,
                color = SubtleGray,
                letterSpacing = 0.8.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isInput) {
                    TextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.weight(1f),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Medium,
                            color = NavyBlue
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = {
                            Text("0", fontSize = 30.sp, color = SubtleGray)
                        }
                    )
                } else {
                    Text(
                        text = value.ifEmpty { "-"},
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Medium,
                        color = TubeRed,
                        modifier = Modifier.weight(1f)
                    )
                }

                // unit selector dropdown
                Box {
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = Color(0xFFEEF2F7),
                        border = androidx.compose.foundation.BorderStroke(
                            0.5.dp, Color(0xFFC8D430)
                        ),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = unit.label,
                                fontSize = 12.sp,
                                color = NavyBlue,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "▾", fontSize = 12.sp, color = NavyBlue)
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        units.forEach { u ->
                            DropdownMenuItem(
                                text = { Text("${u.label} (${u.abbreviation}") },
                                onClick = {
                                    onUnitChange(u)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// Swap button
@Composable
fun SwapButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = Color(0xFFDDDDDD)
        )
        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(34.dp)
                .clip(CircleShape)
                .background(NavyBlue)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "⇅", color = Color.White, fontSize = 16.sp)
        }
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = Color(0xFFDDDDDD)
        )
    }
}

// Quick picks row
@Composable
fun QuickPicksRow(
    picks: List<QuickPick>,
    fromUnit: ConversionUnit,
    onPickSelected: (QuickPick) -> Unit
) {
    Column {
        Text(
            text = "QUICK PICKS",
            fontSize = 10.sp,
            color = SubtleGray,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(picks) { pick ->
                val isActive = pick.from == fromUnit
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = if (isActive) NavyBlue else CardWhite,
                    border = androidx.compose.foundation.BorderStroke(
                        0.5.dp,
                        if (isActive) NavyBlue else Color(0xFFC8D4E0)
                    ),
                    modifier = Modifier.clickable { onPickSelected(pick) }
                ) {
                    Text(
                        text = pick.label,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        fontSize = 11.sp,
                        color = if (isActive) Color.White else NavyBlue
                    )
                }
            }
        }
    }
}

// Currency live rate badge
@Composable
fun CurrencyBadge(state: CurrencyState) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        when (state) {
            is CurrencyState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(10.dp),
                    color = NavyBlue,
                    strokeWidth = 1.5.dp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Fetching live rate...", fontSize = 11.sp, color = SubtleGray)
            }
            is CurrencyState.Success -> {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF43A047))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Live rate • 1 USD = ${"%.4f".format(state.usdToGbp)} GBP",
                    fontSize = 11.sp,
                    color = Color(0xFF2E7D32)
                )
            }
            is CurrencyState.Unavailable -> {
                Text(
                    text = "⚠ Live rate unavailable",
                    fontSize = 11.sp,
                    color = Color(0xFFB71C1C)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConverterScreenPreview() {
    BritTripTheme {
        ConverterScreen()
    }
}