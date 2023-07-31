package com.example.stockproject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class StockProjectApplication {
    public static void main(String[] args) {
        // SpringApplication.run(StockProjectApplication.class, args);

        try {
            String url = "https://finance.yahoo.com/quote/COKE/history?" +
                    "period1=99100800&period2=1690761600" +
                    "&interval=1mo" +
                    "&filter=history" +
                    "&frequency=1mo" +
                    "&includeAdjustedClose=true";
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element element = elements.get(0); // <table></table>
            Element tbody = element.children().get(1); // <tbody></tbody>

            // tbody의 모든 자식 중, Dividend으로 끝나는 원소 찾기
            for(Element e : tbody.children()){
                String txt = e.text();
                if(!txt.endsWith("Dividend")){
                    continue;
                }

                // Jul 27, 2023 0.5 Dividend
                String[] splits = txt.split("");
                String month = splits[0];
                int day = Integer.valueOf(splits[1].replace(",", ""));
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}