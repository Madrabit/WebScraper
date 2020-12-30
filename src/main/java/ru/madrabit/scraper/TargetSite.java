package ru.madrabit.scraper;

import ru.madrabit.scraper.consts.SiteLetters;

public interface TargetSite {
    void scrapeOneLetter(SiteLetters letter);

    void scrapeAllLetters();
}
