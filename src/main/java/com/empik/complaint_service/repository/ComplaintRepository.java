package com.empik.complaint_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<ComplaintEntity, Long> {

    Optional<ComplaintEntity> findByReporterNameAndProductId(String reporterName, Long productId);
}
