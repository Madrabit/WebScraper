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
public class ScraperApp {

    public static final String START_URL = "https://tests24.su/test-24/promyshlennaya-bezopasnost/";
    private String previousURL;
    private static int questionSerial = 0;

    private SeleniumHandler seleniumHandler = new SeleniumHandler();

    public static void main(String[] args) {
        ScraperApp app = new ScraperApp();
        app.startCrawling();

    }

    public void letterACrawrel() {
        if(seleniumHandler.start()) {
            seleniumHandler.openPage(START_URL);
        }
    }
        public void startCrawling() {
        if (seleniumHandler.start()) {
            seleniumHandler.openPage(START_URL);
            log.info("Opened page: {}", START_URL);
            List<String> letters = scrapeLetters();
            log.info("Letters collected: {}", letters.size());
            for (String letter : letters) {
                moveToUrl(letter);
                List<String> subTests = scrapeSubTests();
                log.info("Sub tests collected: {}", subTests.size());
                for (String ticket : subTests) {
                    moveToUrl(ticket);
                    List<String> scrapeTickets = scrapeTickets();
                    log.info("Tickets collected: {}", scrapeTickets.size());
                    for (String scrapeTicket : scrapeTickets) {
                        moveToUrl(scrapeTicket);
                        seleniumHandler.jumpToResult();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        List<Question> questionList = getAllQuestions();
                        log.info("Questions collectes: {}", questionList.size());

//                        seleniumHandler.openPage(previousURL);
                    }
                }
                seleniumHandler.openPage(previousURL);
            }


//            CreateExcelDemo excelDemo = new CreateExcelDemo();
//            excelDemo.createExcel(questionList);
        }
    }

//    private List<String> scrapeLetters() {
//        WebElement mainBlock = seleniumHandler.getElement("#panel-1201-0-0-1 ");
//        List<String> letters = mainBlock.findElements(By.cssSelector(" a"))
//                .stream().map(e -> e.getAttribute("href")).collect(toList());
//        letters.remove(0); // First one is uniq, should not parse in cycle.
//        letters.remove(letters.size() - 1); //remove excess last one test.
//        return letters;
//    }

//    private List<String> scrapeSubTests() {
//        WebElement mainBlock = seleniumHandler.getElement(".siteorigin-widget-tinymce.textwidget:nth-child(2)");
//        List<String> list = mainBlock.findElements(By.cssSelector("div > blockquote > p > strong > a"))
//                .stream().map(e -> e.getAttribute("href")).collect(toList());
//        return list;
//    }

    private List<String> scrapeSubTests() {
        return commonScraper(".siteorigin-widget-tinymce.textwidget:nth-child(2)",
                "div > blockquote > p > strong > a");
    }

    private List<String> scrapeLetters() {
        List<String> letters = commonScraper("#panel-1201-0-0-1 ", " a");
        letters.remove(0); // First one is uniq, should not parse in cycle.
        letters.remove(letters.size() - 1); //remove excess last one test.
        return letters;
    }

    private List<String> commonScraper(String mainBlock, String element) {
        WebElement mainB = seleniumHandler.getElement(mainBlock);
        List<String> list = mainB.findElements(By.cssSelector(element))
                .stream().map(e -> e.getAttribute("href")).collect(toList());
        return list;
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



    private void moveToLetter(String letter) {
        try {
            previousURL = seleniumHandler.getCurrentUrl();
            seleniumHandler.openPage(letter);
        } catch (Exception e) {
            log.error("Can't click element: {}", letter);
        }
    }

    private void moveToTicketsList(String ticket) {
        try {
//            previousURL = seleniumHandler.getCurrentUrl();
            seleniumHandler.openPage(ticket);
//            seleniumHandler.click(seleniumHandler.getElement(ElementsConst.A_1));
        } catch (Exception e) {
            log.error("Can't click element: {}", ticket);
        }
    }

    private void chooseTicket(String ticket) {
        try {
            seleniumHandler.openPage(ticket);
        } catch (Exception e) {
            log.error("Can't click element: {}", ticket);
        }
    }

    private void moveToUrl(String url) {
        try {
            previousURL = seleniumHandler.getCurrentUrl();
            seleniumHandler.openPage(url);
        } catch (Exception e) {
            log.error("Can't click element: {}", url);
        }
    }



    private Question parseQuestion(WebElement questionDiv) {
        Question question = new Question(
                ++questionSerial,
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

    private List<Question> getAllQuestions() {
        WebElement main = seleniumHandler.getElement(".entry-content");
        return main.findElements((By.cssSelector(".watupro-choices-columns")))
                .stream().map(this::parseQuestion).collect(toList());
    }


}
