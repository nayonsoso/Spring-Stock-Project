package com.example.stockproject.scheduler;

import com.example.stockproject.model.Company;
import com.example.stockproject.model.ScrapedResult;
import com.example.stockproject.model.constants.CacheKey;
import com.example.stockproject.persist.entity.CompanyEntity;
import com.example.stockproject.persist.entity.DividendEntity;
import com.example.stockproject.persist.repository.CompanyRepository;
import com.example.stockproject.persist.repository.DividendRepository;
import com.example.stockproject.scraper.Scraper;
import com.example.stockproject.scraper.YahooFinanceScraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final YahooFinanceScraper scraper;
   
    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true) // finace 에 해당하는 캐시는 모두 비우겠다는 뜻
    @Scheduled(cron = "${scheduler.scrap.yahoo}") // 일정 주기마다 수행 - 매일 자정마다
    public void yahooFinanceScheduling() {

        // 저장된 회사 목록을 조회
        List<CompanyEntity> companies = this.companyRepository.findAll();

        // 회사마다 배당금 정보를 스크래핑
        for (var company : companies) {
            log.info("scrapping scheduler is started -> " + company.getName());

            ScrapedResult scrapedResult = this.scraper.scrap(
                    new Company(company.getTicker(), company.getName())
            );
            // 스크래핑한 배당금 정보 중 DB 에 없는 값은 저장
            scrapedResult.getDividendEntities().stream()
                    // 디비든 모델을 디비든 엔티티로 매핑
                    .map(e -> new DividendEntity(company.getId(), e))
                    // 엘리먼트를 하나씩 디비든 레파지토리에 삽입
                    .forEach(e -> {
                        boolean exist = this.dividendRepository
                                .existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if (!exist) {
                            this.dividendRepository.save(e);
                        }
                    });

            // 연속적으로 스크래핑 대상 사이츠 서버에 요청을 날리지 않도록 일시정지 - 쓰레드를 잠시 멈춤
            try {
                Thread.sleep(3000); // 3sec
            } catch (InterruptedException e) {
                // InterruptedException 은 추가적인 처리가 필요 (그렇지 않으면 쓰레드가 종료되지 않을 수도 있음)
                Thread.currentThread().interrupt();
            }

        }

    }
}
