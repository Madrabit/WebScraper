package ru.madrabit.scraper;

import org.junit.Assert;
import org.junit.Test;
import ru.madrabit.scraper.scraper.CrawlerForA;
import ru.madrabit.scraper.scraper.ScraperApp;

public class ScrapperAppTest {


    @Test
    public void getAllLettersLinks() {
        ScraperApp crawler = new ScraperApp();
        crawler.start();
        Assert.assertEquals(true, crawler.getLetters().size() > 0);
    }

    @Test
    public void getAllSubTests() {
        ScraperApp crawler = new ScraperApp();
        crawler.start();
        Assert.assertEquals(true, crawler.getSubTests().size() > 0);
        for (String subTest : crawler.getSubTests()) {
            System.out.println(subTest);
        }
    }

    @Test
    public void cropUrl() {
        String s = "https://tests24.su/b-1-25-proizvodstvo-vodoroda-metodom-elektroliza-vody/";
        s = s.substring(19);
        String[] result = s.split("-");
        String res = ("b".equals(result[0]) ? "Б" : "Д") + "." + result[1] + "." + result[2];
        System.out.println(res);

    }

    @Test
    public void getLetterB11ThenReturnB11Name() {

    }

    @Test
    public void getSubLetterC1ThenReturnTickets() {

    }

    @Test
    public void getSubLetterC1ThenReturnAllQuestionsOfC1() {

    }

    @Test

    public void saveC1ResultToUniqueExcelFile() {

    }



}
