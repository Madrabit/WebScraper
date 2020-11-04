package ru.madrabit.scraper.scraper;

import lombok.extern.slf4j.Slf4j;
import ru.madrabit.scraper.scraper.config.SeleniumHandler;
import ru.madrabit.scraper.scraper.domen.Question;
import ru.madrabit.scraper.scraper.poi.CreateExcel;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class CrawlerForA implements Crawler {

    private SeleniumHandler seleniumHandler = new SeleniumHandler();
    private Util util = new Util(seleniumHandler);
    private List<String> scrapeTickets = new LinkedList<>();
    List<Question> questionList = new LinkedList<>();

    @Override
    public void start() {
        if (seleniumHandler.start()) {
            seleniumHandler.openPage(ElementsConst.A_TICKETS);
            log.info("Opened page with tickets: {}", ElementsConst.A_TICKETS);
            scrapeTickets = util.scrapeTickets();
            log.info("Tickets collected: {}", scrapeTickets.size());
            for (String scrapeTicket : scrapeTickets) {
                util.moveToUrl(scrapeTicket);
                seleniumHandler.jumpToResult();
                questionList.addAll(util.getAllQuestions("A.1"));
            }
            log.info("Questions collectes: {}", questionList.size());
            seleniumHandler.stop();
        }
        if (!questionList.isEmpty()) {
            CreateExcel excelDemo = new CreateExcel("A.1");
            excelDemo.createExcel(questionList);
        }
    }




    public List<String> getScrapeTickets() {
        return scrapeTickets;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }
}
