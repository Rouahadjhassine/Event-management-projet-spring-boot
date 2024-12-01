package com.example.eventmanagement.controllers;

import com.example.eventmanagement.models.Participant;
import com.example.eventmanagement.services.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/participants")
public class ParticipantController {
    @Autowired
    private ParticipantService participantService;

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

}
