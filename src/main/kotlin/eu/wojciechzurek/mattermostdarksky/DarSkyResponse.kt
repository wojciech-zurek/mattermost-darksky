package eu.wojciechzurek.mattermostdarksky

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class DarSkyResponse(
        val currently: CurrentlyResponse
)