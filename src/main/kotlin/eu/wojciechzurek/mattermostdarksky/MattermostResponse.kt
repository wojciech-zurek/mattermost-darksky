package eu.wojciechzurek.mattermostdarksky

import com.fasterxml.jackson.annotation.JsonProperty

data class MattermostResponse(

        @JsonProperty("response_type")
        val responseType: String = "in_channel",
        val text: String
)