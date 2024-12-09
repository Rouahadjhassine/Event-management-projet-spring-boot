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
    public String showProfileAndSearchForm(Model model) {
        // Simulation : remplacer par l'authentification réelle
        Participant participant = participantRepository.findById(1L).orElse(null);

        if (participant == null) {
            return "redirect:/login"; // Rediriger vers la page de connexion si l'utilisateur n'est pas authentifié
        }

        // Ajouter les attributs au modèle
        model.addAttribute("participant", participant);
        model.addAttribute("espace", new EspaceEvenement()); // Pour le formulaire de recherche

        return "utilisateur/profile-search"; // Correspond à profile-search.html
    }

    // Traitement du formulaire de recherche
    @PostMapping("/search-results")
    public String searchSpaces(@ModelAttribute EspaceEvenement criteria, Model model) {
        // Simulation : remplacer par l'authentification réelle
        Participant participant = participantRepository.findById(1L).orElse(null);

        if (participant == null) {
            return "redirect:/login";
        }

        // Enregistrer la demande dans la base
        FormulaireDemande demande = new FormulaireDemande();
        demande.setCapacite(criteria.getCapacite());
        demande.setTypeEspace(criteria.getTypeEspace());
        demande.setDateDemande(LocalDate.now());
        demande.setParticipant(participant);
        demande.setStatus("En cours");

        formulaireDemandeRepository.save(demande);

        // Rechercher les espaces correspondants
        List<EspaceEvenement> spaces = espaceEvenementRepository.findByCapaciteAndTypeEspaceAndDisponibilite(
                criteria.getCapacite(),
                criteria.getTypeEspace(),
                "Disponible"
        );

        model.addAttribute("spaces", spaces);
        return "utilisateur/search-results"; // Correspond à search-results.html
    }

    // Recherche des prestataires par nom
    @GetMapping("/search-prestataires")
    public String searchPrestataires(@RequestParam("nom") String nom, Model model) {
        List<EspaceEvenement> espaces = espaceEvenementRepository.findByNomCompletContainingIgnoreCase(nom);

        model.addAttribute("spaces", espaces);
        model.addAttribute("searchQuery", nom); // Ajouter la requête de recherche au modèle
        return "utilisateur/search-results"; // Affiche les résultats correspondant au prestataire
    }


    // Détails d'un espace
    @GetMapping("/space-details/{id}")
    public String showSpaceDetails(@PathVariable Long id, Model model) {
        EspaceEvenement espace = espaceEvenementRepository.findById(id).orElse(null);

        if (espace == null) {
            return "redirect:/utilisateur/profile"; // Rediriger en cas d'espace non trouvé
        }

        model.addAttribute("space", espace);
        return "utilisateur/space-details";
    }

    // Déconnexion (redirige vers une page définie dans Spring Security)
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login"; // Par défaut, Spring Security gère cette redirection
    }
}