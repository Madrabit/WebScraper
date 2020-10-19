package ru.madrabit.scraper;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import ru.madrabit.scraper.config.SeleniumHandler;

@Slf4j
public class ScraperApp {

    public static final String BASIC = "https://tests24.su/test-24/promyshlennaya-bezopasnost/";

    private SeleniumHandler seleniumHandler = new SeleniumHandler();

    public static void main(String[] args) {
        ScraperApp app = new ScraperApp();
        app.startCrawling();

    }

    private void startCrawling() {
        if(seleniumHandler.start()) {
            seleniumHandler.openPage(BASIC);
            moveToLetter();
            moveToTest();
            chooseTicket();
            seleniumHandler.jumpToResult();
            log.info("Opened page: {}", BASIC);

        }
    }

    private void moveToLetter() {
        try {
            seleniumHandler.click(seleniumHandler.getElement(ElementsConst.TEST_A));
        } catch (Exception e) {
            log.error("Can't click element: {}", ElementsConst.TEST_A);
        }
    }

    private void moveToTest() {
        try {
            seleniumHandler.click(seleniumHandler.getElement(ElementsConst.A_1));
        } catch (Exception e) {
            log.error("Can't click element: {}", ElementsConst.TEST_A);
        }
    }

    private void chooseTicket() {
        try {
            seleniumHandler.click(seleniumHandler.getElement(ElementsConst.TICKET_1));
        } catch (Exception e) {
            log.error("Can't click element: {}", ElementsConst.TEST_A);
        }
    }


}
