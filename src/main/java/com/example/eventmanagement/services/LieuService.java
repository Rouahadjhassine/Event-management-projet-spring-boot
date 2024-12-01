package com.example.eventmanagement.services;

import com.example.eventmanagement.models.Lieu;
import com.example.eventmanagement.repository.LieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LieuService {

    @Autowired
    private com.example.eventmanagement.repository.LieuRepository lieuRepository;  // Le repository pour accéder aux lieux

    // Méthode de filtrage des lieux en fonction des critères
    public List<Lieu> filterLieux(Integer numberOfPeople, Boolean isOpenSpacePreferred, Double budget, String locationPreference) {
        // Appel du repository pour filtrer les lieux
        return lieuRepository.filterLieux(numberOfPeople, isOpenSpacePreferred, budget, locationPreference);
    }
}
