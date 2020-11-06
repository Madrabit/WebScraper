package ru.madrabit.scraper;

import ru.madrabit.scraper.consts.SiteLetters;
import ru.madrabit.scraper.test24.SiteTest24su;

public class Main {
    public static void main(String[] args) {
        TargetSite test24su = new SiteTest24su();
//        TargetSite siteB = new SiteBClass();

        test24su.execute(SiteLetters.B_1);
//        test24su.execute(SiteLetters.ALL);
//        siteB.execute(SiteLetters.D);
    }
}
