package ru.vtb.kamp.school.medicina.autotests.steps

import io.cucumber.java.ru.Дано
import io.cucumber.java.ru.И
import io.cucumber.java.ru.Когда
import io.cucumber.java.ru.То
import org.assertj.core.api.Assertions.assertThat
import ru.vtb.kamp.school.medicina.autotests.support.ApiContext
import java.nio.charset.StandardCharsets

/**
 * Общие шаги: отправка POST/GET-запросов к RPC-эндпоинтам и проверка ответа.
 * Контекст [ApiContext] инжектится PicoContainer (один на сценарий).
 */
class ApiSteps(private val ctx: ApiContext) {

    private var requestBody = "{}"

    private fun lastResponse() = ctx.lastResponse ?: error("ответ ещё не получен")

    @Дано("тело запроса:")
    fun setRequestBody(body: String) {
        this.requestBody = body
    }

    @Дано("тело запроса с полями:")
    fun setRequestBodyFromFields(fields: Map<String, String>) {
        this.requestBody = fields.entries.joinToString(
            prefix = "{", postfix = "}", separator = ","
        ) { "\"${it.key}\":\"${it.value}\"" }
    }

    @Когда("отправляю POST на {string}")
    fun sendPost(path: String) {
        ctx.lastResponse = ctx.request().body(ctx.resolve(requestBody)).post(path)
    }

    @Когда("отправляю PUT на {string}")
    fun sendPut(path: String) {
        ctx.lastResponse = ctx.request().body(ctx.resolve(requestBody)).put(path)
    }

    @Когда("отправляю GET на {string}")
    fun sendGet(path: String) {
        ctx.lastResponse = ctx.request().get(path)
    }

    @Когда("загружаю документ на {string} с полями:")
    fun uploadDocument(path: String, fields: Map<String, String>) {
        val spec = ctx.multipartRequest()
        for ((key, value) in fields) {
            spec.multiPart(key, ctx.resolve(value))
        }
        // Один тестовый файл в памяти — содержимое неважно, мок считает только размер.
        spec.multiPart(
            "files", "autotest.txt",
            "autotest-content".toByteArray(StandardCharsets.UTF_8), "text/plain"
        )
        ctx.lastResponse = spec.post(path)
    }

    @И("сохраняю поле {string} из ответа как {string}")
    fun saveResponseField(jsonPath: String, varName: String) {
        val value = lastResponse().jsonPath().getString(jsonPath)
        assertThat(value).`as`("значение поля %s для сохранения в %s", jsonPath, varName).isNotNull()
        ctx.putVar(varName, value)
    }

    @И("сохраняю id свободного слота из ответа как {string}")
    fun saveFreeSlotId(varName: String) {
        // Расписание: days[].periods[].slots[]. Берём id первого слота с available == true.
        val slotId = lastResponse().jsonPath()
            .getString("days.periods.slots.flatten().findAll { it.available == true }.id[0]")
        assertThat(slotId).`as`("в расписании нет свободных слотов").isNotNull()
        ctx.putVar(varName, slotId)
    }

    @То("код ответа {int}")
    fun responseStatusCode(expected: Int) {
        assertThat(lastResponse().statusCode()).isEqualTo(expected)
    }

    @То("в ответе поле {string} равно {string}")
    fun responseFieldEquals(jsonPath: String, expected: String) {
        val actual = lastResponse().jsonPath().getString(jsonPath)
        assertThat(actual).isEqualTo(expected)
    }

    @То(value = "в ответе есть текст {string}")
    fun responseContainsText(expected: String) {
        assertThat(lastResponse().body().asString()).contains(expected)
    }

    @То("в ответе есть непустой массив {string}")
    fun responseHasNonEmptyArray(jsonPath: String) {
        val list = lastResponse().jsonPath().getList<Any>(jsonPath)
        assertThat(list).isNotNull().isNotEmpty()
    }

    @То("в ответе есть поле {string}")
    fun responseHasField(jsonPath: String) {
        val value = lastResponse().jsonPath().get<Any>(jsonPath)
        assertThat(value).`as`("поле %s присутствует в ответе", jsonPath).isNotNull()
    }

    @То("тип содержимого ответа содержит {string}")
    fun responseContentTypeContains(expected: String) {
        assertThat(lastResponse().contentType).contains(expected)
    }
}
