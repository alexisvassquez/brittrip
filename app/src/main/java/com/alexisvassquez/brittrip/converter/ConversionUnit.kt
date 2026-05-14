package com.alexisvassquez.brittrip.converter

/*
* Conversion Unit
*
* Sealed interface implemented by each unit enum.
* UI works with these types, no raw strings passed.
* */

sealed interface ConversionUnit {
    val label: String         // display name e.g., "Miles"
    val abbreviation: String  // short label e.g., "mi"
}

// Length
enum class LengthUnit(
    override val label: String,
    override val abbreviation: String
) : ConversionUnit {
    MILES("Miles", "mi"),
    KILOMETERS("Kilometers", "km"),
    FEET("Feet", "ft"),
    METERS("Meters", "m"),
    INCHES("Inches", "in"),
    CENTIMETERS("Centimeters", "cm"),
    YARDS("Yards", "yd")
}

// Weight
enum class WeightUnit(
    override val label: String,
    override val abbreviation: String
) : ConversionUnit {
    POUNDS("Pounds", "lb"),
    KILOGRAMS("Kilograms", "kg"),
    STONE("Stone", "st"),
    OUNCES("Ounces", "oz"),
    GRAMS("Grams", "g")
}

// Temperature
enum class TemperatureUnit(
    override val label: String,
    override val abbreviation: String
) : ConversionUnit {
    CELSIUS("Celsius", "°C"),
    FAHRENHEIT("Fahrenheit", "°F")
}

// Volume
enum class VolumeUnit(
    override val label: String,
    override val abbreviation: String
) : ConversionUnit {
    LITERS("Liters", "L"),
    US_GALLONS("US Gallons", "US gal"),
    IMP_GALLONS("Imperial Gallons", "imp gal"),
    US_PINTS("US Pints", "US pt"),
    IMP_PINTS("Imperial Pints", "imp pt")
}

// Speed
enum class SpeedUnit(
    override val label: String,
    override val abbreviation: String
) : ConversionUnit {
    MPH("Miles per hour", "mph"),
    KMH("Kilometers per hour", "km/h")
}

// Currency
// values are dynamic (live rate), handled by CurrencyRepository
enum class CurrencyUnit(
    override val label: String,
    override val abbreviation: String
) : ConversionUnit {
    USD("US Dollar", "$"),
    GBP("British Pound", "£")
}

// Category
// maps each tab in the UI to its unit type
enum class Category(
    val label: String,
    val units: List<ConversionUnit>
) {
    LENGTH("Length", LengthUnit.entries),
    WEIGHT("Weight", WeightUnit.entries),
    TEMPERATURE("Temperature", TemperatureUnit.entries),
    VOLUME("Volume", VolumeUnit.entries),
    CURRENCY("Currency", CurrencyUnit.entries),
    SPEED("Speed", SpeedUnit.entries)
}