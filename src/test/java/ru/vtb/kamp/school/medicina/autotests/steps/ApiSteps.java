package ru.vtb.kamp.school.medicina.autotests.steps;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.То;
import io.restassured.response.Response;
import ru.vtb.kamp.school.medicina.autotests.support.ApiContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Общие шаги: отправка POST/GET-запросов к RPC-эндпоинтам и проверка ответа.
 * Контекст {@link ApiContext} инжектится PicoContainer (один на сценарий).
 */
public class ApiSteps {

    private final ApiContext ctx;
    private String requestBody = "{}";

    public ApiSteps(ApiContext ctx) {
        this.ctx = ctx;
    }

    @Дано("тело запроса:")
    public void телоЗапроса(String body) {
        this.requestBody = body;
    }

    @Дано("тело запроса с полями:")
    public void телоЗапросаСПолями(Map<String, String> fields) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (var e : fields.entrySet()) {
            if (!first) json.append(",");
            json.append("\"").append(e.getKey()).append("\":\"").append(e.getValue()).append("\"");
            first = false;
        }
        json.append("}");
        this.requestBody = json.toString();
    }

    @Когда("отправляю POST на {string}")
    public void отправляюPost(String path) {
        Response r = ctx.request().body(requestBody).post(path);
        ctx.setLastResponse(r);
    }

    @Когда("отправляю GET на {string}")
    public void отправляюGet(String path) {
        Response r = ctx.request().get(path);
        ctx.setLastResponse(r);
    }

    @То("код ответа {int}")
    public void кодОтвета(int expected) {
        assertThat(ctx.getLastResponse().statusCode()).isEqualTo(expected);
    }

    @То("в ответе поле {string} равно {string}")
    public void полеРавно(String jsonPath, String expected) {
        String actual = ctx.getLastResponse().jsonPath().getString(jsonPath);
        assertThat(actual).isEqualTo(expected);
    }

    @То("в ответе есть непустой массив {string}")
    public void непустойМассив(String jsonPath) {
        var list = ctx.getLastResponse().jsonPath().getList(jsonPath);
        assertThat(list).isNotNull().isNotEmpty();
    }

    @То("в ответе есть поле {string}")
    public void естьПоле(String jsonPath) {
        assertThat(ctx.getLastResponse().jsonPath().get(jsonPath)).isNotNull();
    }
}
