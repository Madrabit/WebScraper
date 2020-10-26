package ru.madrabit.scraper;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.madrabit.scraper.config.SeleniumHandler;
import ru.madrabit.scraper.domen.Answer;
import ru.madrabit.scraper.domen.Question;

import java.util.List;

@Slf4j
public class ScraperApp {

    public static final String BASIC = "https://tests24.su/test-24/promyshlennaya-bezopasnost/";

    private SeleniumHandler seleniumHandler = new SeleniumHandler();

    public static void main(String[] args) {
        ScraperApp app = new ScraperApp();
        app.startCrawling();

    }

    private void startCrawling() {
        if (seleniumHandler.start()) {
            seleniumHandler.openPage(BASIC);
            log.info("Opened page: {}", BASIC);
            moveToLetter();
            moveToTest();
            chooseTicket();
            seleniumHandler.jumpToResult();
            getAllQuestions();
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

    private void parseQuestion(WebElement qBlock) {
        Question question = new Question(
                1,
                qBlock.findElement(By.cssSelector(".show-question-content")).getText()
        );
        List<WebElement> answers = qBlock.findElements(By.cssSelector(".show-question-choices > ul > li"));
        int i = 0;
        for (WebElement a : answers) {
            Answer answer = new Answer(++i, a.getText(), question.getId());
            if (a.getAttribute("class").contains("correct-answer")) {
                answer.setRight(true);
                question.getAnswerNumber().add(i);
                answer.setText(a.findElement(By.tagName("span")).getText());
            } else {
                answer.setText(a.getText());
            }
            question.getAnswerSet().add(answer);
        }
        System.out.println("Вопрос");
        System.out.println(question.toString());
    }

    private void getAllQuestions() {
        WebElement main = seleniumHandler.getElement(".entry-content");
        List<WebElement> questions = main.findElements((By.cssSelector(".watupro-choices-columns")));
        questions.forEach(this::parseQuestion);
    }



}
