package ru.vologhat.mosedubot;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import spark.Request;
import spark.Response;
import spark.Route;

abstract class BotHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        Update update = BotUtils.parseUpdate(request.body());

        onWebhookUpdate(update);

        return "ok";
    }

    public boolean isMessage(Update update) {
        return update.message() != null && update.message().text() != null;
    }

    public boolean isCallbackQuery(Update update) {
        return update.callbackQuery() != null;
    }

    abstract void onWebhookUpdate(Update update);

    abstract String getToken();

    abstract TelegramBot getBot();
}
