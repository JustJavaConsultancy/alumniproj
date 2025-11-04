package tech.justjava.alumni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.justjava.alumni.entity.DocumentRequest;

public interface DocumentRequestRepository extends JpaRepository<DocumentRequest, Long> {
    // Custom query methods if needed
}
