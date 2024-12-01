package com.example.eventmanagement.controllers;

import com.example.eventmanagement.models.Formulaire;
import com.example.eventmanagement.models.Lieu;
import com.example.eventmanagement.services.LieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class FormulaireController {

    @Autowired
    private LieuService lieuService; // Service pour gérer le filtrage des lieux

    // Afficher le formulaire de filtrage
    @GetMapping("/formulaire")
    public String showFormulaire(Model model) {
        model.addAttribute("formulaire", new Formulaire()); // Ajouter un objet Formulaire vide pour le formulaire
        return "formulaire";  // Vue du formulaire
    }

    // Traiter la soumission du formulaire et afficher les résultats filtrés
    @PostMapping("/formulaire")
    public String filterLieux(@ModelAttribute Formulaire formulaire, Model model) {

        // Filtrer les lieux selon les critères du formulaire
        List<Lieu> filteredLieux = lieuService.filterLieux(formulaire.getNumberOfPeople(),
                formulaire.getIsOpenSpacePreferred(),
                formulaire.getBudget(),
                formulaire.getLocationPreference());

        // Ajouter la liste filtrée au modèle
        model.addAttribute("filteredLieux", filteredLieux);
        return "resultats";  // Afficher la vue des résultats filtrés
    }
}
