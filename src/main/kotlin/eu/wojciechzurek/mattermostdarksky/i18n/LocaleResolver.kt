package eu.wojciechzurek.mattermostdarksky.i18n

import org.springframework.context.i18n.LocaleContext
import org.springframework.context.i18n.SimpleLocaleContext
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.i18n.LocaleContextResolver
import java.util.*

@Component
class LocaleResolver : LocaleContextResolver{

    override fun setLocaleContext(exchange: ServerWebExchange, localeContext: LocaleContext?) {
        throw UnsupportedOperationException("Not Supported")
    }

    override fun resolveLocaleContext(exchange: ServerWebExchange): LocaleContext {

        val locale = when (val language = exchange.request.queryParams.getFirst("lang")) {
            null -> Locale.getDefault()
            else -> Locale.forLanguageTag(language)
        }

        return SimpleLocaleContext(locale)
    }
}

