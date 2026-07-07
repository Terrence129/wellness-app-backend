package com.wellness.wellnessappbackend.wellness;

import com.wellness.wellnessappbackend.user.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * @description:
 * @author: chenyaqi
 * @email: terrence.yaqi.chen@u.nus.edu
 * @date: 
 */

@Entity
@Table(
        name = "wellness_logs",
        uniqueConstraints = @UniqueConstraint(name = "uq_wellness_logs_user_date", columnNames = {"user_id", "log_date"})
)
@Getter
@Setter
public class WellnessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "sleep_hours", precision = 4, scale = 2)
    private BigDecimal sleepHours;

    @Column(name = "mood_score")
    @JdbcTypeCode(SqlTypes.TINYINT)
    private Integer moodScore;

    @Column(name = "water_cups")
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Integer waterCups;

    private Integer steps;

    @Column(name = "exercise_minutes")
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Integer exerciseMinutes;

    @Column(length = 1000)
    private String note;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
