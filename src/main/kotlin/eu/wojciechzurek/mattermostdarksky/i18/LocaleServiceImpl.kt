package eu.wojciechzurek.mattermostdarksky.i18

import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.springframework.web.server.ServerWebExchange


@Service
class LocaleServiceImpl(
        private val messageSource: MessageSource,
        private val localeResolver: LocaleResolver
) : LocaleService {

    override fun getMessage(code: String, exchange: ServerWebExchange, vararg args: String?): String {
        val localeContext = localeResolver.resolveLocaleContext(exchange)
        return messageSource.getMessage(code, args, localeContext.locale!!)
    }
}