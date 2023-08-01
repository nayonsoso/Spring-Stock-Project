package com.example.stockproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ScrapedResult {
    private Company company;
    private List<Dividend> dividendEntities;

    public ScrapedResult() {this.dividendEntities = new ArrayList<>(); }
}
