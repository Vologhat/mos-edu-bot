package ru.vologhat.mosedubot.data.item;

import org.json.JSONObject;

public class EventItem {
    private String author;
    private String title;
    private String format;
    private String direction;
    private String time;
    private String peopleType;
    private String joinUrl;
    private int freePlaces;

    public EventItem(String author, String title, String format, String direction, String time, String peopleType, String joinUrl, int freePlaces) {
        this.author = author;
        this.title = title;
        this.format = format;
        this.direction = direction;
        this.time = time;
        this.peopleType = peopleType;
        this.joinUrl = joinUrl;
        this.freePlaces = freePlaces;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getFormat() {
        return format;
    }

    public String getDirection() {
        return direction;
    }

    public String getTime() {
        return time;
    }

    public String getPeopleType() {
        return peopleType;
    }

    public String getJoinUrl() {
        return joinUrl;
    }

    public int getFreePlaces() {
        return freePlaces;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject()
                .put("author", getAuthor())
                .put("title", getTitle())
                .put("format", getFormat())
                .put("direction", getDirection())
                .put("time", getTime())
                .put("peopleType", getPeopleType())
                .put("joinUrl", getJoinUrl())
                .put("freePlaces", getFreePlaces());
        return jsonObject.toString();
    }
}
