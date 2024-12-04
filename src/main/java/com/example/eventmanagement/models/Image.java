package com.example.eventmanagement.models;

import jakarta.persistence.*;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob  // Pour stocker de grands objets (comme des chaînes encodées en Base64)
    @Column(length = 10485760)  // Taille maximale si nécessaire (ici 10 Mo)
    private String data;

    @ManyToOne
    @JoinColumn(name = "espace_id", nullable = false)
    private EspaceEvenement espaceEvenement;

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public EspaceEvenement getEspaceEvenement() {
        return espaceEvenement;
    }

    public void setEspaceEvenement(EspaceEvenement espaceEvenement) {
        this.espaceEvenement = espaceEvenement;
    }
}
