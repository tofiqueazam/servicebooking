package com.example.servicebooking.repository;

import com.example.servicebooking.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.servicebooking.entity.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;



public interface ProviderRepository
        extends JpaRepository<Provider, Long> {

    List<Provider> findByCityIgnoreCase(String city);

    List<Provider> findByApprovedTrue();

    boolean existsByEmail(String email);
    List<Provider> findByApprovedFalse();
    
    @Query("""
    SELECT p FROM Provider p
    WHERE p.approved = true
      AND (:city IS NULL OR LOWER(p.city) = LOWER(:city))
      AND (:name IS NULL OR
           LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
    """)
Page<Provider> searchApprovedProviders(
        @Param("city") String city,
        @Param("name") String name,
        Pageable pageable
);
}
