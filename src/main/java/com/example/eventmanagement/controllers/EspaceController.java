package com.example.eventmanagement.controllers;

import com.example.eventmanagement.models.EspaceEvenement;
import com.example.eventmanagement.models.Prestataire;
import com.example.eventmanagement.repository.EspaceEvenementRepository;
import com.example.eventmanagement.repository.PrestataireRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;  // Ajoutez l'annotation @Controller ici
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller  // L'annotation @Controller est indispensable pour indiquer que c'est un contrôleur Spring
public class EspaceController {

    @Autowired
    private PrestataireRepository prestataireRepository;

    @Autowired
    private EspaceEvenementRepository espaceEvenementRepository;
    @GetMapping("/utilisateur/prestataire/registerespace")
    public String showRegisterForm(HttpSession session, Model model) {
        // Récupérer l'ID du prestataire connecté
        Long prestataireId = (Long) session.getAttribute("prestataireId");
        model.addAttribute("espaceEvenement", new EspaceEvenement());

        if (prestataireId == null) {
            model.addAttribute("error", "Veuillez vous connecter pour ajouter un espace.");
            return "utilisateur/loginclient"; // Rediriger vers la page de connexion si non connecté
        }

        // Initialiser un objet EspaceEvenement vide pour remplir le formulaire


        return "utilisateur/registerespace"; // Nom de la page contenant le formulaire
    }

    @PostMapping("/utilisateur/prestataire/registerespace")
    public String registerEspace(@ModelAttribute EspaceEvenement espaceEvenement,
                                 HttpSession session,
                                 Model model) {
        // Récupération de l'ID du prestataire connecté
        Long prestataireId = (Long) session.getAttribute("prestataireId");

        if (prestataireId == null) {
            model.addAttribute("error", "Veuillez vous connecter pour ajouter un espace.");
            return "utilisateur/loginclient";
        }

        // Récupérer le prestataire depuis l'ID
        Optional<Prestataire> prestataireOpt = prestataireRepository.findById(prestataireId);
        if (prestataireOpt.isEmpty()) {
            model.addAttribute("error", "Prestataire non valide.");
            return "utilisateur/loginclient";
        }

        // Associer l'espace au prestataire
        Prestataire prestataire = prestataireOpt.get();
        espaceEvenement.setPrestataire(prestataire);

        // Sauvegarder l'espace dans la base
        espaceEvenementRepository.save(espaceEvenement);

        // Réinitialiser le formulaire pour l'utilisateur
        model.addAttribute("espaceEvenement", new EspaceEvenement()); // Créer un nouveau EspaceEvenement pour vider le formulaire

        // Retourner à la page de création d'espace avec un formulaire réinitialisé
        return "utilisateur/homeprestataire";  // Changez "utilisateur/registerespace" pour le nom de votre vue de formulaire
    }


}
