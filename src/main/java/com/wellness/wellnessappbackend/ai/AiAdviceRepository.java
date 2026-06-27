package com.wellness.wellnessappbackend.ai;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AiAdviceRepository extends JpaRepository<AiAdvice, Long> {

    Optional<AiAdvice> findFirstByUserIdOrderByCreatedAtDesc(Long userId);
}
