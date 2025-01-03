package com.example.eventmanagement.services;

import com.example.eventmanagement.models.EspaceEvenement;
import com.example.eventmanagement.repository.EspaceEvenementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class EspaceEvenementService {

    @Autowired
    private EspaceEvenementRepository espaceEvenementRepository;

    // Méthode pour enregistrer un nouvel espace événement
    public EspaceEvenement enregistrerEspace(EspaceEvenement espaceEvenement) {
        // Vous pouvez ajouter des calculs ou des transformations avant d'enregistrer l'espace
        // Exemple: calculer le coutTotal
        calculerCoutTotal(espaceEvenement);

        // Sauvegarder l'espace dans la base de données
        return espaceEvenementRepository.save(espaceEvenement);
    }

    // Méthode pour récupérer tous les espaces
    public List<EspaceEvenement> getAllEspaces() {
        return espaceEvenementRepository.findAll();
    }

    // Méthode pour récupérer un espace par son ID
    public Optional<EspaceEvenement> getEspaceById(Long id) {
        return espaceEvenementRepository.findById(id);
    }

    // Méthode pour calculer le coût total en fonction des services
    private void calculerCoutTotal(EspaceEvenement espaceEvenement) {
        BigDecimal coutTotal = BigDecimal.ZERO;

        if (espaceEvenement.getPrixDecoration() != null) {
            coutTotal = coutTotal.add(espaceEvenement.getPrixDecoration());
        }
        if (espaceEvenement.getPrixPhotographie() != null) {
            coutTotal = coutTotal.add(espaceEvenement.getPrixPhotographie());
        }
        if (espaceEvenement.getPrixBoissons() != null) {
            coutTotal = coutTotal.add(espaceEvenement.getPrixBoissons());
        }
        if (espaceEvenement.getPrixBuffet() != null) {
            coutTotal = coutTotal.add(espaceEvenement.getPrixBuffet());
        }

        // Calculer le coût total en fonction du nombre de personnes
        if (espaceEvenement.getNombrePersonnes() != null) {
            coutTotal = coutTotal.multiply(new BigDecimal(espaceEvenement.getNombrePersonnes()));
        }

        // Assigner le coût total à l'objet
        espaceEvenement.setCoutTotal(coutTotal);
    }
    public List<EspaceEvenement> getEspacesParPrestataire(Long prestataireId) {
        return espaceEvenementRepository.findByPrestataireId(prestataireId);
    }

}
