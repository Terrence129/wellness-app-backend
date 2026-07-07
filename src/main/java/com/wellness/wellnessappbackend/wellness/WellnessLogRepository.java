package com.wellness.wellnessappbackend.wellness;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public interface WellnessLogRepository extends JpaRepository<WellnessLog, Long> {

    boolean existsByUserIdAndLogDate(Long userId, LocalDate logDate);

    Optional<WellnessLog> findByIdAndUserId(Long id, Long userId);

    Optional<WellnessLog> findByUserIdAndLogDate(Long userId, LocalDate logDate);

    @Query("""
            select log from WellnessLog log
            where log.user.id = :userId
              and (:startDate is null or log.logDate >= :startDate)
              and (:endDate is null or log.logDate <= :endDate)
            """)
    Page<WellnessLog> findByUserAndOptionalDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    List<WellnessLog> findByUserIdAndLogDateBetweenOrderByLogDateAsc(Long userId, LocalDate startDate, LocalDate endDate);
}
