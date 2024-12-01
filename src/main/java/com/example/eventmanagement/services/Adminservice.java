package com.example.eventmanagement.services;

import com.example.eventmanagement.models.Admin;
import com.example.eventmanagement.models.Participant;
import com.example.eventmanagement.repository.AdminRepository;
import com.example.eventmanagement.repository.ParticipantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Adminservice {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ParticipantRepository participantRepository;

    // Créer un nouvel admin
    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }



    // Récupérer un admin par ID
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll(); // Récupère tous les participants
    }

    public Optional<Admin> updateAdmin(Long id, Admin newAdminData) {
        return adminRepository.findById(id).map(existingAdmin -> {
            existingAdmin.setEmail(newAdminData.getEmail());
            existingAdmin.setPassword(newAdminData.getPassword());
            return adminRepository.save(existingAdmin);
        });
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);  // Supprime un admin par son ID
    }
    public Optional<Admin> findByEmail(String email) {
        return Optional.ofNullable(adminRepository.findByEmail(email));
    }
    @Transactional
    public Participant updateParticipant(Long id, Participant participantDetails) {
        return participantRepository.findById(id).map(existingParticipant -> {
            existingParticipant.setNom(participantDetails.getNom());
            existingParticipant.setPrenom(participantDetails.getPrenom());
            existingParticipant.setEmail(participantDetails.getEmail());
            existingParticipant.setPassword(participantDetails.getPassword());
            existingParticipant.setNumTelephone(participantDetails.getNumTelephone());
            return participantRepository.save(existingParticipant);
        }).orElseThrow(() -> new IllegalArgumentException("Participant not found with id: " + id));
    }


}
