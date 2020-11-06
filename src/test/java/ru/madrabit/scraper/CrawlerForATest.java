package ru.madrabit.scraper;

import org.junit.Assert;
import org.junit.Test;

public class CrawlerForATest {
    @Test
    public void getFirstLetterTicketsReturnSizeMoreThen0() {
        CrawlerForA crawler = new CrawlerForA();
        crawler.start();
        Assert.assertEquals(true, crawler.getScrapeTickets().size() > 0);

    }

    @Test
    public void getAllQuestionsFromAThenQSizeMoreThen0() {
        CrawlerForA crawler = new CrawlerForA();
        crawler.start();
        Assert.assertEquals(true, crawler.getQuestionList().size() > 0);
    }
}
