package com.alexisvassquez.brittrip.converter

/*
* BritTrip Conversion Engine
*
* Pure Kotlin conversion logic, no Android dependencies
* All constants are exact standard values carried over from the
* original CLI legacy prototype.
* Currency conversion is handled separately via CurrencyRepository
* (requires network).
*
* Usage:
*   val result = Converter.convert(5.0, LengthUnit.MILES, LengthUnit.KILOMETERS)
* */

object Converter {
    // Exact standard constants
    // same as legacy UnitConverter.java
    private const val MILES_TO_KM = 1.609344
    private const val FOOT_TO_M = 0.3048
    private const val INCH_TO_CM = 2.54
    private const val LB_TO_KG = 0.45359237
    private const val STONE_TO_LB = 14.0
    private const val OZ_TO_G = 28.349523125
    private const val YARD_TO_M = 0.9144
    private const val LITER_TO_US_GAL = 1.0 / 3.785411784
    private const val LITER_TO_IMP_GAL = 1.0 / 4.54609
    private const val LITER_TO_US_PINT = 1.0 / 0.473176473
    private const val LITER_TO_IMP_PINT = 1.0 / 0.56826125
    private const val MPH_TO_KMH = 1.609344

    // Length
    fun milesToKilometers(value: Double) = value * MILES_TO_KM
    fun kilometersToMiles(value: Double) = value / MILES_TO_KM

    fun feetToMeters(value: Double) = value * FOOT_TO_M
    fun metersToFeet(value: Double) = value / FOOT_TO_M

    fun inchesToCentimeters(value: Double) = value * INCH_TO_CM
    fun centimetersToInches(value: Double) = value / INCH_TO_CM

    fun yardsToMeters(value: Double) = value * YARD_TO_M
    fun metersToYards(value: Double) = value / YARD_TO_M

    // Weight
    fun poundsToKilograms(value: Double) = value * LB_TO_KG
    fun kilogramsToPounds(value: Double) = value / LB_TO_KG

    fun stoneToKilograms(value: Double) = value * STONE_TO_LB * LB_TO_KG
    fun kilogramsToStone(value: Double) = value / LB_TO_KG / STONE_TO_LB

    fun ouncesToGrams(value: Double) = value * OZ_TO_G
    fun gramsToOunces(value: Double) = value / OZ_TO_G

    // Temperature
    fun celsiusToFahrenheit(value: Double) = value * 9.0 / 5.0 + 32.0
    fun fahrenheitToCelsius(value: Double) = (value - 32.0) * 5.0 / 9.0

    // Volume
    fun literToUsGallons(value: Double) = value * LITER_TO_US_GAL
    fun usGallonsToLiters(value: Double) = value / LITER_TO_US_GAL

    fun literToImpGallons(value: Double) = value * LITER_TO_IMP_GAL
    fun impGallonsToLiters(value: Double) = value / LITER_TO_IMP_GAL

    fun literToUsPints(value: Double) = value * LITER_TO_US_PINT
    fun usPintsToLiters(value: Double) = value / LITER_TO_US_PINT

    fun literToImpPints(value: Double) = value * LITER_TO_IMP_PINT
    fun impPintsToLiters(value: Double) = value / LITER_TO_IMP_PINT

    // Speed
    fun mphToKmh(value: Double) = value * MPH_TO_KMH
    fun kmhToMph(value: Double) = value / MPH_TO_KMH

    // Generic dispatch
    // called by the UI with unit enums
    fun convert(value: Double, from: ConversionUnit, to: ConversionUnit): Double {
        if (from == to) return value
        return when (from) {
            // length
            LengthUnit.MILES -> when (to) {
                LengthUnit.KILOMETERS -> milesToKilometers(value)
                else -> unsupported(from, to)
            }

            LengthUnit.KILOMETERS -> when (to) {
                LengthUnit.MILES -> kilometersToMiles(value)
                else -> unsupported(from, to)
            }

            LengthUnit.FEET -> when (to) {
                LengthUnit.METERS -> feetToMeters(value)
                else -> unsupported(from, to)
            }

            LengthUnit.METERS -> when (to) {
                LengthUnit.FEET -> metersToFeet(value)
                LengthUnit.YARDS -> metersToYards(value)
                else -> unsupported(from, to)
            }

            LengthUnit.INCHES -> when (to) {
                LengthUnit.CENTIMETERS -> inchesToCentimeters(value)
                else -> unsupported(from, to)
            }

            LengthUnit.CENTIMETERS -> when (to) {
                LengthUnit.INCHES -> centimetersToInches(value)
                else -> unsupported(from, to)
            }

            LengthUnit.YARDS -> when (to) {
                LengthUnit.METERS -> yardsToMeters(value)
                else -> unsupported(from, to)
            }

            // Weight
            WeightUnit.POUNDS -> when (to) {
                WeightUnit.KILOGRAMS -> poundsToKilograms(value)
                else -> unsupported(from, to)
            }

            WeightUnit.KILOGRAMS -> when (to) {
                WeightUnit.POUNDS -> kilogramsToPounds(value)
                WeightUnit.STONE -> kilogramsToStone(value)
                else -> unsupported(from, to)
            }

            WeightUnit.STONE -> when (to) {
                WeightUnit.KILOGRAMS -> stoneToKilograms(value)
                else -> unsupported(from, to)
            }

            WeightUnit.OUNCES -> when (to) {
                WeightUnit.GRAMS -> ouncesToGrams(value)
                else -> unsupported(from, to)
            }

            // Temperature
            TemperatureUnit.CELSIUS -> when (to) {
                TemperatureUnit.FAHRENHEIT -> fahrenheitToCelsius(value)
                else -> unsupported(from, to)
            }

            // Volume
            VolumeUnit.LITERS -> when (to) {
                VolumeUnit.US_GALLONS -> literToUsGallons(value)
                VolumeUnit.IMP_GALLONS -> literToImpGallons(value)
                VolumeUnit.US_PINTS -> literToUsPints(value)
                VolumeUnit.IMP_PINTS -> literToImpPints(value)
                else -> unsupported(from, to)
            }

            VolumeUnit.US_GALLONS -> when (to) {
                VolumeUnit.LITERS -> usGallonsToLiters(value)
                else -> unsupported(from, to)
            }

            VolumeUnit.IMP_GALLONS -> when (to) {
                VolumeUnit.LITERS -> impGallonsToLiters(value)
                else -> unsupported(from, to)
            }

            VolumeUnit.US_PINTS -> when (to) {
                VolumeUnit.LITERS -> usPintsToLiters(value)
                else -> unsupported(from, to)
            }

            VolumeUnit.IMP_PINTS -> when (to) {
                VolumeUnit.LITERS -> impPintsToLiters(value)
                else -> unsupported(from, to)
            }

            // Speed
            SpeedUnit.MPH -> when (to) {
                SpeedUnit.KMH -> mphToKmh(value)
                else -> unsupported(from, to)
            }

            SpeedUnit.KMH -> when (to) {
                SpeedUnit.MPH -> kmhToMph(value)
                else -> unsupported(from, to)
            }

            else -> unsupported(from, to)
        }
    }

    private fun unsupported(from: ConversionUnit, to: ConversionUnit): Double {
        throw IllegalArgumentException("Unsupported conversion: $from → $to")
    }
}