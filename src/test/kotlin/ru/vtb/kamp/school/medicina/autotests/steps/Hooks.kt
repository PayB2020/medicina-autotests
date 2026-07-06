package ru.vtb.kamp.school.medicina.autotests.steps

import io.cucumber.java.After
import io.cucumber.java.AfterAll
import io.cucumber.java.Before
import io.cucumber.java.BeforeAll
import ru.vtb.kamp.school.medicina.autotests.support.ApiContext
import ru.vtb.kamp.school.medicina.autotests.support.Db

/**
 * Пре-хуки сценариев. [ApiContext] инжектится PicoContainer —
 * тот же экземпляр, что и в шагах (glue-scope: один на сценарий).
 */
/**
 * Глобальный пре-хук: выполняется ОДИН РАЗ перед всеми сценариями всего прогона.
 * Сбрасываем сид-запись appt-001, чтобы прогон начинался «с чистого листа».
 * Ошибки игнорируем — хук не должен ронять прогон.
 *
 * Объявлен как top-level функция (не в companion object): @JvmStatic в companion
 * object оставляет на Hooks$Companion нестатический метод-мост с той же
 * аннотацией, который Cucumber тоже сканирует и отвергает как невалидный.
 */
@BeforeAll
fun beforeAll() {
    runCatching {
        val db = Db()
        db.execute("update patients set is_active= false;")
        db.execute("insert into patients (id, first_name, last_name, middle_name, last_name_initial, age_label, birth_date, gender, phone,\n" +
                "                      oms, snils, relation, oms_attached, attached_clinic_id, is_active)\n" +
                "values ('22222222-2222-2222-2222-222222222777','Иван','Иванович','Иванов','В.','33 года','1993-03-05','male','+79031234561','7701000000000023','214-998-477 16','adult',true,'11111111-1111-1111-1111-111111111101',true\n" +
                "       );")
    }.onFailure { println(">> [beforeAll] SQL пропущен: ${it.message}") }
}

@AfterAll
fun afterAll() {
    runCatching {
        val db = Db()
        db.execute("update patients set is_active= false;")
        db.execute("update patients set is_active= true where id = '22222222-2222-2222-2222-222222222201'")
        db.execute("delete from appointments where patient_id = '22222222-2222-2222-2222-222222222777' ")
        db.execute("delete from patients where id = '22222222-2222-2222-2222-222222222777' ")
    }.onFailure { println(">> [beforeAll] SQL пропущен: ${it.message}") }
}

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
            db.execute("delete  from service_appointments where patient_id in (select id from patients where last_name = 'Веремеенко');")
            db.execute("delete  from appointments where patient_id in (select id from patients where last_name = 'Веремеенко');")
            db.execute("delete  from patients where last_name = 'Веремеенко'")
        }.onFailure { println(">> [beforeAny] SQL пропущен: ${it.message}") }
    }
//    @After("@appointmentsV2")
//    fun resetAppointment() {
//        runCatching {
//            ctx.request()
//                .body(
//                    """{"patientId":"22222222-2222-2222-2222-222222222201",""" +
//                            """"appointmentId":"appt-001","reason":"pre-test reset"}"""
//                )
//                .post("/appointments/cancel")
//        }
//    }
}
