package com.example.stockproject.persist.repository;

import com.example.stockproject.persist.entity.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DividendRepository extends JpaRepository<DividendEntity, Long> {
    List<DividendEntity> findAllByCompanyId(Long companyId);
}
