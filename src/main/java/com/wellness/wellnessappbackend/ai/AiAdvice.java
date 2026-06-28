package com.wellness.wellnessappbackend.ai;

import com.wellness.wellnessappbackend.user.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "ai_advice")
@Getter
@Setter
public class AiAdvice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "advice_date", nullable = false)
    private LocalDate adviceDate;

    @Column(name = "source_start_date")
    private LocalDate sourceStartDate;

    @Column(name = "source_end_date")
    private LocalDate sourceEndDate;

    @Lob
    @Column(name = "advice_text", nullable = false, length = 5000)
    private String adviceText;

    @Column(name = "model_name", length = 100)
    private String modelName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
