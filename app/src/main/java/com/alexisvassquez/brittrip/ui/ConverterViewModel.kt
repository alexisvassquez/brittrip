package com.alexisvassquez.brittrip.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexisvassquez.brittrip.converter.Category
import com.alexisvassquez.brittrip.converter.ConversionUnit
import com.alexisvassquez.brittrip.converter.Converter
import com.alexisvassquez.brittrip.converter.CurrencyUnit
import com.alexisvassquez.brittrip.converter.LengthUnit
import com.alexisvassquez.brittrip.converter.TemperatureUnit
import com.alexisvassquez.brittrip.converter.VolumeUnit
import com.alexisvassquez.brittrip.converter.WeightUnit
import com.alexisvassquez.brittrip.converter.SpeedUnit
import com.alexisvassquez.brittrip.data.ConversionHistory
import com.alexisvassquez.brittrip.data.ConversionResult
import com.alexisvassquez.brittrip.data.CurrencyRepository
import com.alexisvassquez.brittrip.data.CurrencyState
import com.alexisvassquez.brittrip.data.QuickPick
import com.alexisvassquez.brittrip.data.QuickPicks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/*
* Converter View Model
*
* Sits between the data layer and UI, holds state,
* calls into Converter and CurrencyRepository.
* Compose will observe to know when to redraw screen.
*/

class ConverterViewModel : ViewModel() {
    private val currencyRepository = CurrencyRepository()

    // Selected category (tab)
    private val _selectedCategory = MutableStateFlow(Category.LENGTH)
    val selectedCategory: StateFlow<Category> = _selectedCategory.asStateFlow()

    // Selected units (from/to dropdowns)
    private val _fromUnit = MutableStateFlow<ConversionUnit>(LengthUnit.MILES)
    val fromUnit: StateFlow<ConversionUnit> = _fromUnit.asStateFlow()

    private val _toUnit = MutableStateFlow<ConversionUnit>(LengthUnit.KILOMETERS)
    val toUnit: StateFlow<ConversionUnit> = _toUnit.asStateFlow()

    // Input / output values
    private val _inputValue = MutableStateFlow("")
    val inputValue: StateFlow<String> = _inputValue.asStateFlow()

    private val _outputValue = MutableStateFlow("")
    val outputValue: StateFlow<String> = _outputValue.asStateFlow()

    // Conversion history
    private val _history = MutableStateFlow<List<ConversionHistory>>(emptyList())
    val history: StateFlow<List<ConversionHistory>> = _history.asStateFlow()

    // Currency state
    private val _currencyState = MutableStateFlow<CurrencyState>(CurrencyState.Loading)
    val currencyState: StateFlow<CurrencyState> = _currencyState.asStateFlow()

    // Quick picks for current category
    val quickPicks: StateFlow<List<QuickPick>> get() = MutableStateFlow(
        quickPicksForCategory(_selectedCategory.value)
    )

    // User actions
    fun onCategorySelected(category: Category) {
        _selectedCategory.value = category

        // reset units to sensible defaults for the new category
        val defaults = defaultUnitsFor(category)
        _fromUnit.value = defaults.first
        _toUnit.value = defaults.second
        _inputValue.value = ""
        _outputValue.value = ""

        // fetch currency rate when currency tab is selected
        if (category == Category.CURRENCY) fetchCurrencyRate()
    }

    fun onInputChanged(input: String) {
        _inputValue.value = input
        calculate(input)
    }

    fun onFromUnitChanged(unit: ConversionUnit) {
        _fromUnit.value = unit
        calculate(_inputValue.value)
    }

    fun onToUnitChanged(unit: ConversionUnit) {
        _fromUnit.value = unit
        calculate(_inputValue.value)
    }

    fun onSwapUnits() {
        val temp = _fromUnit.value
        _fromUnit.value = _toUnit.value
        _toUnit.value = temp
        calculate(_inputValue.value)
    }

    fun onQuickPickSelected(pick: QuickPick) {
        _fromUnit.value = pick.from
        _toUnit.value = pick.to
        calculate(_inputValue.value)
    }

    // Core Calculation
    private fun calculate(input: String) {
        val value = input.toDoubleOrNull()
        if (value == null) {
            _outputValue.value = ""
            return
        }

        try {
            val result = when (_selectedCategory.value) {
                Category.CURRENCY -> {
                    val rate = (_currencyState.value as? CurrencyState.Success)?.usdToGbp
                        ?: return
                    when {
                        _fromUnit.value == CurrencyUnit.USD -> currencyRepository.usdToGbp(value, rate)
                        else -> currencyRepository.gbpToUsd(value, rate)
                    }
                }
                else -> Converter.convert(value, _fromUnit.value, _toUnit.value)
            }

            _outputValue.value = formatResult(result)

            // save to history
            val entry = ConversionHistory(
                result = ConversionResult(
                    inputValue = value,
                    outputValue = result,
                    fromUnit = _fromUnit.value,
                    toUnit = _toUnit.value,
                    category = _selectedCategory.value
                )
            )
            // keep last 50
            _history.value = listOf(entry) + _history.value.take(49)

        } catch (_: IllegalArgumentException) {
            _outputValue.value = ""
        }
    }

    // Currency fetch
    private fun fetchCurrencyRate() {
        _currencyState.value = CurrencyState.Loading
        viewModelScope.launch {
            val rate = currencyRepository.fetchUsdToGbpRate()
            _currencyState.value = if (rate != null) {
                CurrencyState.Success(rate)
            } else {
                CurrencyState.Unavailable
            }
        }
    }

    // Helpers
    private fun formatResult(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toLong().toString()
        } else {
            "%.4f".format(value).trimEnd('0').trimEnd('.')
        }
    }

    private fun defaultUnitsFor(category: Category): Pair<ConversionUnit, ConversionUnit> {
        return when (category) {
            Category.LENGTH -> LengthUnit.MILES to LengthUnit.KILOMETERS
            Category.WEIGHT -> WeightUnit.POUNDS to WeightUnit.KILOGRAMS
            Category.TEMPERATURE -> TemperatureUnit.CELSIUS to TemperatureUnit.FAHRENHEIT
            Category.VOLUME -> VolumeUnit.LITERS to VolumeUnit.US_GALLONS
            Category.CURRENCY -> CurrencyUnit.USD to CurrencyUnit.GBP
            Category.SPEED -> SpeedUnit.MPH to SpeedUnit.KMH
        }
    }

    private fun quickPicksForCategory(category: Category) : List<QuickPick> {
        return when (category) {
            Category.LENGTH -> QuickPicks.LENGTH
            Category.WEIGHT -> QuickPicks.WEIGHT
            Category.TEMPERATURE -> QuickPicks.TEMPERATURE
            Category.VOLUME -> QuickPicks.VOLUME
            Category.SPEED -> QuickPicks.SPEED
            Category.CURRENCY -> emptyList()
        }
    }

    override fun onCleared() {
        super.onCleared()
        currencyRepository.close()
    }
}