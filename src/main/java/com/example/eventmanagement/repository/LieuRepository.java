package com.example.eventmanagement.repository;

import com.example.eventmanagement.models.Lieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LieuRepository extends JpaRepository<Lieu, Long> {

    // Requête personnalisée pour filtrer les lieux selon les critères
    @Query("SELECT l FROM Lieu l WHERE l.capacity >= :numberOfPeople " +
            "AND l.isOpenSpace = :isOpenSpacePreferred " +
            "AND l.price <= :budget " +
            "AND (l.type LIKE %:locationPreference% OR :locationPreference IS NULL)")
    List<Lieu> filterLieux(Integer numberOfPeople, Boolean isOpenSpacePreferred, Double budget, String locationPreference);
}
