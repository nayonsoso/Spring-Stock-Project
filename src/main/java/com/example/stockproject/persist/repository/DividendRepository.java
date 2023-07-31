package com.example.stockproject.persist.repository;

import com.example.stockproject.persist.entity.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DividendRepository extends JpaRepository<DividendEntity, Long> {
}
