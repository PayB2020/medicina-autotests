# medicina-autotests

Автотесты REST API мок-сервиса **«Медицина — ВТБ Онлайн»** на Cucumber (BDD).

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
mvn test -DbaseUrl=http://localhost:9000/v1

# сервис запущен с профилем security (нужен токен)
mvn test -DapiToken=dev-secret-token
```

## Структура

| Путь | Назначение |
|---|---|
| `src/test/resources/features/*.feature` | сценарии (Gherkin, ru) |
| `src/test/java/.../steps` | шаги (step definitions) |
| `src/test/java/.../support/ApiContext.java` | конфиг запуска + общий контекст сценария |
| `src/test/java/.../RunCucumberTest.java` | раннер JUnit Platform |

Отчёты после прогона: `target/cucumber-report.html`, `target/cucumber.json`.
