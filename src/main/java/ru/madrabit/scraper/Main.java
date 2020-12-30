package ru.madrabit.scraper;

import ru.madrabit.scraper.consts.SiteLetters;
import ru.madrabit.scraper.test24.Test24;

public class Main {
    public static void main(String[] args) {
        TargetSite test24su = new Test24();
        test24su.scrapeOneLetter(SiteLetters.valueOf(args[0]));
    }
}
