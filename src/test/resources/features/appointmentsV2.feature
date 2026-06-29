# language: ru
Функция: Записи к врачу - Расширенный тест
  Создание, просмотр, изменение и отмена записей; списки записей и направлений.
  @medical-card
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