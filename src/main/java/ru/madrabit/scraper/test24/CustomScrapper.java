package ru.madrabit.scraper.test24;

import lombok.extern.slf4j.Slf4j;
import ru.madrabit.scraper.*;
import ru.madrabit.scraper.config.SeleniumHandler;
import ru.madrabit.scraper.consts.SiteLetters;
import ru.madrabit.scraper.domen.Question;
import ru.madrabit.scraper.poi.CreateExcel;

import java.util.*;

@Slf4j
public class CustomScrapper implements Scrapper {

    public static final String START_URL = "https://tests24.su/test-24/promyshlennaya-bezopasnost/";
    private final Enum<SiteLetters> letter;

    public CustomScrapper(Enum<SiteLetters> letter) {
        this.letter = letter;
    }

    private SeleniumHandler seleniumHandler = SeleniumHandler.getSeleniumHandler();
    private Util util = Util.getUtil();
    private List<String> ticketsList = new LinkedList<>();
    List<Question> questionList = new LinkedList<>();
    Map<Enum<SiteLetters>, String> letters = new HashMap<>();

    @Override
    public void work() {
        if (seleniumHandler.start()) {
            seleniumHandler.openPage(START_URL);
            log.info("Opened main page: {}", START_URL);
            UrlCrawler urlCrawler = new UrlCrawlerImpl(seleniumHandler);
            if (letter.equals(SiteLetters.A_1)) {
                seleniumHandler.openPage(ElementsConst.A_TICKETS);
                Map<String, List<String>> tickets = urlCrawler.getTicketsUrlForA1();
                log.info("Tickets size: {}", tickets.size());
                QuestionsParser questionsParser = new QuestionsParser(tickets.get("A.1"), "A.1");
                questionList = questionsParser.iterateTickets();
                log.info("Questions in ticket: {}", questionList.size());
                if (!questionList.isEmpty()) {
                    CreateExcel excelDemo = new CreateExcel("A.1");
                    excelDemo.createExcel(questionList);
                }
            } else {
                letters = urlCrawler.scrapeLetters();
                seleniumHandler.openPage(letters.get(letter));
                Map<String, String> subTests = urlCrawler.scrapeSubTests();
                Map<String, List<String>> tickets = urlCrawler.getTicketsUrl(subTests);
                log.info("Tickets size: {}", tickets.size());
                for (Map.Entry<String, List<String>> entry : tickets.entrySet()) {
                    QuestionsParser questionsParser = new QuestionsParser(entry.getValue(), entry.getKey());
                    questionList = questionsParser.iterateTickets();
                    log.info("Questions in ticket: {}", questionList.size());
                    if (!questionList.isEmpty()) {
                        CreateExcel excelDemo = new CreateExcel(entry.getKey());
                        excelDemo.createExcel(questionList);
                    }
                }
            }
            seleniumHandler.stop();
        }
    }
}
