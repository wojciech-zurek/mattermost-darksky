package eu.wojciechzurek.mattermostdarksky.i18n

import org.springframework.web.server.ServerWebExchange



interface LocaleService {

    fun getMessage(code: String, exchange: ServerWebExchange, vararg args: String?): String
}