package ru.vtb.kamp.school.medicina.autotests.steps;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.То;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.vtb.kamp.school.medicina.autotests.support.ApiContext;

import java.nio.charset.StandardCharsets;
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
        Response r = ctx.request().body(ctx.resolve(requestBody)).post(path);
        ctx.setLastResponse(r);
    }

    @Когда("отправляю PUT на {string}")
    public void отправляюPut(String path) {
        Response r = ctx.request().body(ctx.resolve(requestBody)).put(path);
        ctx.setLastResponse(r);
    }

    @Когда("отправляю GET на {string}")
    public void отправляюGet(String path) {
        Response r = ctx.request().get(path);
        ctx.setLastResponse(r);
    }

    @Когда("загружаю документ на {string} с полями:")
    public void загружаюДокумент(String path, Map<String, String> fields) {
        RequestSpecification spec = ctx.multipartRequest();
        for (var e : fields.entrySet()) {
            spec.multiPart(e.getKey(), ctx.resolve(e.getValue()));
        }
        // Один тестовый файл в памяти — содержимое неважно, мок считает только размер.
        spec.multiPart("files", "autotest.txt",
                "autotest-content".getBytes(StandardCharsets.UTF_8), "text/plain");
        ctx.setLastResponse(spec.post(path));
    }

    @И("сохраняю поле {string} из ответа как {string}")
    public void сохраняюПоле(String jsonPath, String varName) {
        String value = ctx.getLastResponse().jsonPath().getString(jsonPath);
        assertThat(value).as("значение поля %s для сохранения в %s", jsonPath, varName).isNotNull();
        ctx.putVar(varName, value);
    }

    @И("сохраняю id свободного слота из ответа как {string}")
    public void сохраняюСвободныйСлот(String varName) {
        // Расписание: days[].periods[].slots[]. Берём id первого слота с available == true.
        String slotId = ctx.getLastResponse().jsonPath()
                .getString("days.periods.slots.flatten().findAll { it.available == true }.id[0]");
        assertThat(slotId).as("в расписании нет свободных слотов").isNotNull();
        ctx.putVar(varName, slotId);
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
        Object value = ctx.getLastResponse().jsonPath().get(jsonPath);
        assertThat(value)
                .as("поле %s присутствует в ответе", jsonPath)
                .isNotNull();
    }

    @То("тип содержимого ответа содержит {string}")
    public void типСодержимого(String expected) {
        assertThat(ctx.getLastResponse().getContentType()).contains(expected);
    }
}
