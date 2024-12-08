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

    @GetMapping("/profile")
    public String showProfile(Model model) {
        // Remplacez par l'authentification réelle
        Participant participant = participantRepository.findById(1L).orElse(null);
        model.addAttribute("participant", participant);
        return "utilisateur/profile";
    }

    @GetMapping("/search-form")
    public String showSearchForm(Model model) {
        model.addAttribute("espace", new EspaceEvenement());
        return "utilisateur/search-form";
    }

    @PostMapping("/search-results")
    public String searchSpaces(@ModelAttribute EspaceEvenement criteria, Model model) {
        // Simulation : Remplacez avec l'authentification réelle
        Participant participant = participantRepository.findById(1L).orElse(null);

        // Enregistrer la demande dans la base
        FormulaireDemande demande = new FormulaireDemande();
        demande.setCapacite(criteria.getCapacite());
        demande.setTypeEspace(criteria.getTypeEspace());
        demande.setDateDemande(LocalDate.now());
        demande.setParticipant(participant);
        demande.setStatus("En cours");

        formulaireDemandeRepository.save(demande);

        // Recherche des espaces correspondants
        List<EspaceEvenement> spaces = espaceEvenementRepository.findByCapaciteAndTypeEspaceAndDisponibilite(
                criteria.getCapacite(),
                criteria.getTypeEspace(),
                "Disponible"
        );

        model.addAttribute("spaces", spaces); // Utiliser 'spaces' ici, qui contient des objets EspaceEvenement
        return "utilisateur/search-results";
    }


    @GetMapping("/space-details/{id}")
    public String showSpaceDetails(@PathVariable Long id, Model model) {
        EspaceEvenement espace = espaceEvenementRepository.findById(id).orElse(null);
        model.addAttribute("espace", espace); // 'espace' contient l'entité EspaceEvenement
        return "utilisateur/space-details";
    }


    @PostMapping("/confirm-request/{id}")
    public String confirmRequest(@PathVariable Long id) {
        // Simulation : Remplacez avec l'authentification réelle
        Participant participant = participantRepository.findById(1L).orElse(null);

        // Récupérer la demande en cours pour ce participant
        FormulaireDemande demande = formulaireDemandeRepository.findByParticipantId(participant.getId())
                .stream()
                .filter(d -> d.getStatus().equals("En cours"))
                .findFirst()
                .orElse(null);

        if (demande != null) {
            demande.setStatus("Confirmée");
            formulaireDemandeRepository.save(demande);
        }

        System.out.println("Demande confirmée pour l'espace ID: " + id);
        return "redirect:/participants/profile"; // Redirection vers le profil du participant
    }
}

