package ru.madrabit.scraper.test24;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.madrabit.scraper.Util;
import ru.madrabit.scraper.config.SeleniumHandler;
import ru.madrabit.scraper.domen.Answer;
import ru.madrabit.scraper.domen.Question;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class QuestionsParser {
    private int questionSerial;
    private final SeleniumHandler seleniumHandler = SeleniumHandler.getSeleniumHandler();
    private final Util util = Util.getUtil();
    private final List<String> ticketsList;
    private final String id;

    public QuestionsParser(List<String> ticketsList, String id) {
        this.ticketsList = ticketsList;
        this.id = id;
    }

    public List<Question> iterateTickets() {
        List<Question> questionList = new LinkedList<>();
        this.setQuestionSerial(0);
        for (String ticket : ticketsList) {
            util.moveToUrl(ticket);
            seleniumHandler.jumpToResult();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            questionList.addAll(getAllQuestions(id));
        }
        return questionList;
    }

    public void setQuestionSerial(int questionSerial) {
        this.questionSerial = questionSerial;
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

    private List<Question> getAllQuestions(String id) {
        WebElement main = seleniumHandler.getElement(".entry-content");
        return main.findElements((By.cssSelector(".watupro-choices-columns")))
                .stream().map(questionDiv -> parseQuestion(questionDiv, id)).collect(toList());
    }

}
