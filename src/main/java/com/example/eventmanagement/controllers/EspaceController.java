package com.example.eventmanagement.controllers;

import com.example.eventmanagement.models.EspaceEvenement;
import com.example.eventmanagement.models.Prestataire;
import com.example.eventmanagement.models.Image;
import com.example.eventmanagement.repository.EspaceEvenementRepository;
import com.example.eventmanagement.repository.PrestataireRepository;
import com.example.eventmanagement.repository.ImageRepository;

import com.example.eventmanagement.services.EspaceEvenementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class EspaceController {

    @Autowired
    private PrestataireRepository prestataireRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private EspaceEvenementRepository espaceEvenementRepository;
    @Autowired
    private EspaceEvenementService espaceEvenementService;

    // Affichage du formulaire d'ajout d'un espace
    @GetMapping("/utilisateur/prestataire/registerespace")
    public String showRegisterForm(HttpSession session, Model model) {
        Long prestataireId = (Long) session.getAttribute("prestataireId");

        if (prestataireId == null) {
            model.addAttribute("error", "Veuillez vous connecter pour ajouter un espace.");
            return "utilisateur/loginclient";
        }


        String prestataireNom = (String) session.getAttribute("prestataireNom");
        model.addAttribute("prestataireNom", prestataireNom);
        model.addAttribute("espaceEvenement", new EspaceEvenement());

        return "utilisateur/profileprestataire";
    }

    // Enregistrement d'un nouvel espace
    @PostMapping("/utilisateur/prestataire/registerespace")
    public String registerEspace(@ModelAttribute EspaceEvenement espaceEvenement,
                                 @RequestParam("imageFiles") MultipartFile[] files,
                                 HttpSession session,
                                 Model model) {
        Long prestataireId = (Long) session.getAttribute("prestataireId");
        if (prestataireId == null) {
            model.addAttribute("error", "Veuillez vous connecter pour ajouter un espace.");
            return "utilisateur/loginclient";
        }

        Optional<Prestataire> prestataireOpt = prestataireRepository.findById(prestataireId);
        if (prestataireOpt.isEmpty()) {
            model.addAttribute("error", "Prestataire non valide.");
            return "utilisateur/loginclient";
        }

        // Lien avec le prestataire et sauvegarde de l'espace
        espaceEvenement.setPrestataire(prestataireOpt.get());
        espaceEvenementRepository.save(espaceEvenement);

        // Traitement des images en Base64
        try {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    byte[] bytes = file.getBytes();
                    String encodedImage = Base64.getEncoder().encodeToString(bytes);

                    Image image = new Image();
                    image.setData(encodedImage);
                    image.setEspaceEvenement(espaceEvenement);
                    imageRepository.save(image);
                }
            }
            List<Image> images = imageRepository.findByEspaceEvenement(espaceEvenement);
            model.addAttribute("images", images);
        } catch (IOException e) {
            model.addAttribute("error", "Erreur lors du téléchargement des images.");
            return "utilisateur/profileprestataire";
        }

        return "redirect:/espace/confirmation-et-details?id=" + espaceEvenement.getId();

    }
    @GetMapping("/espace/confirmation-et-details")
    public String showConfirmationAndDetails(@RequestParam("id") Long espaceId, Model model) {
        Optional<EspaceEvenement> espaceOpt = espaceEvenementRepository.findById(espaceId);
        if (espaceOpt.isEmpty()) {
            model.addAttribute("error", "Espace non trouvé.");
            return "redirect:/utilisateur/profileprestataire";
        }

        EspaceEvenement espace = espaceOpt.get();
        List<Image> images = imageRepository.findByEspaceEvenement(espace);

        model.addAttribute("espace", espace); // Informations de l'espace
        model.addAttribute("images", images); // Images associées
        model.addAttribute("confirmationMessage", "Votre espace a été enregistré avec succès."); // Message de confirmation

        return "utilisateur/confirmation-et-details"; // Vue unifiée
    }

    @GetMapping("/espace/confirmation")
    public String confirmationPage(@RequestParam("id") Long espaceId, Model model) {
        Optional<EspaceEvenement> espaceOpt = espaceEvenementRepository.findById(espaceId);
        if (espaceOpt.isEmpty()) {
            model.addAttribute("error", "Espace non trouvé.");
            return "redirect:/utilisateur/profileprestataire";
        }

        model.addAttribute("espaceId", espaceId);
        return "utilisateur/confirmation";
    }


    // Affichage des photos associées aux espaces d'un prestataire
    @GetMapping("/utilisateur/prestataire/gererphotos")
    public String gererPhotos(Model model, HttpSession session) {
        Long prestataireId = (Long) session.getAttribute("prestataireId");
        if (prestataireId == null) {
            model.addAttribute("error", "Veuillez vous connecter pour gérer vos photos.");
            return "utilisateur/loginclient";
        }

        // Récupération des espaces liés au prestataire
        List<EspaceEvenement> espaces = espaceEvenementRepository.findByPrestataireId(prestataireId);

        // Récupération des images associées à ces espaces
        List<Image> images = imageRepository.findByEspaceEvenementIn(espaces);

        model.addAttribute("images", images); // Ajouter les images pour la vue
        return "utilisateur/gerer-photos"; // Page dédiée
    }


    @PostMapping("/utilisateur/prestataire/deletephoto")
    public String deletePhoto(@RequestParam("imageId") Long imageId, HttpSession session, Model model) {
        Long prestataireId = (Long) session.getAttribute("prestataireId");
        if (prestataireId == null) {
            model.addAttribute("error", "Veuillez vous connecter pour effectuer cette action.");
            return "utilisateur/loginclient";
        }

        // Supprimer l'image uniquement si elle appartient à un espace du prestataire
        Optional<Image> imageOpt = imageRepository.findById(imageId);
        if (imageOpt.isPresent()) {
            Image image = imageOpt.get();
            if (image.getEspaceEvenement().getPrestataire().getId().equals(prestataireId)) {
                imageRepository.delete(image);
            } else {
                model.addAttribute("error", "Vous n'avez pas les droits pour supprimer cette image.");
            }
        }

        return "redirect:/utilisateur/prestataire/gererphotos";
    }




    // Téléchargement d'images pour un espace existant
    @PostMapping("/espace/upload")
    public String uploadImage(@RequestParam("imageFile") MultipartFile file,
                              @RequestParam("espaceId") Long espaceId,
                              Model model) {
        try {
            byte[] bytes = file.getBytes();
            String encodedImage = Base64.getEncoder().encodeToString(bytes);

            EspaceEvenement espace = espaceEvenementRepository.findById(espaceId)
                    .orElseThrow(() -> new RuntimeException("Espace non trouvé"));

            Image image = new Image();
            image.setData(encodedImage);
            image.setEspaceEvenement(espace);

            imageRepository.save(image);

            model.addAttribute("message", "Image uploadée avec succès !");
        } catch (IOException e) {
            model.addAttribute("error", "Erreur lors de l'upload de l'image : " + e.getMessage());
        }

        return "redirect:/espace/details?id=" + espaceId;
    }
    @GetMapping("/espace/details")
    public String showEspaceDetails(@RequestParam("id") Long espaceId, Model model) {
        Optional<EspaceEvenement> espaceOpt = espaceEvenementRepository.findById(espaceId);
        if (espaceOpt.isEmpty()) {
            model.addAttribute("error", "Espace non trouvé.");
            return "redirect:/utilisateur/profileprestataire"; // Redirige si l'espace n'existe pas
        }

        EspaceEvenement espace = espaceOpt.get();
        List<Image> images = imageRepository.findByEspaceEvenement(espace); // Récupère les images associées

        model.addAttribute("espace", espace);
        model.addAttribute("images", images); // Passe les images à la vue

        return "utilisateur/details";  // Nom de la vue Thymeleaf ou JSP à créer
    }
    @PostMapping("/utilisateur/prestataire/update")
    public String updatePrestataire(@ModelAttribute Prestataire prestataire, HttpSession session, Model model) {
        Long prestataireId = (Long) session.getAttribute("prestataireId");

        if (prestataireId == null) {
            model.addAttribute("error", "Veuillez vous connecter pour modifier vos informations.");
            return "utilisateur/loginclient";
        }

        Optional<Prestataire> existingPrestataireOpt = prestataireRepository.findById(prestataireId);
        if (existingPrestataireOpt.isEmpty()) {
            model.addAttribute("error", "Prestataire non trouvé.");
            return "utilisateur/profileprestataire";
        }

        Prestataire existingPrestataire = existingPrestataireOpt.get();

        // Mise à jour des champs du prestataire
        existingPrestataire.setName(prestataire.getName());
        existingPrestataire.setLocalName(prestataire.getLocalName());
        existingPrestataire.setEmail(prestataire.getEmail());
        existingPrestataire.setLocalAddress(prestataire.getLocalAddress());
        existingPrestataire.setPhoneNumber(prestataire.getPhoneNumber());

        if (prestataire.getPassword() != null && !prestataire.getPassword().isEmpty()) {
            existingPrestataire.setPassword(prestataire.getPassword());
        }

        prestataireRepository.save(existingPrestataire);

        model.addAttribute("success", "Vos informations ont été mises à jour avec succès.");

        // Ajoutez l'objet prestataire à la vue avant de rediriger
        model.addAttribute("prestataire", existingPrestataire);

        return "utilisateur/profileprestataire";  // Vous pouvez également rediriger, mais assurez-vous que le modèle est passé à la page
    }


    @GetMapping("/espaces")
    public String afficherEspaces(@RequestParam(required = false) String type,Model model) {
        // Récupérer tous les espaces
        List<EspaceEvenement> espaces = espaceEvenementService.getAllEspaces();
        model.addAttribute("espaces", espaces);
        model.addAttribute("eventType", type != null ? type : "Unknown Event");

        // Retourner la vue pour afficher les espaces
        return "utilisateur/chedy"; // Assurez-vous que le fichier HTML s'appelle "espaces.html"
    }




}
