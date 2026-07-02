package ru.vtb.kamp.school.medicina.autotests.support

import java.sql.ResultSet
import java.sql.DriverManager

/**
 * Выполнение SQL по JDBC. Реквизиты по умолчанию — база medcard,
 * переопределяются системными свойствами -DdbUrl / -DdbUser / -DdbPassword.
 *
 * Внимание: аккаунт medcard_readonly имеет права только на чтение —
 * корректны SELECT-запросы; INSERT/UPDATE/DELETE вернут "permission denied".
 */
class Db(
    private val url: String = prop("dbUrl", "jdbc:postgresql://45.8.229.106:9432/medcard"),
    private val user: String = prop("dbUser", "medcard_readonly"),
    private val password: String = prop("dbPassword", "medcard_readonly_pass"),
) {

    /**
     * Выполнить SELECT и смапить строки результата.
     * Соединение открывается и закрывается на каждый вызов (для хуков этого достаточно).
     */
    fun <T> query(sql: String, mapper: (ResultSet) -> T): List<T> {
        DriverManager.getConnection(url, user, password).use { conn ->
            conn.prepareStatement(sql).use { st ->
                st.executeQuery().use { rs ->
                    val result = ArrayList<T>()
                    while (rs.next()) result += mapper(rs)
                    return result
                }
            }
        }
    }

    /** Выполнить произвольный SQL без возврата данных (для readonly-аккаунта — только чтение). */
    fun execute(sql: String) {
        DriverManager.getConnection(url, user, password).use { conn ->
            conn.createStatement().use { it.execute(sql) }
        }
    }

    companion object {
        private fun prop(key: String, def: String): String {
            val v = System.getProperty(key)
            return if (v.isNullOrBlank()) def else v
        }
    }
}
