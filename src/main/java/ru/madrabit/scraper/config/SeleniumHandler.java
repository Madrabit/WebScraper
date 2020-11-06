package ru.madrabit.scraper.config;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
public class SeleniumHandler {
    private static SeleniumHandler seleniumHandler;

    private SeleniumHandler() {
    }

    public static SeleniumHandler getSeleniumHandler() {
        if (seleniumHandler == null) {
            seleniumHandler = new SeleniumHandler();
        }
        return seleniumHandler;
    }

    private WebDriver driver;
    private Wait<WebDriver> wait;
    public static final int WAIT_TIME_MAX = 2;

    public boolean start() {
        try {
            driver = getChromeDriver();
            wait = new WebDriverWait(driver, WAIT_TIME_MAX);
        } catch (Exception e) {
            log.error("Driver was not initialized: {}", e.getMessage());
            return false;
        }
        return true;
    }

    public boolean openPage(String url) {
        try {
            driver.get(url);
        } catch (Exception e) {
            log.warn("Error opening page: {}", url);
            return false;
        }
        return true;
    }

    public void stop() {
        if (driver != null) {
            driver.quit();
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    private WebDriver getChromeDriver() {
        ChromeConfig.setDriverPath();
        return new ChromeDriver(ChromeConfig.getChromeOptions());
    }

    public void click(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", element);
    }

    public WebElement getElement(String path) {
        WebElement element = null;
        try {
           element = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(path)));
        }  catch (Exception e) {
//            log.info("NO such element: {}", path);
        }
       return element;
    }

    public void jumpToResult() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        js.executeScript("WatuPRO.submitResult = function (e) { \n" +
                "    var data = {\n" +
                "        \"action\": 'watupro_submit',\n" +
                "        \"quiz_id\": this.exam_id,\n" +
                "        'question_id[]': this.qArr,\n" +
                "        \"watupro_questions\": jQuery('#quiz-' + this.exam_id + ' input[name=watupro_questions]').val(),\n" +
                "        \"post_id\": this.post_id\n" +
                "    };  \n" +
                "    data = this.completeData(data);  \n" +
                "  \n" +
                "    data['start_time'] = jQuery('#startTime').val();  \n" +
                "  \n" +
                "    data['in_ajax'] = 1;\n" +
                "    if (!e || !e.no_ajax || e.no_ajax.value != 1) {\n" +
                "        try {\n" +
                "            jQuery.ajax({\n" +
                "                \"type\": 'POST',\n" +
                "                \"url\": this.siteURL,\n" +
                "                \"data\": data,\n" +
                "                \"success\": WatuPRO.success,\n" +
                "                \"error\": WatuPRO.errHandle,\n" +
                "                \"cache\": false,\n" +
                "                dataType: \"text\"\n" +
                "            });\n" +
                "        } catch (err) {\n" +
                "            alert(err)\n" +
                "        }\n" +
                "    }\n" +
                "}\n" );
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        js.executeScript("document.querySelector('.watupro-submit-button:not([style*=\"display: none\"]').disabled = false;" +
                "document.querySelector(\'.watupro-submit-button:not([style*=\"display: none\"]\').click();");



    }
}


