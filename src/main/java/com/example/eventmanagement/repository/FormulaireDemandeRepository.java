package com.example.eventmanagement.repository;

import com.example.eventmanagement.models.FormulaireDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormulaireDemandeRepository extends JpaRepository<FormulaireDemande, Long> {
    List<FormulaireDemande> findByParticipantId(Long participantId);
}
