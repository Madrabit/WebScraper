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
            } else {
                letters = urlCrawler.scrapeLetters();
                seleniumHandler.openPage(letters.get(letter));
                Map<String, String> subTests = urlCrawler.scrapeSubTests();
            }




//            log.info("Opened page with tickets: {}", letter);
//            ticketsList = util.scrapeTickets();
//            log.info("Tickets collected: {}", ticketsList.size());

//            QuestionsParser questionsParser = new QuestionsParser(ticketsList, "A.1");
//            questionList = questionsParser.iterateTickets();
//            log.info("Questions in ticket: {}", questionList.size());


//            for (String scrapeTicket : ticketsList) {
//                util.moveToUrl(scrapeTicket);
//                seleniumHandler.jumpToResult();
//                questionList.addAll(util.getAllQuestions("A.1"));
//            }
//            log.info("Questions collectes: {}", questionList.size());
//            seleniumHandler.stop();
        }
//        if (!questionList.isEmpty()) {
//            CreateExcel excelDemo = new CreateExcel("A.1");
//            excelDemo.createExcel(questionList);
//        }
    }


}
