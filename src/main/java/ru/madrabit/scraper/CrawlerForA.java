package ru.madrabit.scraper;

import lombok.extern.slf4j.Slf4j;
import ru.madrabit.scraper.config.SeleniumHandler;
import ru.madrabit.scraper.domen.Question;
import ru.madrabit.scraper.poi.CreateExcel;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class CrawlerForA implements Crawler {

    private SeleniumHandler seleniumHandler = SeleniumHandler.getSeleniumHandler();
    private Util util = Util.getUtil();
    private List<String> ticketsList = new LinkedList<>();
    List<Question> questionList = new LinkedList<>();

    @Override
    public void start() {
        if (seleniumHandler.start()) {
            seleniumHandler.openPage(ElementsConst.A_TICKETS);
            log.info("Opened page with tickets: {}", ElementsConst.A_TICKETS);
            ticketsList = util.scrapeTickets();
            log.info("Tickets collected: {}", ticketsList.size());
            QuestionsParser questionsParser = new QuestionsParser(ticketsList, "A.1");
            questionList = questionsParser.iterateTickets();
            log.info("Questions in ticket: {}",questionList.size());
//            for (String scrapeTicket : ticketsList) {
//                util.moveToUrl(scrapeTicket);
//                seleniumHandler.jumpToResult();
//                questionList.addAll(util.getAllQuestions("A.1"));
//            }
//            log.info("Questions collectes: {}", questionList.size());
            seleniumHandler.stop();
        }
        if (!questionList.isEmpty()) {
            CreateExcel excelDemo = new CreateExcel("A.1");
            excelDemo.createExcel(questionList);
        }
    }




    public List<String> getScrapeTickets() {
        return ticketsList;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }
}
