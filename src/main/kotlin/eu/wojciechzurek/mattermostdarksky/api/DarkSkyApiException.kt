package eu.wojciechzurek.mattermostdarksky.api

import org.springframework.http.HttpStatus

class DarkSkyApiException(val httpStatus: HttpStatus, message: String) : RuntimeException(message)