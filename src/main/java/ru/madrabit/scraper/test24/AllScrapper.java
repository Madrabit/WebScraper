package ru.madrabit.scraper.test24;

import ru.madrabit.scraper.Scrapper;
import ru.madrabit.scraper.consts.SiteLetters;

import java.util.Arrays;

public class AllScrapper {
    Scrapper scrapper = new CustomScrapperTest24();

    public void scrapeAll() {
        Arrays.stream(SiteLetters.values())
                .forEach(letter -> scrapper.work(letter));
    }
}
