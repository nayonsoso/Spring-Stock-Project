package com.example.stockproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// xxxMapping 경로의 공통된 부분을 컨트롤러로 빼줄 수 있음
@RestController()
@RequestMapping("/company")
public class CompanyController {
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword){
        return null;
    }

    @GetMapping
    public ResponseEntity<?> searchCompany(){
        return null;
    }

    @PostMapping
    public ResponseEntity<?> addCompany(){
        return null;
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCompany(){
        return null;
    }
}
