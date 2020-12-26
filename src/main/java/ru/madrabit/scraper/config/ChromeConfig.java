package ru.madrabit.scraper.config;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Config for Chromediver.
 * Driver saved in resources folder.
 * IMPORTANT chromedriver.exe should match Selenium version driver.
 */
@Slf4j
public class ChromeConfig {
    public static final String CHROME_DRIVER_PATH = "src/main/resources/chromedriver.exe";

    public static void setDriverPath() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        log.info("Path to chrome driver: {}", System.getProperty("webdriver.chrome.driver"));
    }

    static ChromeOptions getChromeOptions() {
        return new ChromeOptions()
                .addArguments("--lang=ru")
                .addArguments("start-maximized");
    }
}
