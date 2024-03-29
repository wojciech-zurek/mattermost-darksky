package eu.wojciechzurek.mattermostdarksky.api

import com.fasterxml.jackson.annotation.JsonProperty

data class MattermostResponse(
        @JsonProperty("response_type")
        val responseType: String = "ephemeral",
        val text: String
)