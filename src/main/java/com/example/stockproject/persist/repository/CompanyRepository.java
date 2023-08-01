package com.example.stockproject.persist.repository;

import com.example.stockproject.persist.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    boolean existsByTicker(String ticker);
    Optional<CompanyEntity> findByName(String name);
}
