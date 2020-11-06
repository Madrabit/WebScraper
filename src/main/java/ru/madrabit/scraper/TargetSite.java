package ru.madrabit.scraper;

import ru.madrabit.scraper.consts.SiteLetters;

public interface TargetSite {
    public void execute(SiteLetters letter);
}
