package com.example.eventmanagement.services;

import com.example.eventmanagement.models.Participant;
import com.example.eventmanagement.models.Prestataire;
import com.example.eventmanagement.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Transactional
    public void saveParticipant(Participant participant) {
        participantRepository.save(participant);

    }
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll(); // Récupère tous les participants
    }

    public void savePrestataire(Prestataire prestataire) {
    }
    @Transactional
    public Participant updateParticipant(Long id, Participant participantDetails) {
        return participantRepository.findById(id).map(participant -> {
            // Mise à jour des champs avec les nouvelles données
            if (participantDetails.getNom() != null) participant.setNom(participantDetails.getNom());
            if (participantDetails.getPrenom() != null) participant.setPrenom(participantDetails.getPrenom());
            if (participantDetails.getEmail() != null) participant.setEmail(participantDetails.getEmail());
            if (participantDetails.getPassword() != null) participant.setPassword(participantDetails.getPassword());
            if (participantDetails.getNumTelephone() != null) participant.setNumTelephone(participantDetails.getNumTelephone());

            return participantRepository.save(participant);
        }).orElseThrow(() -> new IllegalArgumentException("Participant introuvable avec l'ID : " + id));
    }

}
