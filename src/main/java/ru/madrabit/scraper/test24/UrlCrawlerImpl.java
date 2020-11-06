package ru.madrabit.scraper.test24;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.madrabit.scraper.UrlCrawler;
import ru.madrabit.scraper.config.SeleniumHandler;
import ru.madrabit.scraper.consts.SiteLetters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UrlCrawlerImpl implements UrlCrawler {

    private final SeleniumHandler seleniumHandler;

    public UrlCrawlerImpl(SeleniumHandler seleniumHandler) {
        this.seleniumHandler = seleniumHandler;
    }

    @Override
    public Map<Enum<SiteLetters>, String> scrapeLetters() {
        Map<Enum<SiteLetters>, String> map = new HashMap<>();
        List<WebElement> elements = seleniumHandler.getElement("#panel-1201-0-0-1 ").findElements(By.cssSelector(" a"));
        elements.remove(0); // First one is uniq, should not parse in cycle.
        elements.remove(elements.size() - 1); //remove excess last one test.
        for (WebElement e : elements) {
            String href = e.getAttribute("href");
            try {
                map.put(getTestNameFromUrl(href), href);
            } catch (NoSuchLetterException nsl) {
                log.error(nsl.getMessage(), nsl);
            }
        }
        log.info("Letters collected: {}", map.size());
        return map;
    }

    @Override
    public  Map<String, String> scrapeSubTests() {
        Map<String, String> map = new HashMap<>();
        for (WebElement e : seleniumHandler.getElement(".siteorigin-widget-tinymce.textwidget:nth-child(2)")
                .findElements(By.cssSelector("div > blockquote > p > strong > a"))) {
            String href = e.getAttribute("href");
            if (href.contains("proverka")) break; // exclude last test, which doesn't need
            try {
                map.put(getTestNameFromTicketUrl(href), href);
            } catch (NoSuchLetterException nsl) {
                log.error(nsl.getMessage(), nsl);
            }
        }
        log.info("Letters collected: {}", map.size());
        return map;
    }

    private static Enum<SiteLetters> getTestNameFromUrl(String url) throws NoSuchLetterException {
        String s = url.substring(19);
        String[] splitted = s.split("-");
        String result = "";
        // if letter and number glued like "b1"
        if (splitted[0].length() > 1) {
            return SiteLetters.valueOf(splitted[0].substring(0, 1).toUpperCase()
                    + "_"
                    + splitted[0].substring(1, 2));
        }
        switch (splitted[0]) {
            case "b":
                result = "B_" + splitted[1];
                break;
            case "d":
                result = "D";
                break;
            default:
                throw new NoSuchLetterException("Wrong Letter selected");
        }
        return SiteLetters.valueOf(result);
    }

    private String getTestNameFromTicketUrl(String url) throws NoSuchLetterException {
        String s = url.substring(19);
        String[] splitted = s.split("-");
        String result = "";
        switch (splitted[0]) {
            case "b":
                result = "Б." + splitted[1] + "." + splitted[2];
                break;
            case "d":
                result = "Д." + splitted[1];
                break;
            default:
                throw new NoSuchLetterException("Wrong Letter selected");
        }
        return result;
    }
}
