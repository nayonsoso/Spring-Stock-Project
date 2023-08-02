package com.example.stockproject.persist.entity;

import com.example.stockproject.model.Dividend;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "dividend")
@Getter
@ToString
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"companyId", "date"})})
public class DividendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long companyId;
    private LocalDateTime date;
    private String dividend;

    public DividendEntity(Long companyId, Dividend dividend) {
        this.companyId = companyId;
        this.dividend = dividend.getDividend();
        this.date = dividend.getDate();
    }
}
