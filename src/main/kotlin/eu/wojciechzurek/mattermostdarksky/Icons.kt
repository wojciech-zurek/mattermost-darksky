package eu.wojciechzurek.mattermostdarksky

enum class Icons(
        private val code: String,
        private val icon: String
) {
    CLEAR_DAY("clear-day", "☼"),
    CLEAR_NIGHT("clear-night", "☾"),
    RAIN("rain", "☂"),
    SNOW("snow", "❄☃"),
    SLEET("sleet", "❆ ☔"),
    WIND("wind", "♎"),
    FOG("fog", "♒"),
    CLOUDY("cloudy", "☁"),
    PARTLY_CLOUD_DAY("partly-cloudy-day", "☼☁"),
    PARTLY_CLOUD_NIGHT("partly-cloudy-night", "☾☁"),
    HAIL("hail", "☔"),
    THUNDERSTORM("thunderstorm", "☇"),
    TORNADO("tornado", "🌪"),
    UNKNOWN("unknown", "?");

    companion object {
        fun getIcon(code: String?): String = values().firstOrNull { it.code == code }?.icon ?: UNKNOWN.icon
    }
}