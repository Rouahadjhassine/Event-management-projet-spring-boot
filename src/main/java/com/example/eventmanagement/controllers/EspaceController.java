package com.example.eventmanagement.controllers;

import com.example.eventmanagement.models.EspaceEvenement;
import com.example.eventmanagement.models.Prestataire;
import com.example.eventmanagement.models.Image;
import com.example.eventmanagement.repository.EspaceEvenementRepository;
import com.example.eventmanagement.repository.PrestataireRepository;
import com.example.eventmanagement.repository.ImageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Base64;
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

        return "redirect:/utilisateur/profileprestataire";
    }

    // Affichage des photos associées aux espaces d'un prestataire
    @GetMapping("/utilisateur/prestataire/gererphotos")
    public String showPhotos(Model model, HttpSession session) {
        Long prestataireId = (Long) session.getAttribute("prestataireId");

        if (prestataireId != null) {
            List<EspaceEvenement> espaces = espaceEvenementRepository.findByPrestataireId(prestataireId);
            List<Image> images = imageRepository.findByEspaceEvenementIn(espaces); // Méthode personnalisée
            model.addAttribute("images", images);
        } else {
            model.addAttribute("error", "Veuillez vous connecter pour accéder à cette page.");
        }

        return "utilisateur/profileprestataire";
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

        return "utilisateur/profileprestataire"; // Nom de la vue Thymeleaf ou JSP à créer
    }

}
