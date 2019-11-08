package eu.wojciechzurek.mattermostdarksky

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.router
import java.text.SimpleDateFormat
import java.util.*


@SpringBootApplication
@EnableWebFlux
class MattermostDarkSkyApplication {

    @Bean
    fun routes(handler: WeatherHandler) = router {
        "/api".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/weather/current/{location}", handler::current)
                GET("/weather/daily/{location}", handler::daily)
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<MattermostDarkSkyApplication>(*args)
}

fun <T> loggerFor(clazz: Class<T>): Logger = LoggerFactory.getLogger(clazz)

fun Long.toStringDate(): String = SimpleDateFormat("dd-MM-yyyy").format(Date(this * 1000))