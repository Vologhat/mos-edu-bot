package ru.vologhat.mosedubot.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.vologhat.mosedubot.data.item.EventDayItem;
import ru.vologhat.mosedubot.data.item.EventItem;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.vologhat.mosedubot.Constants.*;

public class Repository {

    /**
     * @param date - full date of day with form "yy-mm-dd"
     * @return - List with events of needed day */
    public static List<EventItem> getEventsList(String date) {
        List<EventItem> list = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("http://events.mosedu.ru/wp-content/themes/eventstheme/srv-srv.php")
                    .userAgent(USER_AGENT)
                    .data("action_type", "show_day")
                    .data("cd", date)
                    .data("pos", "l")
                    .post();

            Elements containers = doc.select("table").first().select("tbody");
            for (int i = 1; i < containers.size(); i++) {
                Element body = containers.get(i);

                String author = body.select("td").get(2).text();
                String text = body.text();

                Pattern pattern = Pattern.compile("\\d+\\.\\d+ (\\d+:\\d+ — \\d+:\\d+) (\\d+ / \\d+) (.+) (Учащиеся Учителя|Учитеся|Учащиеся) (Направление:.+) (Формат:.+)");
                Matcher matcher = pattern.matcher(text);

                if (matcher.find()) {
                    String time = matcher.group(1).replace("\u2014", "-");
                    String people = matcher.group(2);
                    String title = matcher.group(3).replace(author + " ", "");
                    String peopleType = matcher.group(4).toLowerCase().replace(" ", ", ");
                    String direction = matcher.group(5);
                    String format = matcher.group(6);
                    //window.location.href='http://events.mosedu.ru/wp-content/themes/eventstheme/e-event.php?e=763'
                    String joinUrl = doc.select("div[class=event-block]").get(i-1)
                            .attr("onclick")
                            .replaceFirst(".+href=.(.+).", "$1");

                    // Check free places
                    String[] peopleArr = people.split("\\s/\\s");
                    int current = Integer.parseInt(peopleArr[0]), max = Integer.parseInt(peopleArr[1]);

                    if (current < max) {
                        EventItem item = new EventItem(author, title, format, direction, time, peopleType, joinUrl, max - current);
                        list.add(item);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @param y - year value
     * @param m - month value
     * @return - List with event days of needed month*/
    public static List<EventDayItem> getMonthEventDaysList(String y, String m) {
        List<EventDayItem> list = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("http://events.mosedu.ru/wp-content/themes/eventstheme/srv-srv.php")
                    .userAgent(USER_AGENT)
                    .data("action_type", "show_month")
                    .data("m", m)
                    .data("y", y)
                    .post();
            Elements curMonth = doc.select("div[id=center-calend-block-l]").select("div[class=b-calendar__number]");
            for (int i = 0; i < curMonth.size(); i++) {
                Element element = curMonth.get(i);
                //If main element has events
                if (element.childrenSize() > 1) {
                    org.jsoup.nodes.Element child = element.select("a[class=active-calendar-link]").get(0);

                    int day = Integer.parseInt(child.text());
                    //showDayEvents('2021-02-12','l');
                    String date = child.attr("onclick").replaceFirst(".+\\('(.+)',.+\\).+", "$1");

                    EventDayItem item = new EventDayItem(day, date);
                    list.add(item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @return - List with event days of current month*/
    public static List<EventDayItem> getCurrentMonthEventDaysList() {
        List<EventDayItem> list = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("http://events.mosedu.ru/")
                    .userAgent(USER_AGENT)
                    .get();
            Elements curMonth = doc.select("div[id=center-calend-block-l]").select("div[class=b-calendar__number]");
            for (int i = 0; i < curMonth.size(); i++) {
                Element element = curMonth.get(i);
                //If main element has events
                if (element.childrenSize() > 1) {
                    org.jsoup.nodes.Element child = element.select("a[class=active-calendar-link]").get(0);

                    int day = Integer.parseInt(child.text());
                    //showDayEvents('2021-02-12','l');
                    String date = child.attr("onclick").replaceFirst(".+\\('(.+)',.+\\).+", "$1");

                    int curDay = getCurrentDay();

                    if (day >= curDay) {
                        System.out.println(curDay);
                        EventDayItem item = new EventDayItem(day, date);
                        list.add(item);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @return - Current day value*/
    public static int getCurrentDay() {
        LocalDate date = LocalDate.now();
        return date.getDayOfMonth();
    }
}
