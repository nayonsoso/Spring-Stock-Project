package com.example.stockproject.controller;

import com.example.stockproject.model.ScrapedResult;
import com.example.stockproject.service.FinanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance")
@AllArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/dividend/{companyName}")
    public ResponseEntity<?> searchFinance(@PathVariable String companyName){
        ScrapedResult scrapedResult = financeService.getDividendByCompanyName(companyName);
        return ResponseEntity.ok(scrapedResult);
    }
}
