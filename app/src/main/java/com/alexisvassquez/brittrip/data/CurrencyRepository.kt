package com.alexisvassquez.brittrip.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/*
* CurrencyRepository
*
* Fetches the live USD -> GBP exchange rate from the Frankfurter
* API (ECB-sourced, free, no key).
* Returns null gracefully on network failure or parse error,
* the UI handles the fallback.
*
* All network calls are suspend functions.
* Calls from a coroutine/ViewModel
*/

class CurrencyRepository {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private val json = Json { ignoreUnknownKeys = true }

    companion object {
        private const val BASE_URL = "https://api.frankfurter.dev/v1/latest?base=USD&symbols=GBP"
    }

    // Fetches the live USD -> GBP rate.
    // @return exchange rate as Double, or null if unavailable.
    suspend fun fetchUsdToGbpRate(): Double? {
        return try {
            val response = client.get(BASE_URL)
            if (!response.status.isSuccess()) return null

            val body = response.bodyAsText()
            val jsonElement = json.parseToJsonElement(body)

            jsonElement
                .jsonObject["rates"]
                ?.jsonObject["GBP"]
                ?.jsonPrimitive
                ?.doubleOrNull

        } catch (_: Exception) {
            // network failure, timeout, parse error - all handled gracefully
            null
        }
    }

    // Converts USD to GBP given a live rate.
    fun usdToGbp(amount: Double, rate: Double): Double = amount * rate

    // Converts GBP to USD given a live rate
    fun gbpToUsd(amount: Double, rate: Double): Double = amount / rate

    // Clean up the Ktor client when no longer needed
    // Call from ViewModel's onCleared()
    fun close() = client.close()
}