package com.wellness.wellnessappbackend.ai.advice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public interface AiAdviceRepository extends JpaRepository<AiAdvice, Long> {

    Optional<AiAdvice> findFirstByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<AiAdvice> findByIdAndUserId(Long id, Long userId);

    @Query("""
            select advice from AiAdvice advice
            where advice.user.id = :userId
              and (:startDate is null or advice.adviceDate >= :startDate)
              and (:endDate is null or advice.adviceDate <= :endDate)
            """)
    Page<AiAdvice> findByUserAndOptionalAdviceDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );
}
