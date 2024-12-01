package com.example.eventmanagement.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Prestataire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "is_validated", nullable = false)
    private boolean isValidated;

    @Column(name = "local_address")
    private String localAddress;

    @Column(name = "local_name")
    private String localName;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "verification_image")
    private String verificationImage;

    @Column(name = "password", nullable = false)
    private String password;
    @OneToMany(mappedBy = "prestataire", cascade = CascadeType.ALL)
    private List<EspaceEvenement> espaces;
// Nouveau champ ajouté

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getVerificationImage() {
        return verificationImage;
    }

    public void setVerificationImage(String verificationImage) {
        this.verificationImage = verificationImage;
    }

    public String getPassword() {
        return password; // Getter pour le mot de passe
    }

    public void setPassword(String password) {
        this.password = password; // Setter pour le mot de passe
    }
}
