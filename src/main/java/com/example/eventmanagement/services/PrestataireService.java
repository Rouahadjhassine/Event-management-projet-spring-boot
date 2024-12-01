package com.example.eventmanagement.services;

import com.example.eventmanagement.models.Prestataire;
import com.example.eventmanagement.repository.PrestataireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrestataireService {

    @Autowired
    private PrestataireRepository prestataireRepository;

    /**
     * Sauvegarde un prestataire dans la base de données.
     */
    @Transactional
    public void savePrestataire(Prestataire prestataire) {
        prestataireRepository.save(prestataire);
    }

    /**
     * Récupère tous les prestataires.
     */
    public List<Prestataire> getAllPrestataires() {
        return prestataireRepository.findAll(); // Récupère tous les prestataires
    }

    /**
     * Mise à jour des informations d'un prestataire existant.
     */
    @Transactional
    public Prestataire updatePrestataire(Long id, Prestataire prestataireDetails) {
        return prestataireRepository.findById(id).map(prestataire -> {
            // Mise à jour des champs avec les nouvelles données
            if (prestataireDetails.getName() != null) prestataire.setName(prestataireDetails.getName());
            if (prestataireDetails.getEmail() != null) prestataire.setEmail(prestataireDetails.getEmail());
            if (prestataireDetails.getPhoneNumber() != null)
                prestataire.setPhoneNumber(prestataireDetails.getPhoneNumber());
            if (prestataireDetails.getLocalAddress() != null)
                prestataire.setLocalAddress(prestataireDetails.getLocalAddress());
            if (prestataireDetails.getLocalName() != null) prestataire.setLocalName(prestataireDetails.getLocalName());
            if (prestataireDetails.getVerificationImage() != null)
                prestataire.setVerificationImage(prestataireDetails.getVerificationImage());
            prestataire.setValidated(prestataireDetails.isValidated()); // Correction ici

            return prestataireRepository.save(prestataire);
        }).orElseThrow(() -> new IllegalArgumentException("Prestataire introuvable avec l'ID : " + id));
    }
}