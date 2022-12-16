package org.example;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
    private static final Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    public Main() {
    }

    private static Document getPage() throws IOException {
        String url = "https://www.pogoda.spb.ru";
        return Jsoup.parse(new URL(url), 3000);
    }

    private static String getDateFromString(String stringDate) throws IOException {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        } else {
            throw new IOException("Can't extract date from string");
        }
    }

    private static int printFourElements(Elements values, int index) {
        int iterationCount = 4;
        if (index == 0) {
            Element valueLn = (Element) values.get(3);
            boolean isMorning = valueLn.text().contains("Утро");
            if (isMorning) {
                iterationCount = 3;
            }
        }

        for (int i = 0; i < iterationCount; ++i) {
            Element valueLine = (Element) values.get(index + i);
            Iterator var5 = valueLine.select("td").iterator();

            while (var5.hasNext()) {
                Element td = (Element) var5.next();
                System.out.print(td.text() + "  ");
            }

            System.out.println();
        }

        return iterationCount;
    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;
        Iterator var6 = names.iterator();

        while (var6.hasNext()) {
            Element name = (Element) var6.next();
            String dateString = name.select("th[id=dt]").text();
            String weatherConditionString = name.select("th[id=yt]").text();
            String temperatureString = name.select("th[id=tt]").text();
            String pressureString = name.select("th[id=pt]").text();
            String humidityString = name.select("th[id=ht]").text();
            String windString = name.select("th[id=wt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + " | " + weatherConditionString + " | " + temperatureString + " | " + pressureString + " | " + humidityString + " | " + windString);
            int iterationCount = printFourElements(values, index);
            index += iterationCount;
            System.out.println("***********************************************************************************************************");
        }

    }
}