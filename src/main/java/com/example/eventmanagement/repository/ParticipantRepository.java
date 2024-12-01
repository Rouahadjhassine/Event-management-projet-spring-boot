package com.example.eventmanagement.repository;

import com.example.eventmanagement.models.Admin;
import com.example.eventmanagement.models.Participant;
import com.example.eventmanagement.models.Prestataire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface  ParticipantRepository extends JpaRepository<Participant, Long> {
    @Override
    Optional<Participant> findById(Long id);
    Optional<Participant> findByEmail(String email);

    // Vous pouvez ajouter des méthodes personnalisées si nécessaire
}
