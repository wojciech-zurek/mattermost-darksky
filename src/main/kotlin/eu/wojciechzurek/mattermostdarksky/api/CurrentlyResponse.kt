package eu.wojciechzurek.mattermostdarksky.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CurrentlyResponse(
        val apparentTemperature: Float,
        val cloudCover: Int,
        val dewPoint : Float,
        val humidity: Int,
        val icon: String,
        val ozone: Float?,
        val precipIntensity: Float?,
        val precipProbability: Float?,
        val precipType: String?,
        val pressure : Int,
        val summary: String,
        val temperature: Float,
        val time: Long,
        val uvIndex: Int,
        val visibility : Float,
        val windBearing: Int?,
        val windGust: Float?,
        val windSpeed: Float
)