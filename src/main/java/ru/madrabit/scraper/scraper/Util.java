package ru.madrabit.scraper.scraper;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.madrabit.scraper.scraper.config.SeleniumHandler;
import ru.madrabit.scraper.scraper.domen.Answer;
import ru.madrabit.scraper.scraper.domen.Question;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class Util {
    private String previousURL;
    private int questionSerial;
    private final SeleniumHandler seleniumHandler;

    public Util(SeleniumHandler seleniumHandler) {
        this.seleniumHandler = seleniumHandler;
    }

    public List<String> scrapeTickets() {
        String LINKS = "div p a ";
        String ROW_CHILD_2 = ".panel-grid.panel-no-style:nth-child(2)";
        String ROW_CHILD_3 = ".panel-grid.panel-no-style:nth-child(3)";
        WebElement firstRow = seleniumHandler.getElement(ROW_CHILD_2 + " > div > " + LINKS);
        WebElement secondRow = seleniumHandler.getElement(ROW_CHILD_3 + " > div > " + LINKS);
        WebElement element = null;
        List<String> tickets = new ArrayList<>();
        if (firstRow.getText().contains("БИЛЕТ")) {
            element = seleniumHandler.getElement(ROW_CHILD_2);
        } else if (secondRow != null && secondRow.getText().contains("БИЛЕТ")) {
            element = seleniumHandler.getElement(ROW_CHILD_3);
        } else {
            log.error("Can't find row with tickets");
        }
        if (element != null) {
            tickets = element.findElements(By.cssSelector(LINKS)).stream().map(e -> e.getAttribute("href")).collect(toList());
        }
        return tickets;
    }

    public void moveToUrl(String url) {
        try {
//            previousURL = seleniumHandler.getCurrentUrl();
            seleniumHandler.openPage(url);
        } catch (Exception e) {
            log.error("Can't click element: {}", url);
        }
    }

    private Question parseQuestion(WebElement questionDiv, String id) {
        Question question = new Question(
                id + "-" + ++questionSerial,
                questionDiv.findElement(By.cssSelector(".show-question-content")).getText()
        );
        List<WebElement> answers = questionDiv.findElements(By.cssSelector(".show-question-choices > ul > li"));
        int serial = 0;
        for (WebElement webElem : answers) {
            Answer answer = new Answer(++serial, webElem.getText(), question.getId());
            if (webElem.getAttribute("class").contains("correct-answer")) {
                answer.setRight(true);
                question.getAnswerNumber().add(serial);
                answer.setText(webElem.findElement(By.tagName("span")).getText());
            } else {
                answer.setText(webElem.getText());
            }
            question.getAnswerSet().add(answer);
        }
        return question;
    }

    public List<Question> getAllQuestions(String id) {
        WebElement main = seleniumHandler.getElement(".entry-content");
        return main.findElements((By.cssSelector(".watupro-choices-columns")))
                .stream().map((WebElement questionDiv) -> parseQuestion(questionDiv, id)).collect(toList());
    }

    public String getTestNameFromUrl(String url) {
        String s = url.substring(19);
        String[] result = s.split("-");
        return ("b".equals(result[0]) ? "Б" : "Д") + "." + result[1] + "." + result[2];
    }

    public void setQuestionSerial(int questionSerial) {
        this.questionSerial = questionSerial;
    }
}
