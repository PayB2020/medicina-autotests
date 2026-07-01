# language: ru
Функция: Записи к врачу - Расширенный тест
  Создание, просмотр, изменение и отмена записей; списки записей и направлений.

#Все цели обращения (appointment/goals/list) ->


  Сценарий: Все цели обращения 200
    Дано тело запроса:
      """
      {
          "patientId": "string"
      }
      """
    Когда отправляю POST на "appointment/goals/list"
    То код ответа 200

  Сценарий: Все цели обращения 400
    Дано тело запроса:
      """
      {

      }
      """
    Когда отправляю POST на "appointment/goals/list"
    То код ответа 400

  Сценарий: Все цели обращения 405
    Дано тело запроса:
      """
      {
          "patientId": "string"
      }
      """
    Когда отправляю GET на "appointment/goals/list"
    То код ответа 405

    # ^^^ Все цели обращения (appointment/goals/list) ^^^

    # -> Активные направления пациента (appointment/referrals/list) ->

  Сценарий: Активные направления пациентов 200
    Дано тело запроса:
      """
      {
          "patientId": "string"
      }
      """
    Когда отправляю POST на "appointment/referrals/list"
    То код ответа 200

  Сценарий: Активные направления пациентов 400
    Дано тело запроса:
      """
      {

      }
      """
    Когда отправляю POST на "appointment/referrals/list"
    То код ответа 400

  Сценарий: Активные направления пациентов 405
    Дано тело запроса:
      """
      {
          "patientId": "string"
      }
      """
    Когда отправляю GET на "appointment/referrals/list"
    То код ответа 405

    # ^^^ Активные направления пациента (appointment/referrals/list) ^^^

    # -> Отмена записи (appointment/cancel) ->

  Сценарий: Отмена записи (предварительный просмотр слота для записи, ее создание и отмена) 200
    Дано тело запроса:
      """
      {
          "patientId": "22222222-2222-2222-2222-222222222201",
          "appointmentId": "appt-001",
          "reason": "string"
      }
      """
    Когда отправляю POST на "/appointments/cancel"
    То код ответа 200
    Дано тело запроса:
      """
      {
        "patientId": "string",
        "doctorId": "doc-001",
        "days": 14
      }
      """
    Когда отправляю POST на "/doctors/schedule"
    То код ответа 200
    И сохраняю id свободного слота из ответа как "slotId"
    Дано тело запроса:
      """
      {
          "doctorId": "doc-001",
          "slotId": "${slotId}",
          "patientId": "22222222-2222-2222-2222-222222222201",
          "goalId": "goal-sick",
          "referralId": "string",
          "complaints": "string"
      }
      """
    Когда отправляю POST на "/appointments/create"
    То код ответа 201
    Дано тело запроса:
      """
      {
          "patientId": "22222222-2222-2222-2222-222222222201",
          "appointmentId": "appt-001",
          "reason": "string"
      }
      """
    Когда отправляю POST на "/appointments/cancel"
    То код ответа 200

  Сценарий: Отмена записи 400
    Дано тело запроса:
      """

      """
    Когда отправляю POST на "appointments/cancel"
    То код ответа 400

  Сценарий: Отмена записи 404
    Дано тело запроса:
      """
      {
          "patientId": "",
          "appointmentId": "",
          "reason": "string"
      }
      """
    Когда отправляю POST на "appointments/cancel"
    То код ответа 404

  Сценарий: Отмена записи 405
    Дано тело запроса:
      """
      {
          "patientId": "",
          "appointmentId": "",
          "reason": "string"
      }
      """
    Когда отправляю GET на "appointments/cancel"
    То код ответа 405

  # ^^^ Отмена записи (appointment/cancel) ^^^

  # -> Создание записи (appointments/create) ->

  Сценарий: Создание записи 200
    Дано тело запроса:
      """
      {
          "patientId": "22222222-2222-2222-2222-222222222201",
          "appointmentId": "appt-001",
          "reason": "string"
      }
      """
    Когда отправляю POST на "/appointments/cancel"
    То код ответа 200
    Дано тело запроса:
      """
      {
        "patientId": "22222222-2222-2222-2222-222222222201",
        "doctorId": "doc-001",
        "days": 14
      }
      """
    Когда отправляю POST на "/doctors/schedule"
    То код ответа 200
    И сохраняю id свободного слота из ответа как "slotId"
    Дано тело запроса:
    """
      {
        "doctorId": "doc-001",
        "slotId": "${slotId}",
        "patientId": "22222222-2222-2222-2222-222222222201",
        "goalId": "goal-sick"
      }
      """
    Когда отправляю POST на "/appointments/create"
    То код ответа 201
    И сохраняю поле "id" из ответа как "appointmentsId"
    Дано тело запроса:
      """
      {
          "patientId": "22222222-2222-2222-2222-222222222201",
          "appointmentId": "${appointmentsId}",
          "reason": "string"
      }
      """
    Когда отправляю POST на "/appointments/cancel"
    То код ответа 200

  Сценарий: Создание записи - 404
    Дано тело запроса:
      """
      {
          "doctorId": "string",
          "slotId": "string",
          "patientId": "string",
          "goalId": "string",
          "referralId": "string",
          "complaints": "string"
      }
      """
    Когда отправляю POST на "/appointments/create"
    То код ответа 404

  Сценарий: Создание записи - 405
    Дано тело запроса:
      """
      {
          "doctorId": "string",
          "slotId": "string",
          "patientId": "string",
          "goalId": "string",
          "referralId": "string",
          "complaints": "string"
      }
      """
    Когда отправляю GET на "/appointments/create"
    То код ответа 405

    Сценарий: Создание записи - 409
      Дано тело запроса:
      """
      {
          "doctorId": "doc-001",
          "slotId": "slot-doc-001-2026-06-30-19",
          "patientId": "22222222-2222-2222-2222-222222222201",
          "goalId": "goal-sick",
          "referralId": "string",
          "complaints": "string"
      }
      """
      Когда отправляю POST на "/appointments/create"
      То код ответа 409

    # ^^^ Создание записи (appointments/create) ^^^

    # -> Карточка записи (appointments/get) ->

  Сценарий: Карточка записи 200
    Дано тело запроса:
      """
      {
          "patientId": "22222222-2222-2222-2222-222222222201",
          "appointmentId": "appt-e8bf3ae9-5375-4621-8b83-f0a63b79cd4b"
      }
      """
    Когда отправляю POST на "/appointments/get"
    То код ответа 200

  Сценарий: Карточка записи 400
    Дано тело запроса:
      """
      {

      }
      """
    Когда отправляю POST на "/appointments/get"
    То код ответа 400

  Сценарий: Карточка записи 404
    Дано тело запроса:
      """
        {
            "patientId": "string",
            "appointmentId": "string"
        }
      """
    Когда отправляю POST на "/appointments/get"
    То код ответа 404

  Сценарий: Карточка записи 405
    Дано тело запроса:
      """
        {
            "patientId": "string",
            "appointmentId": "string"
        }
      """
    Когда отправляю GET на "/appointments/get"
    То код ответа 405

    #^^^ Карточка записи (/appointments/get) ^^^

    #-> Список записей пациента ->

  Сценарий: Список записей пациента 200
    Дано тело запроса:
      """
      {
          "patientId": "string",
          "status": "upcoming"
      }
      """
    Когда отправляю POST на "/appointments/list"
    То код ответа 200

  Сценарий: Список записей пациента 400
    Дано тело запроса:
      """
      {
          "patientId": "",
          "status": ""
      }
      """
    Когда отправляю POST на "/appointments/list"
    То код ответа 400

  Сценарий: Список записей пациента 405
    Дано тело запроса:
      """
      {
          "patientId": "",
          "status": ""
      }
      """
    Когда отправляю GET на "/appointments/list"
    То код ответа 405

  # ^^^ Список записей пациента (appointments/list) ^^^

  # -> Изменить настройки записи ->

  Сценарий: Изменение настроек записи 200
    Дано тело запроса:
      """
      {
          "patientId": "22222222-2222-2222-2222-222222222201",
          "appointmentId": "appt-001",
          "reason": "string"
      }
      """
    Когда отправляю POST на "/appointments/cancel"
    То код ответа 200
    Дано тело запроса:
      """
      {
        "patientId": "22222222-2222-2222-2222-222222222201",
        "doctorId": "doc-001",
        "days": 14
      }
      """
    Когда отправляю POST на "/doctors/schedule"
    То код ответа 200
    И сохраняю id свободного слота из ответа как "slotId"
    Дано тело запроса:
    """
      {
        "doctorId": "doc-001",
        "slotId": "${slotId}",
        "patientId": "22222222-2222-2222-2222-222222222201",
        "goalId": "goal-sick"
      }
      """
    Когда отправляю POST на "/appointments/create"
    То код ответа 201
    И сохраняю поле "id" из ответа как "appointmentsId"
    Дано тело запроса:
      """
      {
        "patientId": "22222222-2222-2222-2222-222222222201",
        "doctorId": "doc-001",
        "days": 14
      }
      """
    Когда отправляю POST на "/doctors/schedule"
    То код ответа 200
    И сохраняю id свободного слота из ответа как "newSlotId"
    Дано тело запроса:
      """
          {
          "patientId": "22222222-2222-2222-2222-222222222201",
          "reminderEnabled": true,
          "newSlotId": "${newSlotId}",
          "appointmentId": "${appointmentsId}"
          }
      """
    Когда отправляю POST на "/appointments/update"
    То код ответа 200
    Дано тело запроса:
      """
      {
          "patientId": "22222222-2222-2222-2222-222222222201",
          "appointmentId": "${appointmentsId}",
          "reason": "string"
      }
      """
    Когда отправляю POST на "/appointments/cancel"
    То код ответа 200

  Сценарий: Изменение настроек записи 404
      Дано тело запроса:
      """
          {
              "patientId": "0",
              "reminderEnabled": true,
              "newSlotId": "0",
              "appointmentId": "0"
          }
      """
    Когда отправляю POST на "appointments/update"
    То код ответа 404

  Сценарий: Изменение настроек записи 405
      Дано тело запроса:
      """
          {
              "patientId": "0",
              "reminderEnabled": true,
              "newSlotId": "0",
              "appointmentId": "0"
          }
      """
    Когда отправляю GET на "appointments/update"
    То код ответа 405

  Сценарий: Изменение настроек записи 409
    Дано тело запроса:
      """
      {
        "patientId": "22222222-2222-2222-2222-222222222201",
        "doctorId": "doc-001",
        "days": 14
      }
      """
    Когда отправляю POST на "/doctors/schedule"
    То код ответа 200
    И сохраняю id свободного слота из ответа как "slotId"
    Дано тело запроса:
    """
      {
        "doctorId": "doc-001",
        "slotId": "${slotId}",
        "patientId": "22222222-2222-2222-2222-222222222201",
        "goalId": "goal-sick"
      }
      """
    Когда отправляю POST на "/appointments/create"
    То код ответа 201
    И сохраняю поле "id" из ответа как "appointmentsId"
    Дано тело запроса:
      """
          {
          "patientId": "22222222-2222-2222-2222-222222222201",
          "reminderEnabled": true,
          "newSlotId": "${slotId}",
          "appointmentId": "${appointmentsId}"
          }
      """
    Когда отправляю POST на "/appointments/update"
    То код ответа 409
    Дано тело запроса:
      """
      {
          "patientId": "22222222-2222-2222-2222-222222222201",
          "appointmentId": "${appointmentsId}",
          "reason": "string"
      }
      """
    Когда отправляю POST на "/appointments/cancel"
    То код ответа 200