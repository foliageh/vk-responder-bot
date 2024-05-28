# Инструкция по конфигурации и запуску
### Создание бота в ВК
1. Создать сообщество ВК и перейти в раздел "Управление".
2. В разделе "Сообщения" включить Сообщения сообщества, и в подразделе "Настройки для бота" включить Возможности ботов.
3. В разделе "Настройки" -> "Работа с API" -> "Ключи доступа" создать токен с доступом к сообщениям 
и записать его в переменную `vk.api.access-token` в файле [vk.properties](src/main/resources/vk.properties).
4. В разделе "Настройки" -> "Работа с API" -> "Callback API":
   - В разделе "Типы событий" включить Входящие сообщения.
   - В разделе "Настройки сервера" установить версию API 5.236 (как в [vk.properties](src/main/resources/vk.properties)).

### Запуск приложения
1. Если ngrok не установлен, то скачать с официального сайта и добавить токен аутентификации (https://dashboard.ngrok.com/get-started/your-authtoken).
2. Получить внешний адрес для Spring Boot приложения командой `./ngrok http 8080` (порт должен совпадать с таковым из [application.properties](src/main/resources/application.properties)).
3. Перейти в раздел "Callback API" в управлении сообществом:
   - Скопировать полученный внешний адрес в строку "Адрес", дописав к нему `/callback`.
   - Скопировать "строку, которую должен вернуть сервер" в переменную `vk.api.server-confirm-code` в файле [vk.properties](src/main/resources/vk.properties).
4. Выполнить команду `./gradlew bootRun` и дождаться запуска приложения.
5. В том же разделе "Callback API" нажать на кнопку "Подтвердить".

Для последующих запусков достаточно будет просто выполнить `./gradlew bootRun`.
Если меняется адрес сервера, шаги из "Запуск приложения" необходимо повторить заново.

### Описание параметров конфигурации
1. `vk.api.version` - версия Callback API, по умолчанию `5.236`.
2. `vk.api.access-token` - ключ доступа к боту.
3. `vk.api.server-confirm-code` - код подтверждения адреса сервера, на который будут приходить уведомления о событиях.