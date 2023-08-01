package com.example.stockproject.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Dividend {
    // 왜 회사 이름이 빠질 수 있는건지 이해가 안됨
    private LocalDateTime date;
    private String dividend;
}
