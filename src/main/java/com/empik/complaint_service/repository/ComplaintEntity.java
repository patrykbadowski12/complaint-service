package com.empik.complaint_service.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "COMPLAINTS",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_reporter_product",
                columnNames = {"reporterName", "productId"}
        ),
        indexes = {
                @Index(name = "idx_reporter_name_product_id", columnList = "reporterName, productId")
        }
)
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String reporterName;

    @Column(nullable = false)
    private String reporterEmail;

    @Column(nullable = false)
    private String reporterCountry;

    @Column(nullable = false)
    private int reportCount;

    @PrePersist
    protected void onCreate() {
        this.reportCount = 1;
    }
}
