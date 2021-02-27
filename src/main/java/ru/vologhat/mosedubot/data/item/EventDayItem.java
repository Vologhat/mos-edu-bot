package ru.vologhat.mosedubot.data.item;

import org.json.JSONObject;

public class EventDayItem {
    private int day;
    private String date;

    public EventDayItem(int day, String date) {
        this.day = day;
        this.date = date;
    }

    public int getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject()
                .put("day", getDay())
                .put("date", getDate());
        return jsonObject.toString();
    }
}
