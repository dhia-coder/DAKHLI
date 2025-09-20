package tn.st2i.user_backend.dto;

import java.util.List;

public class ClasseDTO {
    private Long id;
    private String nom;
    private String niveau;
    private Integer capacite;           // nouveau champ
    private Long etablissementId;
    private String etablissementNom;
    private List<UserDTO> students;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }

    public Integer getCapacite() { return capacite; }
    public void setCapacite(Integer capacite) { this.capacite = capacite; }

    public Long getEtablissementId() { return etablissementId; }
    public void setEtablissementId(Long etablissementId) { this.etablissementId = etablissementId; }

    public String getEtablissementNom() { return etablissementNom; }
    public void setEtablissementNom(String etablissementNom) { this.etablissementNom = etablissementNom; }

    public List<UserDTO> getStudents() { return students; }
    public void setStudents(List<UserDTO> students) { this.students = students; }
}
