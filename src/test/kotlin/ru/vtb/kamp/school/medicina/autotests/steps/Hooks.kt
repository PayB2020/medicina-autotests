package ru.vtb.kamp.school.medicina.autotests.steps

import io.cucumber.java.Before
import ru.vtb.kamp.school.medicina.autotests.support.ApiContext

/**
 * Пре-хуки сценариев. [ApiContext] инжектится PicoContainer —
 * тот же экземпляр, что и в шагах (glue-scope: один на сценарий).
 */
class Hooks(private val ctx: ApiContext) {

    /**
     * Сброс состояния перед каждым сценарием фичи @appointmentsV2:
     * отменяем сид-запись appt-001, чтобы создание новой начиналось «с чистого листа».
     * Ошибки игнорируем (записи могло не быть) — хук не должен ронять сценарий.
     */
    @Before("@appointmentsV2")
    fun resetAppointment() {
        runCatching {
            ctx.request()
                .body(
                    """{"patientId":"22222222-2222-2222-2222-222222222201",""" +
                        """"appointmentId":"appt-001","reason":"pre-test reset"}"""
                )
                .post("/appointments/cancel")
        }
    }
}
