package ru.vtb.kamp.school.medicina.autotests.support;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

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

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response response) {
        this.lastResponse = response;
    }
}
