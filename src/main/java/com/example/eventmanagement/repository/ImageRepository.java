package com.example.eventmanagement.repository;

import com.example.eventmanagement.models.EspaceEvenement;
import com.example.eventmanagement.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByEspaceEvenement(EspaceEvenement espaceEvenement);

    List<Image> findByEspaceEvenementIn(List<EspaceEvenement> espaces);
    @Query("SELECT img FROM Image img WHERE img.espaceEvenement.nomComplet = :nomComplet")
    List<Image> findByEspaceNomComplet(@Param("nomComplet") String nomComplet);

}
