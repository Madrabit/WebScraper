package ru.madrabit.scraper;

import ru.madrabit.scraper.consts.SiteLetters;

import java.util.List;
import java.util.Map;

public interface UrlCrawler {
    Map<String, String> scrapeSubTests();

    Map<Enum<SiteLetters>, String> scrapeLetters();

    Map<String, List<String>> getTicketsUrl(Map<String, String> subTests);

    Map<String, List<String>> getTicketsUrlForA1();
}
