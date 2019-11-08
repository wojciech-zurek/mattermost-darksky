package eu.wojciechzurek.mattermostdarksky.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class DarSkyResponse(
        val currently: CurrentlyResponse?,
        val daily: DailyResponse?
)