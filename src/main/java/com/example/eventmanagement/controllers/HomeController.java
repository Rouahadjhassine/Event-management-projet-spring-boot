package com.example.eventmanagement.controllers;

import com.example.eventmanagement.models.EspaceEvenement;
import com.example.eventmanagement.models.Participant;
import com.example.eventmanagement.models.Prestataire;
import com.example.eventmanagement.repository.ParticipantRepository;
import com.example.eventmanagement.repository.PrestataireRepository;
import com.example.eventmanagement.services.ParticipantService;
import com.example.eventmanagement.services.PrestataireService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private ParticipantService participantService;
    @Autowired
    private PrestataireService prestataireService;
    @Autowired
    private ParticipantRepository participantRepository;
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
                // Redirection vers la page 'homeprestataire' si le prestataire est authentifié
                return "redirect:/utilisateur/homeprestataire";
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
                return "redirect:/utilisateur/home";
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



}

