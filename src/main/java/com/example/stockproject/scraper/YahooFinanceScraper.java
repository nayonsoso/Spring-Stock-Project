package com.example.stockproject.scraper;

import com.example.stockproject.model.Company;
import com.example.stockproject.model.Dividend;
import com.example.stockproject.model.ScrapedResult;
import com.example.stockproject.model.constants.Month;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper {
    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
    private static final long START_TIME = 86400; // 60*60*24

    @Override
    public ScrapedResult scrap(Company company) {
        ScrapedResult scrapedResult = new ScrapedResult();
        scrapedResult.setCompany(company);

        try {
            long now = System.currentTimeMillis()/1000;

            String url = String.format(STATISTICS_URL, company.getTicker(), START_TIME, now);
            Document document = Jsoup.connect(url).get();

            Elements parsingDiv = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element tableEle = parsingDiv.get(0); // <table></table>
            Element tbody = tableEle.children().get(1); // <tbody></tbody>

            List<Dividend> dividenList = new ArrayList<>();
            for (Element e : tbody.children()) { // tbody의 모든 자식 중, Dividend으로 끝나는 원소 찾기
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                }

                // txt example : Jul 27, 2023 0.5 Dividend
                String[] splits = txt.split(" ");
                int month = Month.strToNumber(splits[0]);
                int day = Integer.valueOf(splits[1].replace(",", ""));
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];

                if (month < 0) {
                    throw new RuntimeException("Unexpected Month enum value -> " + month);
                }

                dividenList.add( new Dividend(LocalDateTime.of(year, month, day, 0, 0), dividend));
            }
            scrapedResult.setDividendEntities(dividenList);

        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }

        return scrapedResult;
    }

    @Override
    public Company scrapCompanyByTicker(String ticker){
        try {
            String url = String.format(SUMMARY_URL, ticker, ticker);
            Document document = Jsoup.connect(url).get();

            // h1태그 중 첫번째것 중에 -으로 구분하여 두번째 것을 띄어쓰기 없이 가져옴
            Element titleEle = document.getElementsByTag("h1").get(0);
            String title = titleEle.text().split(" - ")[1].trim();
            return new Company(ticker, title);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
