package ru.madrabit.scraper;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.madrabit.scraper.config.SeleniumHandler;
import ru.madrabit.scraper.domen.Question;
import ru.madrabit.scraper.poi.CreateExcel;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class ScraperApp implements  Crawler{

    public static final String START_URL = "https://tests24.su/test-24/promyshlennaya-bezopasnost/";
    private SeleniumHandler seleniumHandler = SeleniumHandler.getSeleniumHandler();
    private Util util = Util.getUtil();

    List<String> letters = new ArrayList<>();

    public List<String> getLetters() {
        return letters;
    }

    List<String> subTests = new ArrayList<>();

    public List<String> getSubTests() {
        return subTests;
    }

    @Override
    public void start() {
        if (seleniumHandler.start()) {
            seleniumHandler.openPage(START_URL);
            log.info("Opened page: {}", START_URL);
            letters = scrapeLetters();
            log.info("Letters collected: {}", letters.size());
//            for (String letter : letters) {
            for (int i = 10; i < 13; i++) {
                util.moveToUrl(letters.get(i));
                List<String> subTests = scrapeSubTests();

//                for (String ticket : subTests) {
                for (int j = 0; j < subTests.size(); j++) {
                    util.moveToUrl(subTests.get(j));
                    String id = util.getTestNameFromUrl(subTests.get(j));
                    List<String> ticketsList = util.scrapeTickets();
                    log.info("Tickets collected: {}", ticketsList.size());
                    QuestionsParser questionsParser = new QuestionsParser(ticketsList, id);
                    List<Question> questionList = questionsParser.iterateTickets();
                    log.info("Questions in ticket: {}",questionList.size());
                    if (!questionList.isEmpty()) {
                        CreateExcel excelDemo = new CreateExcel(id);
                        excelDemo.createExcel(questionList);
                    }

                }
            }
            log.info("Sub tests collected: {}", subTests.size());
            seleniumHandler.stop();
        }
    }



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
        return  seleniumHandler.getElement(mainBlock)
                .findElements(By.cssSelector(element))
                .stream().map(e -> e.getAttribute("href"))
                .collect(toList());
    }


    private List<String> scrapeTickets() {
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

}
