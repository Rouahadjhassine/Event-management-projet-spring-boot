package com.example.eventmanagement.repository;

import com.example.eventmanagement.models.EspaceEvenement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EspaceEvenementRepository extends JpaRepository<EspaceEvenement, Long> {
    // Trouver un espace par type
    List<EspaceEvenement> findByTypeEspace(String typeEspace);

    // Trouver un espace par son nom complet
    Optional<EspaceEvenement> findByNomComplet(String nomComplet);

    List<EspaceEvenement> findByPrestataireId(Long prestataireId);

    List<EspaceEvenement> findByCapaciteAndTypeEspaceAndDisponibilite(Integer capacite, String typeEspace, String disponible);

    List<EspaceEvenement> findByNomCompletContainingIgnoreCase(String nom);
    List<EspaceEvenement> findByAdresseCompleteContainingIgnoreCase(String adresseCafe);
}