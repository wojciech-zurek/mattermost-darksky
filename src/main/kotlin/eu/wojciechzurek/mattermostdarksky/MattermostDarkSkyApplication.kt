package eu.wojciechzurek.mattermostdarksky

import eu.wojciechzurek.mattermostdarksky.i18.LocaleService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono


@SpringBootApplication
@EnableWebFlux
class MattermostDarkSkyApplication {

    @Bean
    fun routes(handler: WeatherHandler) = router {
        "/api".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/weather/current/{location}", handler::weather)
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<MattermostDarkSkyApplication>(*args)
}

fun <T> loggerFor(clazz: Class<T>): Logger = LoggerFactory.getLogger(clazz)

@Component
class WeatherHandler(
        private val localeService: LocaleService,
        @Value("\${darksky.api.key}")
        private val apiKey: String
) {

    private val webClient = WebClient.create("https://api.darksky.net/forecast/{api}/{location}/")

    private val icons = mapOf(
            "clear-day" to "☼",
            "clear-night" to "☾",
            "rain" to "☂",
            "snow" to "❄☃",
            "sleet" to "❆ ☔",
            "wind" to "♎",
            "fog" to "♒",
            "cloudy" to "☁",
            "partly-cloudy-day" to "☼☁",
            "partly-cloudy-night" to "☾☁",
            "hail" to "☔",
            "thunderstorm" to "☇",
            "tornado" to "🌪",
            "unknown" to "?"
    )

    fun weather(request: ServerRequest): Mono<ServerResponse> {
        val location = request.pathVariable("location")
        val apiKey = request.queryParam("apiKey").orElse(this.apiKey)
        return webClient
                .get()
                .uri("?lang=pl&exclude=minutely,hourly,daily,flags,alerts&units=auto", apiKey, location)
                .retrieve()
                .onStatus(HttpStatus::isError) { Mono.error(DarkSkyApiException(it.statusCode(), "Dark Sky Endpoint exception")) }
                .bodyToMono(DarSkyResponse::class.java)
                .map {
                    it.let { darkSky ->
                        MattermostResponse(
                                text = localeService.getMessage(
                                        "theme.current",
                                        request.exchange(),
                                        icons[darkSky.currently.icon],
                                        darkSky.currently.apparentTemperature.toString(),
                                        darkSky.currently.pressure.toString(),
                                        darkSky.currently.summary
                                )
                        )
                    }
                }
                .flatMap {
                    ok().bodyValue(it)
                }
    }
}

class DarkSkyApiException(val httpStatus: HttpStatus, message: String) : RuntimeException(message)

@Component
class GlobalErrorAttributes : DefaultErrorAttributes() {

    override fun getErrorAttributes(request: ServerRequest, includeStackTrace: Boolean): Map<String, Any> {
        val map = super.getErrorAttributes(request, includeStackTrace)

        val throwable = getError(request)
        if (throwable is DarkSkyApiException) {
            map["status"] = throwable.httpStatus.value()
            map["error"] = throwable.httpStatus.reasonPhrase
        }

        return map
    }
}