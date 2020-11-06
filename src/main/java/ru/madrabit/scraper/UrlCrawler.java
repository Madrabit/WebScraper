package ru.madrabit.scraper;

import ru.madrabit.scraper.consts.SiteLetters;

import java.util.Map;

public interface UrlCrawler {
    Map<String, String> scrapeSubTests();

    Map<Enum<SiteLetters>, String> scrapeLetters();
}
