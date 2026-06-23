package ru.vtb.kamp.school.medicina.autotests.support;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация и общий контекст одного сценария (cucumber-glue scope:
 * один экземпляр на сценарий, инжектится в шаги через PicoContainer).
 *
 * Параметры запуска (системные свойства, см. pom surefire):
 *   -DbaseUrl   базовый URL сервиса (по умолч. http://localhost:8083/v1)
 *   -DsessionId значение заголовка SessionID (требуется под профилем security)
 *   -DapiToken  bearer-токен (если сервис запущен с профилем security)
 */
public class ApiContext {

    private final String baseUrl;
    private final String sessionId;
    private final String apiToken;

    /** Ответ последнего выполненного запроса — для шагов проверки. */
    private Response lastResponse;

    /**
     * Переменные сценария: значения, сохранённые из предыдущих ответов
     * (например id свободного слота). Подставляются в тело запроса как ${имя}.
     */
    private final Map<String, String> vars = new HashMap<>();

    public ApiContext() {
        this.baseUrl = prop("baseUrl", "http://localhost:8083/v1");
        this.sessionId = prop("sessionId", "autotest-session");
        this.apiToken = prop("apiToken", "");
    }

    private static String prop(String key, String def) {
        String v = System.getProperty(key);
        return (v == null || v.isBlank()) ? def : v;
    }

    /** Базовый request с общими заголовками (SessionID и, если задан, Bearer). */
    public RequestSpecification request() {
        RequestSpecification spec = RestAssured.given()
                .baseUri(baseUrl)
                .header("SessionID", sessionId)
                .contentType("application/json")
                .accept("application/json");
        if (!apiToken.isBlank()) {
            spec.header("Authorization", "Bearer " + apiToken);
        }
        return spec;
    }

    /** Базовый request для multipart-загрузки: без contentType (RestAssured выставит сам). */
    public RequestSpecification multipartRequest() {
        RequestSpecification spec = RestAssured.given()
                .baseUri(baseUrl)
                .header("SessionID", sessionId)
                .accept("application/json");
        if (!apiToken.isBlank()) {
            spec.header("Authorization", "Bearer " + apiToken);
        }
        return spec;
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response response) {
        this.lastResponse = response;
    }

    /** Сохранить значение переменной сценария. */
    public void putVar(String name, String value) {
        vars.put(name, value);
    }

    /** Подставить ${имя} сохранёнными значениями переменных сценария. */
    public String resolve(String text) {
        String result = text;
        for (var e : vars.entrySet()) {
            result = result.replace("${" + e.getKey() + "}", e.getValue());
        }
        return result;
    }
}
