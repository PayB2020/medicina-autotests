# medicina-autotests

Автотесты REST API мок-сервиса **«Медицина — ВТБ Онлайн»** на Cucumber (BDD), на **Kotlin**.

- **Kotlin** (kotlin-maven-plugin) — язык step-кода.
- **Cucumber 7** (JUnit Platform engine) — сценарии на русском (`# language: ru`).
- **REST-assured** — HTTP-запросы и проверки.
- **AssertJ** — ассерты.
- **PicoContainer** — DI контекста между шагами.

## Предусловие

Сервис должен быть запущен (по умолчанию `http://localhost:8083/v1`).
См. основной репозиторий `medicina-test` (`./run.sh` или запуск из IDE).

## Запуск

```bash
mvn test
```

Параметры (системные свойства):

| Свойство     | Назначение                                   | По умолчанию               |
|--------------|----------------------------------------------|----------------------------|
| `baseUrl`    | базовый URL сервиса                          | `http://localhost:8083/v1` |
| `sessionId`  | значение заголовка `SessionID`               | `autotest-session`         |
| `apiToken`   | bearer-токен (если сервис под профилем `security`) | пусто                |

Примеры:

```bash
# другой хост/порт
mvn test -DbaseUrl=http://localhost:8083/v1

mvn test -DbaseUrl=http://45.8.229.106:9080/v1

# сервис запущен с профилем security (нужен токен)
mvn test -DapiToken=dev-secret-token
```

## Структура

| Путь | Назначение |
|---|---|
| `src/test/resources/features/*.feature` | сценарии (Gherkin, ru) |
| `src/test/kotlin/.../steps` | шаги (step definitions) |
| `src/test/kotlin/.../support/ApiContext.kt` | конфиг запуска + общий контекст сценария |
| `src/test/kotlin/.../RunCucumberTest.kt` | раннер JUnit Platform |

Отчёты после прогона: `target/cucumber-report.html`, `target/cucumber.json`.

## Allure-отчёт

Во время прогона результаты Allure пишутся в `target/allure-results`.

```bash
# 1) прогнать тесты
mvn test -DbaseUrl=http://45.8.229.106:9080/v1

# 1.1) c
mvn test -DbaseUrl=http://45.8.229.106:9080/v1 -Dcucumber.filter.tags="@smoke"

# 2) сгенерировать отчёт в target/site/allure-maven-plugin
mvn allure:report

# либо открыть отчёт в браузере на встроенном сервере
mvn allure:serve
```

> Для `allure:serve`/`allure:report` нужен доступ в интернет при первом запуске
> (Maven-плагин скачивает дистрибутив Allure версии `${allure.version}`).

