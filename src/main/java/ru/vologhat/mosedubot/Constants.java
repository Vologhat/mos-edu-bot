package ru.vologhat.mosedubot;

public class Constants {
    //User agent
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36";

    //Bot data
    public static final String API_KEY = "1602257274:AAHaHJgTRbZEcWtYViHMpVBVOodVXLjhcP8";
    public static final String NAME = "mosedubot";

    //Welcome message after start bot
    public static final String HELLO_MSG = "Привет, я MosEduBot!\nМоя функция - получение информации о мероприятиях с сайта http://events.mosedu.ru/" +
            "\nВот мой список команд:" +
            "\n/months - вывод текущего и следующего месяцев" +
            "\n/days - получение списка дней текущего месяца с мероприятиями, которые ещё не прошли";

    //Message pattern for events
    public static final String MESSAGE_PATTERN = "\"%s\"\n\nИнициатор: %s\n\nВремя: %s\n\n%s\n\n%s\n\nКто может присутствовать: %s\n\nОсталось мест: %s\n\nСсылка: %s";
}