package ru.madrabit.scraper.test24;

import ru.madrabit.scraper.Scrapper;
import ru.madrabit.scraper.TargetSite;
import ru.madrabit.scraper.consts.SiteLetters;

public class Main implements TargetSite {

    private Scrapper scrapper;

    @Override
    public void execute(SiteLetters letter) {

        if (letter.equals(SiteLetters.ALL)) {
            scrapper = new AllScrapper();
        } else {
            scrapper = new CustomScrapper(letter);
        }
        scrapper.work();

    }
}
