package com.wellness.wellnessappbackend.ai.advice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

public interface AiAdviceRepository extends JpaRepository<AiAdvice, Long> {

    Optional<AiAdvice> findFirstByUserIdOrderByCreatedAtDesc(Long userId);
}
