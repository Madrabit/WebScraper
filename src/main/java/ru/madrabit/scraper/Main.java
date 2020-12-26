package ru.madrabit.scraper;

import ru.madrabit.scraper.consts.SiteLetters;

public class Main {
    public static void main(String[] args) {
        TargetSite test24su = new ru.madrabit.scraper.test24.Main();
//        TargetSite siteB = new SiteBClass();

        test24su.execute(SiteLetters.A_1);
//        test24su.execute(SiteLetters.ALL);
//        siteB.execute(SiteLetters.D);
    }
}
