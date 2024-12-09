package com.example.eventmanagement.controllers;

import com.example.eventmanagement.models.EspaceEvenement;
import com.example.eventmanagement.models.FormulaireDemande;
import com.example.eventmanagement.models.Participant;
import com.example.eventmanagement.repository.EspaceEvenementRepository;
import com.example.eventmanagement.repository.ParticipantRepository;
import com.example.eventmanagement.repository.FormulaireDemandeRepository;
import com.example.eventmanagement.services.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/utilisateur")
public class ParticipantController {
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private EspaceEvenementRepository espaceEvenementRepository;

    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private FormulaireDemandeRepository formulaireDemandeRepository;


    @PostMapping("/update/{id}")
    public String updateParticipant(
            @PathVariable Long id,
            @ModelAttribute("participant") Participant participant,
            RedirectAttributes redirectAttributes) {
        try {
            Participant updatedParticipant = participantService.updateParticipant(id, participant);
            redirectAttributes.addFlashAttribute("successMessage", "Participant updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/participants/dashboard"; // Redirige vers une page existante
    }

    // Profil après connexion


    // Autowire de vos repositories et services comme précédemment

}

