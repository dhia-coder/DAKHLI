package tn.st2i.user_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "disciplines")
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = true)
    private Integer requiredHours;

    @Column(nullable = true)
    private String niveau;

    // Getters and Setters
    public Integer getRequiredHours() {
        return requiredHours;
    }

    public void setRequiredHours(Integer requiredHours) {
        this.requiredHours = requiredHours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }
    public String getNiveau() {
        return niveau;
    }
}