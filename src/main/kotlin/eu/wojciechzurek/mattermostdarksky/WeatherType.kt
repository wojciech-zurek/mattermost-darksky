package eu.wojciechzurek.mattermostdarksky

import eu.wojciechzurek.mattermostdarksky.api.DarSkyResponse
import eu.wojciechzurek.mattermostdarksky.i18.LocaleService
import org.springframework.web.server.ServerWebExchange
import kotlin.math.roundToInt

enum class WeatherType(val uri: String) : Extractor {

    CURRENT("?lang={lang}&exclude=minutely,hourly,daily,flags,alerts&units={units}") {
        override fun getText(response: DarSkyResponse, localeService: LocaleService, exchange: ServerWebExchange): String {

            return localeService.getMessage(
                    "theme.current",
                    exchange,
                    Icons.getIcon(response.currently?.icon),
                    response.currently?.temperature?.roundToInt().toString(),
                    response.currently?.pressure.toString(),
                    response.currently?.summary
            )
        }
    },
    DAILY("?lang={lang}&exclude=currently,minutely,hourly,flags,alerts&units={units}") {
        override fun getText(response: DarSkyResponse, localeService: LocaleService, exchange: ServerWebExchange): String {
            val rows = response.daily?.data?.joinToString(separator = "") { daily ->
                localeService.getMessage(
                        "theme.daily.row",
                        exchange,
                        daily.time.toStringDate(),
                        Icons.getIcon(daily.icon),
                        daily.summary,
                        daily.temperatureMin.roundToInt().toString(),
                        daily.temperatureMax.roundToInt().toString(),
                        daily.pressure.roundToInt().toString()
                )
            }

            return localeService.getMessage(
                    "theme.daily",
                    exchange,
                    rows)
        }
    };
}

interface Extractor {
    fun getText(response: DarSkyResponse, localeService: LocaleService, exchange: ServerWebExchange): String
}