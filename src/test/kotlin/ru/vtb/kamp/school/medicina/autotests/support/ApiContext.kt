package ru.vtb.kamp.school.medicina.autotests.support

import io.restassured.RestAssured
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification

/**
 * Конфигурация и общий контекст одного сценария (cucumber-glue scope:
 * один экземпляр на сценарий, инжектится в шаги через PicoContainer).
 *
 * Параметры запуска (системные свойства, см. pom surefire):
 *   -DbaseUrl   базовый URL сервиса (по умолч. http://localhost:8083/v1)
 *   -DsessionId значение заголовка SessionID (требуется под профилем security)
 *   -DapiToken  bearer-токен (если сервис запущен с профилем security)
 */
class ApiContext {

    private val baseUrl: String = prop("baseUrl", "http://45.8.229.106:9080/v1")
    private val sessionId: String = prop("sessionId", "autotest-session")
    private val apiToken: String = prop("apiToken", "")

    /** Ответ последнего выполненного запроса — для шагов проверки. */
    var lastResponse: Response? = null

    /**
     * Переменные сценария: значения, сохранённые из предыдущих ответов
     * (например id свободного слота). Подставляются в тело запроса как ${имя}.
     */
    private val vars: MutableMap<String, String> = HashMap()

    /** Базовый request с общими заголовками (SessionID и, если задан, Bearer). */
    fun request(): RequestSpecification {
        val spec = RestAssured.given()
            .baseUri(baseUrl)
            .header("SessionID", sessionId)
            .contentType("application/json")
            .accept("application/json")
        if (apiToken.isNotBlank()) {
            spec.header("Authorization", "Bearer $apiToken")
        }
        return spec
    }

    /** Базовый request для multipart-загрузки: без contentType (RestAssured выставит сам). */
    fun multipartRequest(): RequestSpecification {
        val spec = RestAssured.given()
            .baseUri(baseUrl)
            .header("SessionID", sessionId)
            .accept("application/json")
        if (apiToken.isNotBlank()) {
            spec.header("Authorization", "Bearer $apiToken")
        }
        return spec
    }

    /** Сохранить значение переменной сценария. */
    fun putVar(name: String, value: String) {
        vars[name] = value
    }

    /** Подставить ${имя} сохранёнными значениями переменных сценария. */
    fun resolve(text: String): String {
        var result = text
        for ((key, value) in vars) {
            result = result.replace("\${$key}", value)
        }
        return result
    }

    companion object {
        private fun prop(key: String, def: String): String {
            val v = System.getProperty(key)
            return if (v.isNullOrBlank()) def else v
        }
    }
}
