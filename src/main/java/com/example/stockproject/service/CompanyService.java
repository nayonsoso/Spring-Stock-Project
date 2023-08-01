package com.example.stockproject.service;

import com.example.stockproject.model.Company;
import com.example.stockproject.model.ScrapedResult;
import com.example.stockproject.persist.entity.CompanyEntity;
import com.example.stockproject.persist.entity.DividendEntity;
import com.example.stockproject.persist.repository.CompanyRepository;
import com.example.stockproject.persist.repository.DividendRepository;
import com.example.stockproject.scraper.Scraper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {
    private final Scraper yahooFinanceScraper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public Company save(String ticker){
        boolean exist = companyRepository.existsByTicker(ticker);
        if(exist){
            // 이미 있는 것을 저장하려 한다면 에러 발생
            throw new RuntimeException("already exist ticker -> "+ ticker);
        }
        return this.storeCompanyAndDividend(ticker);
    }
    private Company storeCompanyAndDividend(String ticker){
        // ticker 를 기준으로 회사를 스크래핑
        Company company = this.yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if(ObjectUtils.isEmpty(company)){
            throw new RuntimeException("Failed to scrap ticker ->" + ticker);
        }

        // 해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(company);

        // 스크랩 결과
        CompanyEntity companyEntity = this.companyRepository.save(new CompanyEntity(company));
        List<DividendEntity> dividendEntities = scrapedResult.getDividendEntities().stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e))
                        .collect(Collectors.toList());
        this.dividendRepository.saveAll(dividendEntities);

        return company;
    }

    public List<Company> getAllCompany(){
        return companyRepository.findAll();
    }
}
