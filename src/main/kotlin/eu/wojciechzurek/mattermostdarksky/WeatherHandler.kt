package eu.wojciechzurek.mattermostdarksky

import eu.wojciechzurek.mattermostdarksky.api.DarSkyResponse
import eu.wojciechzurek.mattermostdarksky.api.DarkSkyApiException
import eu.wojciechzurek.mattermostdarksky.api.MattermostResponse
import eu.wojciechzurek.mattermostdarksky.i18n.LocaleService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class WeatherHandler(
        private val localeService: LocaleService,
        @Value("\${darksky.api.key}")
        private val apiKey: String
) {

    private val logger = loggerFor(this.javaClass)
    private val webClient = WebClient.builder()
            .baseUrl("https://api.darksky.net/forecast/{apiKey}/{location}/")
            .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "gzip")
            .build()

    fun current(request: ServerRequest) = weather(request, WeatherType.CURRENT)
    fun daily(request: ServerRequest) = weather(request, WeatherType.DAILY)

    private fun weather(request: ServerRequest, weatherType: WeatherType): Mono<ServerResponse> {
        val location = request.pathVariable("location")
        val apiKey = request.queryParam("apiKey").orElse(this.apiKey)
        val lang = request.queryParam("lang").orElse("pl")
        val units = request.queryParam("units").orElse("auto")
        val receiver = when (request.queryParam("text").orElse("ephemeral")) {
            "channel" -> "in_channel"
            "kanal" -> "in_channel"//polish channel
            else -> "ephemeral"
        }

        return webClient
                .get()
                .uri(weatherType.uri, apiKey, location, lang, units)
                .retrieve()
                .onStatus(HttpStatus::isError) {
                    it.bodyToMono<String>().subscribe { body -> logger.error(body) }
                    Mono.error(DarkSkyApiException(it.statusCode(), "Dark Sky Endpoint exception"))
                }
                .bodyToMono(DarSkyResponse::class.java)
                .map { weatherType.getText(it, localeService, request.exchange()) }
                .map { MattermostResponse(receiver, it) }
                .flatMap { ServerResponse.ok().bodyValue(it) }
    }
}