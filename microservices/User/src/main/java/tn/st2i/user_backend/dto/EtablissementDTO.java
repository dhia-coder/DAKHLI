package tn.st2i.user_backend.dto;

import java.util.List;

public class EtablissementDTO {
    private Long id;
    private String nom;
    private String adresse;
    private Long regionId;
    private String regionNom;
    private List<Long> classeIds;

    // Getters and Setters
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getRegionNom() {
        return regionNom;
    }

    public void setRegionNom(String regionNom) {
        this.regionNom = regionNom;
    }

    public List<Long> getClasseIds() {
        return classeIds;
    }
    public void setClasseIds(List<Long> classeIds) {
        this.classeIds = classeIds;
    }
} 