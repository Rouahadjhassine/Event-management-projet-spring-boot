package com.example.eventmanagement.repository;

import com.example.eventmanagement.models.Prestataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestataireRepository extends JpaRepository<Prestataire, Long> {
    List<Prestataire> findAll();
    Optional<Prestataire> findByEmail(String email);
    Optional<Prestataire> findById(Long id);
    // Vous pouvez ajouter des méthodes personnalisées si nécessaire, par exemple:
    // Optional<Prestataire> findByEmail(String email);
}
