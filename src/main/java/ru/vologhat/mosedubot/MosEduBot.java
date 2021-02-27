package ru.vologhat.mosedubot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import ru.vologhat.mosedubot.data.Repository;
import ru.vologhat.mosedubot.data.item.EventDayItem;
import ru.vologhat.mosedubot.data.item.EventItem;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MosEduBot extends BotHandler{
    private final TelegramBot bot = new TelegramBot(Constants.API_KEY);

    @Override
    void onWebhookUpdate(Update update) {
        if (isMessage(update)) {
            checkCommand(update.message());
        } else if (isCallbackQuery(update)) {
            sendCallback(update.callbackQuery());
        }
    }

    @Override
    String getToken() {
        return Constants.API_KEY;
    }

    @Override
    TelegramBot getBot() {
        return bot;
    }

    /**
     * @param message - User message info*/
    void checkCommand(Message message) {
        switch (message.text()) {
            case "/start":
                sendMessage(message, Constants.HELLO_MSG);
                break;

            case "/months":
                sendMonthsList(message);
                break;

           case "/days":
               List<EventDayItem> days = Repository.getCurrentMonthEventDaysList();
               sendDaysList(message, days);
               break;
        }
    }

    /**
     * @param callbackQuery - Callback data*/
    void sendCallback(CallbackQuery callbackQuery) {
        String callback = callbackQuery.data();

        //Check date callback with form "yy:mm:dd"
        if (callback.matches("\\d+-\\d\\d-\\d\\d")) {
            List<EventItem> events = Repository.getEventsList(callback);
            for (EventItem event: events) {
                String text = String.format(Constants.MESSAGE_PATTERN,
                        event.getTitle(),
                        event.getAuthor(),
                        event.getTime(),
                        event.getFormat(),
                        event.getDirection(),
                        event.getPeopleType(),
                        event.getFreePlaces(),
                        event.getJoinUrl());

                sendMessage(callbackQuery.message(), text);
            }
        } else {
            String[] arr = callback.split(" ");
            List<EventDayItem> days = Repository.getMonthEventDaysList(arr[1], arr[0]);
            sendDaysList(callbackQuery.message(), days);
        }
    }

    /**
     * @param message - User message info
     * @param days - List of event days*/
    void sendDaysList(Message message, List<EventDayItem> days) {
        if (days.size() != 0) {
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

            //generate event calendar markup
            for (int i = 0; i < days.size();) {
                List<InlineKeyboardButton> lineList = new ArrayList<>();

                if (i % 5 == 0) {
                    int index = i + 5;

                    for (int j = i; j < index && i < days.size(); j++, i++) {
                        EventDayItem item = days.get(j);
                        InlineKeyboardButton button = new InlineKeyboardButton(String.valueOf(item.getDay()))
                                .callbackData(item.getDate());

                        lineList.add(button);
                    }
                }

                InlineKeyboardButton[] buttons = lineList.toArray(new InlineKeyboardButton[0]);
                markup.addRow(buttons);
            }

            sendReplyMarkup(message, "Выберите число", markup);
        } else {
            sendMessage(message, "Все мероприятия закончились");
        }
    }

    /**
     * @param message - User message info*/
    void sendMonthsList(Message message) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        LocalDate date = LocalDate.now();
        Month cur = date.getMonth();
        boolean lastMonth = cur.getValue() == 12;
        Month next = Month.of(lastMonth ? 1: date.getMonth().getValue()+1);

        InlineKeyboardButton[] buttons = new InlineKeyboardButton[2];

        buttons[0] = new InlineKeyboardButton(cur.getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru")))
                .callbackData(cur.getValue() + " " + date.getYear());

        buttons[1] = new InlineKeyboardButton(next.getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru")))
                .callbackData(next.getValue() + " " + (lastMonth ? date.getYear()+1: date.getYear()));

        sendReplyMarkup(message, "Выберите месяц", markup.addRow(buttons));
    }

    /**
     * @param message - User message info
     * @param text - text for answer*/
    void sendMessage(Message message, String text) {
        SendMessage sendMessage = new SendMessage(message.chat().id(), text);
        bot.execute(sendMessage);
    }

    /**
     * @param message - User message info
     * @param text - text for answer
     * @param markup - buttons markup*/
    void sendReplyMarkup(Message message, String text, InlineKeyboardMarkup markup) {
        SendMessage sendMessage = new SendMessage(message.chat().id(), text)
                .replyMarkup(markup);
        bot.execute(sendMessage);
    }
}
