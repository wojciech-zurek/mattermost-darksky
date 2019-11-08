package eu.wojciechzurek.mattermostdarksky.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class DailyDetailsResponse (
        val time: Long,
        val icon: String,
        val humidity: Float,
        val ozone: Float,
        val pressure: Float,
        val sunriseTime: Float,
        val sunsetTime: Float,
        val temperatureMax: Float,
        val temperatureMin: Float,
        val uvIndex: Int,
        val visibility: Float,
        val windSpeed: Float,
        val summary: String,
        val cloudCover: Float,
        val precipType: String?,
        val precipProbability: Float?,
        val precipIntensity: Float?

)