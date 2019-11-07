package eu.wojciechzurek.mattermostdarksky

import eu.wojciechzurek.mattermostdarksky.i18.LocaleService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono


@SpringBootApplication
@EnableWebFlux
class MattermostDarkSkyApplication {

    @Bean
    fun routesSGB(handler: WeatherHandler) = router {
        "/api".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/weather/{location}", handler::weather)
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<MattermostDarkSkyApplication>(*args)
}

@Component
class WeatherHandler(
        private val localeService: LocaleService,
        @Value("\${darksky.api.key}")
        private val apiKey: String
) {

    private val webClient = WebClient.create("https://api.darksky.net/forecast/{api}/{localization}/")

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
        return webClient
                .get()
                .uri("?lang=pl&exclude=minutely,hourly,daily,flags,alerts&units=auto", apiKey, location)
                .exchange()
                .map { it.bodyToMono(DarSkyResponse::class.java) }
                .map {
                    it.map { darkSkyResponse ->
                        darkSkyResponse.let { darkSky ->
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
                }
                .flatMap {
                    ok().body(it)
                }
    }
}
