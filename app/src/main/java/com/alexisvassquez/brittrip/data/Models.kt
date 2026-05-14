package com.alexisvassquez.brittrip.data

import com.alexisvassquez.brittrip.converter.Category
import com.alexisvassquez.brittrip.converter.ConversionUnit
import com.alexisvassquez.brittrip.converter.LengthUnit
import com.alexisvassquez.brittrip.converter.SpeedUnit
import com.alexisvassquez.brittrip.converter.TemperatureUnit
import com.alexisvassquez.brittrip.converter.VolumeUnit
import com.alexisvassquez.brittrip.converter.WeightUnit

/*
* Models.kt
*
* Core data structures used across the app.
* No Android dependencies - plain Kotlin data classes.
*/

// ConversionResult
// Represents the output of a single conversion operation.
data class ConversionResult(
    val inputValue: Double,
    val outputValue: Double,
    val fromUnit: ConversionUnit,
    val toUnit: ConversionUnit,
    val category: Category
)

// ConversionHistory
// A stamped record of a past conversion
// Used by the history screen
data class ConversionHistory(
    val result: ConversionResult,
    val timestamp: Long = System.currentTimeMillis()
)

// CurrencyState
// Holds the live USD/GBP rate and its fetch status
// for UI to react to
sealed class CurrencyState {
    object Loading : CurrencyState()
    data class Success(val usdToGbp: Double) : CurrencyState()
    // network failed - UI shows manual fallback
    object Unavailable : CurrencyState()
}

// QuickPick
// A pre-defined unit pair shown as a chip in the UI per category
data class QuickPick(
    val label: String,
    val from: ConversionUnit,
    val to: ConversionUnit
)

// Quick picks per category
// Referenced by the UI directly
// Converted from legacy UnitConverter.java
object QuickPicks {
    val LENGTH = listOf(
        QuickPick("Miles → km", LengthUnit.MILES, LengthUnit.KILOMETERS),
        QuickPick("Feet → m", LengthUnit.FEET, LengthUnit.METERS),
        QuickPick("Inches → cm", LengthUnit.INCHES, LengthUnit.CENTIMETERS),
        QuickPick("Yards → m", LengthUnit.YARDS, LengthUnit.METERS)
    )
    val WEIGHT = listOf(
        QuickPick("lb → kg", WeightUnit.POUNDS, WeightUnit.KILOGRAMS),
        QuickPick("Stone → kg", WeightUnit.STONE, WeightUnit.KILOGRAMS),
        QuickPick("oz → g", WeightUnit.OUNCES, WeightUnit.GRAMS)
    )
    val TEMPERATURE = listOf(
        QuickPick("°C → °F", TemperatureUnit.CELSIUS, TemperatureUnit.FAHRENHEIT),
        QuickPick("°F → °C", TemperatureUnit.FAHRENHEIT, TemperatureUnit.CELSIUS)
    )
    val VOLUME = listOf(
        QuickPick("L → US gal", VolumeUnit.LITERS, VolumeUnit.US_GALLONS),
        QuickPick("L → imp gal", VolumeUnit.LITERS, VolumeUnit.IMP_GALLONS),
        QuickPick("L → US pt", VolumeUnit.LITERS, VolumeUnit.US_PINTS),
        QuickPick("L → imp pt", VolumeUnit.LITERS, VolumeUnit.IMP_PINTS)
    )
    val SPEED = listOf(
        QuickPick("mph → km/h", SpeedUnit.MPH, SpeedUnit.KMH),
        QuickPick("km/h → mph", SpeedUnit.KMH, SpeedUnit.MPH)
    )
}