package com.example.eventmanagement.controllers;

import com.example.eventmanagement.models.Admin;
import com.example.eventmanagement.models.Participant;
import com.example.eventmanagement.models.Prestataire;
import com.example.eventmanagement.services.Adminservice;
import com.example.eventmanagement.services.ParticipantService;
import com.example.eventmanagement.services.PrestataireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private Adminservice adminService;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private PrestataireService prestataireService;

    // Liste des admins
    @GetMapping("/dashboard")
    public String listAdmins(Model model) {
        model.addAttribute("admins", adminService.getAllAdmins());
        return "admin/dashboard";  // Affiche la page contenant la liste des admins
    }
    @GetMapping("/participants")
    public String listParticipants(Model model) {
        List<Participant> participants = participantService.getAllParticipants();
        if (participants.isEmpty()) {
            model.addAttribute("errorMessage", "Aucun participant trouvé.");
        }
        model.addAttribute("participants", participants);
        return "admin/participants"; // Nom du fichier Thymeleaf pour afficher les participants
    }


    @GetMapping("/prestataires")
    public String listPrestataires(Model model) {
        List<Prestataire> prestataires = prestataireService.getAllPrestataires();
        if (prestataires.isEmpty()) {
            model.addAttribute("errorMessage", "Aucun prestataire trouvé.");
        }
        model.addAttribute("prestataires", prestataires); // Ajouter la liste des prestataires au modèle
        return "admin/prestataires"; // Nom du fichier Thymeleaf pour afficher les prestataires
    }
    @GetMapping("/manager")
    public String managerPage(Model model) {
        List<Admin> admins = adminService.getAllAdmins();
        if (admins.isEmpty()) {
            model.addAttribute("errorMessage", "Aucun administrateur trouvé.");
        }
        model.addAttribute("admins", admins);
        return "admin/manager";
    }

    // Modifier un admin via la pop-up
    @PostMapping("/update/{id}")
    public String updateAdmin(@PathVariable Long id, @ModelAttribute("admin") Admin admin, RedirectAttributes redirectAttributes) {
        Optional<Admin> updatedAdmin = adminService.updateAdmin(id, admin);
        if (updatedAdmin.isPresent()) {
            redirectAttributes.addFlashAttribute("successMessage", "Admin updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Admin not found!");
        }
        return "redirect:/admin/dashboard";  // Assuming your modal is on the 'dashboard' page
    }

    @PostMapping("/participants/update/{id}")
    public String updateParticipant(
            @PathVariable Long id,
            @ModelAttribute("participant") Participant participant,
            RedirectAttributes redirectAttributes) {
        try {
            // Update the participant using the service
            adminService.updateParticipant(id, participant);
            redirectAttributes.addFlashAttribute("successMessage", "Participant mis à jour avec succès !");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : " + e.getMessage());
        }
        return "redirect:/admin/dashboard"; // Redirect after update
    }



    // Supprimer un admin
    @GetMapping("/deleteAdmin/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);  // Supprime l'admin
        return "redirect:/admin/dashboard";  // Redirige vers la liste des admins
    }

    // Connexion d'un admin
    @GetMapping("/loginadmin")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new Admin());
        return "admin/loginadmin";  // La page de connexion (loginadmin.html)
    }

    @PostMapping("/loginadmin")
    public String loginAdmin(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<Admin> adminOptional = adminService.findByEmail(email);

        if (adminOptional.isPresent() && adminOptional.get().getPassword().equals(password)) {
            // Si l'admin est connecté avec succès, rediriger vers le tableau de bord
            return "redirect:/admin/dashboard";  // Redirection vers /admin/dashboard
        } else {
            model.addAttribute("errorMessage", "Informations incorrectes. Veuillez réessayer.");
            return "admin/loginadmin"; // Retour à la page de connexion
        }
    }
}
