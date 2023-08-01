package com.example.stockproject.scraper;

import com.example.stockproject.model.Company;
import com.example.stockproject.model.ScrapedResult;
import org.springframework.stereotype.Component;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}