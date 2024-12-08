package com.example.eventmanagement.controllers;

import com.example.eventmanagement.models.EspaceEvenement;
import com.example.eventmanagement.models.FormulaireDemande;
import com.example.eventmanagement.models.Participant;
import com.example.eventmanagement.models.Prestataire;
import com.example.eventmanagement.repository.EspaceEvenementRepository;
import com.example.eventmanagement.repository.FormulaireDemandeRepository;
import com.example.eventmanagement.repository.ParticipantRepository;
import com.example.eventmanagement.repository.PrestataireRepository;
import com.example.eventmanagement.services.ParticipantService;
import com.example.eventmanagement.services.PrestataireService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private EspaceEvenementRepository espaceEvenementRepository;

    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private FormulaireDemandeRepository formulaireDemandeRepository;

    @Autowired
    private ParticipantService participantService;
    @Autowired
    private PrestataireService prestataireService;

    @Autowired
    private PrestataireRepository prestataireRepository;

    @GetMapping("utilisateur/participant/register")
    public String participantRegister(Model model) {
        model.addAttribute("participant", new Participant());
        return "utilisateur/participaninscription"; // Retourne la vue d'inscription
    }

    @PostMapping("utilisateur/participant/register")
    public String registerParticipant(Participant participant, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "utilisateur/participaninscription"; // Si des erreurs de validation, revenir au formulaire
        }
        participantService.saveParticipant(participant); // Sauvegarde dans la base de données
        return "redirect:/utilisateur/home"; // Redirection après l'enregistrement
    }

    @GetMapping("utilisateur/home")
    public String home() {
        return "utilisateur/home"; // Page d'accueil après inscription
    }
    @GetMapping("utilisateur/homeprestataire")
    public String homeprestataire() {
        return "utilisateur/homeprestataire"; // Page d'accueil après inscription
    }

    @GetMapping("/Login")
    public String login() {
        return "utilisateur/loginclient"; // Page d'accueil après inscription
    }

    @PostMapping("/Login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session) {
        // Vérifier d'abord si l'utilisateur est un Prestataire
        Optional<Prestataire> prestataireOpt = prestataireRepository.findByEmail(email);

        if (prestataireOpt.isPresent()) {
            Prestataire prestataire = prestataireOpt.get();
            if (prestataire.getPassword().equals(password)) {
                session.setAttribute("prestataireId", prestataire.getId());
                session.setAttribute("prestataireNom", prestataire.getName());

                // model.addAttribute("espaceEvenement", new EspaceEvenement());
                // Redirection vers la page 'homeprestataire' si le prestataire est authentifié
                return "redirect:/utilisateur/prestataire/registerespace";
            } else {
                model.addAttribute("error", "Mot de passe incorrect !");
                return "utilisateur/loginclient";
            }
        }

        // Si ce n'est pas un Prestataire, vérifier dans Participant
        Optional<Participant> participantOpt = participantRepository.findByEmail(email);

        if (participantOpt.isPresent()) {
            Participant participant = participantOpt.get();
            if (participant.getPassword().equals(password)) {
                // Redirection vers la page 'home' pour un participant
                return "redirect:/profile";
            } else {
                model.addAttribute("error", "Mot de passe incorrect !");
                return "utilisateur/loginclient";
            }
        }

        // Si l'email n'existe ni pour Prestataire ni pour Participant
        model.addAttribute("error", "Email ou mot de passe incorrect !");
        return "utilisateur/loginclient";
    }






    @GetMapping("/inscriptionclient")
    public String inscription() {
        return "utilisateur/inscriptionClient"; // Page d'accueil après inscription
    }

    @GetMapping("utilisateur/prestataire/register")
    public String prestataireRegister(Model model) {
        model.addAttribute("prestataire", new Prestataire());
        return "utilisateur/prestataireinscription";
    }
    @PostMapping("utilisateur/prestataire/register")
    public String registerPrestataire(Prestataire prestataire, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "utilisateur/prestataireinscription"; // Si des erreurs de validation, revenir au formulaire
        }
        prestataireService.savePrestataire(prestataire); // Sauvegarde dans la base de données
        return "redirect:/utilisateur/home"; // Redirection après l'enregistrement
    }

    @GetMapping("utilisateur/profileprestataire")
    public String profile() {
        return "utilisateur/profileprestataire";
    }
    @GetMapping("/profile")
    public String showProfile(Model model) {
        // Remplacez par l'authentification réelle
        Participant participant = participantRepository.findById(1L).orElse(null);
        model.addAttribute("participant", participant);
        return "utilisateur/profile";
    }

    // Formulaire de recherche
    @GetMapping("/search-form")
    public String showSearchForm(Model model) {
        model.addAttribute("espace", new EspaceEvenement());
        return "utilisateur/search-form";
    }

    // Résultats de recherche
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

        model.addAttribute("spaces", spaces);
        return "utilisateur/search-results";
    }

    // Détails d'un espace
    @GetMapping("/space-details/{id}")
    public String showSpaceDetails(@PathVariable Long id, Model model) {
        EspaceEvenement espace = espaceEvenementRepository.findById(id).orElse(null);
        model.addAttribute("space", espace);
        return "utilisateur/space-details";
    }

    // Confirmation de la demande
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
        return "redirect:/utilisateur/profile";
    }


}