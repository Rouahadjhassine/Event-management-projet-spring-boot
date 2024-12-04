package com.example.eventmanagement.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.example.eventmanagement.models.Image;



@Entity
public class EspaceEvenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Informations générales
    private String nomComplet;
    private String adresseComplete;
    private String disponibilite;

    // Détails sur l'espace
    private String typeEspace;
    private Integer capacite;

    @OneToMany(mappedBy = "espaceEvenement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();



    public List<Image> getImages() { return images; }  // Correction ici
    public void setImages(List<Image> images) { this.images = images; }  // Correction ici

    @ManyToOne
    @JoinColumn(name = "prestataire_id", nullable = false)
    private Prestataire prestataire;

    // Services et coûts
    private BigDecimal prixDecoration;
    private BigDecimal prixPhotographie;
    private BigDecimal prixBoissons;
    private BigDecimal prixBuffet;

    // Champ calculé
    private Integer nombrePersonnes;
    private BigDecimal coutTotal;

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getAdresseComplete() {
        return adresseComplete;
    }

    public void setAdresseComplete(String adresseComplete) {
        this.adresseComplete = adresseComplete;
    }

    public String getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(String disponibilite) {
        this.disponibilite = disponibilite;
    }

    public String getTypeEspace() {
        return typeEspace;
    }

    public void setTypeEspace(String typeEspace) {
        this.typeEspace = typeEspace;
    }

    public Integer getCapacite() {
        return capacite;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public BigDecimal getPrixDecoration() {
        return prixDecoration;
    }

    public void setPrixDecoration(BigDecimal prixDecoration) {
        this.prixDecoration = prixDecoration;
    }

    public BigDecimal getPrixPhotographie() {
        return prixPhotographie;
    }

    public void setPrixPhotographie(BigDecimal prixPhotographie) {
        this.prixPhotographie = prixPhotographie;
    }

    public BigDecimal getPrixBoissons() {
        return prixBoissons;
    }

    public void setPrixBoissons(BigDecimal prixBoissons) {
        this.prixBoissons = prixBoissons;
    }

    public BigDecimal getPrixBuffet() {
        return prixBuffet;
    }

    public void setPrixBuffet(BigDecimal prixBuffet) {
        this.prixBuffet = prixBuffet;
    }

    public Integer getNombrePersonnes() {
        return nombrePersonnes;
    }

    public void setNombrePersonnes(Integer nombrePersonnes) {
        this.nombrePersonnes = nombrePersonnes;
    }

    public BigDecimal getCoutTotal() {
        return coutTotal;
    }

    public void setCoutTotal(BigDecimal coutTotal) {
        this.coutTotal = coutTotal;
    }

    public Prestataire getPrestataire() {
        return prestataire;
    }

    public void setPrestataire(Prestataire prestataire) {
        this.prestataire = prestataire;
    }
}
