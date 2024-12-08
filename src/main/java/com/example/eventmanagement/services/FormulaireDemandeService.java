package com.example.eventmanagement.services;

import com.example.eventmanagement.models.FormulaireDemande;
import com.example.eventmanagement.repository.FormulaireDemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
    public class FormulaireDemandeService {

        @Autowired
        private FormulaireDemandeRepository formulaireDemandeRepository;

        public List<FormulaireDemande> getDemandesForParticipant(Long participantId) {
            return formulaireDemandeRepository.findByParticipantId(participantId);
        }

        public FormulaireDemande createDemande(FormulaireDemande demande) {
            return formulaireDemandeRepository.save(demande);
        }

        public FormulaireDemande updateDemandeStatus(Long demandeId, String status) {
            FormulaireDemande demande = formulaireDemandeRepository.findById(demandeId).orElse(null);
            if (demande != null) {
                demande.setStatus(status);
                return formulaireDemandeRepository.save(demande);
            }
            return null;
        }
    }


