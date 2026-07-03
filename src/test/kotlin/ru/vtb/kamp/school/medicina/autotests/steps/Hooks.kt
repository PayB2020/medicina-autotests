package ru.vtb.kamp.school.medicina.autotests.steps

import io.cucumber.java.After
import io.cucumber.java.Before
import ru.vtb.kamp.school.medicina.autotests.support.ApiContext
import ru.vtb.kamp.school.medicina.autotests.support.Db

/**
 * Пре-хуки сценариев. [ApiContext] инжектится PicoContainer —
 * тот же экземпляр, что и в шагах (glue-scope: один на сценарий).
 */
class Hooks(private val ctx: ApiContext) {

    /** Доступ к БД для выполнения SQL в хуках (jdbc:postgresql://45.8.229.106:9432/medcard). */
    private val db = Db()

    /**
     * Глобальный пре-хук: выполняется ПЕРЕД КАЖДЫМ сценарием (без тега — на все фичи).
     * Сбрасываем сид-запись appt-001, чтобы тест начинался «с чистого листа».
     * Ошибки игнорируем — хук не должен ронять сценарий.
     */
    @Before
    fun beforeAny() {
        // Пример выполнения SQL перед тестом (readonly-аккаунт — только SELECT).
        runCatching {
            db.execute("update patients set is_active= false;")
            db.execute("update patients set is_active= true where id = '22222222-2222-2222-2222-222222222777'")
        }.onFailure { println(">> [beforeAny] SQL пропущен: ${it.message}") }

    }

    /**
     * Сброс состояния после каждым сценарием фичи @appointmentsV2:
     * отменяем сид-запись appt-001, чтобы создание новой начиналось «с чистого листа».
     * Ошибки игнорируем (записи могло не быть) — хук не должен ронять сценарий.
     */
    @After("@appointmentsV2")
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
