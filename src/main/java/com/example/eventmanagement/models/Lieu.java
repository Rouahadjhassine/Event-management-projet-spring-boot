package com.example.eventmanagement.models;

import jakarta.persistence.*;

@Entity
public class Lieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;  // Nom du lieu

    @Column(name = "description")
    private String description;  // Description du lieu

    @Column(name = "capacity", nullable = false)
    private Integer capacity;  // Capacité d'accueil du lieu

    @Column(name = "price", nullable = false)
    private Double price;  // Prix du lieu

    @Column(name = "type", nullable = false)
    private String type;  // Type de lieu (restaurant, salle de conférence, etc.)

    @Column(name = "isOpenSpace", nullable = false)
    private Boolean isOpenSpace;  // Si le lieu est un espace ouvert ou non

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsOpenSpace() {
        return isOpenSpace;
    }

    public void setIsOpenSpace(Boolean isOpenSpace) {
        this.isOpenSpace = isOpenSpace;
    }
}
