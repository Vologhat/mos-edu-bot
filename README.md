# Репозиторий MosEduBot
Этот репозиторий создан для демострации Telegram-бота MosEduBot <https://t.me/mosedubot> 

# Архитектура проекта
Проект написан на языке Java версии 8 с приминением одной из систем автоматической сборки - Gradle версии 6.7.
В главной директории помимо основных файлов конфигурации Gradle находится файл Procfile, который является надстройкой для запуска бота на удалённом сервере Heroku, а также папка src. Она содержит в себе весь исходный код бота и имеет следующие классы:

1) Main - главный класс для инициализации бота
2) BotHandler - класс-обработчик для получения обновлений
3) MosEduBot - класс-наследник BotHandler'a, отвечающий за работоспособность бота 
4) Constants - Data-класс с константами
5) Repository - класс для парсинга информации с сайта <http://events.mosedu.ru/> 
6) EventDayItem - класс для хранения информации о дне с событиями
7) EventItem - класс для хранения информации о событии



# Библиотеки
Spark Framework - <https://github.com/perwendel/spark>

Pengrad Java Telegram Bot API - <https://github.com/pengrad/java-telegram-bot-api>

Jsoup - <https://jsoup.org/>

JSON - <https://github.com/stleary/JSON-java>
