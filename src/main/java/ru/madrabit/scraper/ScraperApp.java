package ru.madrabit.scraper;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.madrabit.scraper.config.SeleniumHandler;
import ru.madrabit.scraper.domen.Answer;
import ru.madrabit.scraper.domen.Question;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class ScraperApp {

    public static final String BASIC = "https://tests24.su/test-24/promyshlennaya-bezopasnost/";
    String previousURL;
    private static int quastionN = 0;

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
            moveToTickets();
            List<String> scrapeTickets = scrapeTickets();
            int i = 0;
            for (String scrapeTicket : scrapeTickets) {
                System.out.println("Билет №" + ++i);
                chooseTicket(scrapeTicket);
                seleniumHandler.jumpToResult();
                getAllQuestions();
                seleniumHandler.openPage(previousURL);
            }
        }
    }


    private List<String> scrapeTickets() {
        previousURL = seleniumHandler.getCurrentUrl();

        String LINKS = "div p a ";
        String ROW_CHILD_2 = ".panel-grid.panel-no-style:nth-child(2)";
        String ROW_CHILD_3 = ".panel-grid.panel-no-style:nth-child(3)";
        WebElement firstRow = seleniumHandler.getElement(ROW_CHILD_2 + " > div > " + LINKS);
        WebElement secondRow = seleniumHandler.getElement(ROW_CHILD_3 + " > div > " + LINKS);
        WebElement element = null;
        List<String> tickets = new ArrayList<>();
        if (firstRow.getText().contains("БИЛЕТ")) {
            element = seleniumHandler.getElement(ROW_CHILD_2);
        } else if (secondRow.getText().contains("БИЛЕТ")) {
            element = seleniumHandler.getElement(ROW_CHILD_3);
        } else {
            log.error("Can't find row with tickets");
        }
        if (element != null) {
            tickets = element.findElements(By.cssSelector(LINKS)).stream().map(e -> e.getAttribute("href").toString()).collect(toList());
        }
        return tickets;
    }

    private void moveToLetter() {
        try {
            seleniumHandler.click(seleniumHandler.getElement(ElementsConst.TEST_A));
        } catch (Exception e) {
            log.error("Can't click element: {}", ElementsConst.TEST_A);
        }
    }

    private void moveToTickets() {
        try {
            seleniumHandler.click(seleniumHandler.getElement(ElementsConst.A_1));
        } catch (Exception e) {
            log.error("Can't click element: {}", ElementsConst.TEST_A);
        }
    }

    private void chooseTicket(String ticket) {
        try {
            seleniumHandler.openPage(ticket);
        } catch (Exception e) {
            log.error("Can't click element: {}", ticket);
        }
    }

    private void parseQuestion(WebElement qBlock) {
        Question question = new Question(
                ++quastionN,
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
        System.out.println(question.toString());
    }

    private void getAllQuestions() {
        WebElement main = seleniumHandler.getElement(".entry-content");
        List<WebElement> questions = main.findElements((By.cssSelector(".watupro-choices-columns")));
        questions.forEach(this::parseQuestion);
    }


}
