package ru.madrabit.scraper.test24;

import ru.madrabit.scraper.TargetSite;
import ru.madrabit.scraper.consts.SiteLetters;

public class Test24 implements TargetSite {

    @Override
    public void scrapeOneLetter(SiteLetters letter) {
            new CustomScrapperTest24().work(letter);
    }

    @Override
    public void scrapeAllLetters() {
        new AllScrapper().scrapeAll();
    }
}
