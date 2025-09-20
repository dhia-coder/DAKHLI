package tn.st2i.user_backend.dto;

public class DisciplineDTO {
    private Long id;
    private String nom;
    private Integer requiredHours;
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

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }
}