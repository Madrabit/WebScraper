package ru.madrabit.scraper;

import lombok.extern.slf4j.Slf4j;
import ru.madrabit.scraper.config.SeleniumHandler;

@Slf4j
public class ScraperApp {

    public static final String BASIC = "https://tests24.su/test-24/promyshlennaya-bezopasnost/";

    private SeleniumHandler seleniumHandler = new SeleniumHandler();


    private void startCrawling() {
        if(seleniumHandler.start()) {
            seleniumHandler.openPage(BASIC);
            log.info("Opened page: {}", BASIC);

        }
    }

    public static void main(String[] args) {
        ScraperApp app = new ScraperApp();
        app.startCrawling();

    }
}
