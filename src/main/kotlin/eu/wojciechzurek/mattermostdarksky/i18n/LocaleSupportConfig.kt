package eu.wojciechzurek.mattermostdarksky.i18

import org.springframework.context.annotation.Configuration
import org.springframework.context.i18n.LocaleContext
import org.springframework.context.i18n.SimpleLocaleContext
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.i18n.LocaleContextResolver
import java.util.*

//@Configuration
//class LocaleSupportConfig : DelegatingWebFluxConfiguration() {
//
//    override fun createLocaleContextResolver(): LocaleContextResolver {
//        return LocaleResolver()
//    }
//}