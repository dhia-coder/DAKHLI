package tn.st2i.user_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import tn.st2i.user_backend.entity.User;
import tn.st2i.user_backend.entity.Permission;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private List<String> permissions;
    private RoleDTO roleDTO;
    private Long etablissementId;
    private String etablissementNom;
    private Long classeId;
    private String classeNom;
    private Long disciplineId;
    private String disciplineNom;
    private Long regionId;
    private String regionNom;
    private boolean isBlocked;

    // nouveaux champs exposés
    private String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String gender;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.address = user.getAddress();
        this.birthDate = user.getBirthDate();
        this.gender = user.getGender();
        this.role = user.getRole().getName();
        this.permissions = user.getRole().getPermissions()
            .stream().map(Permission::getName).collect(Collectors.toList());
        this.isBlocked = user.isBlocked();
    }

    // Getters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getRole() { return role; }
    public List<String> getPermissions() { return permissions; }
    public Long getEtablissementId() {
        return etablissementId;
    }
    public String getEtablissementNom() {
        return etablissementNom;
    }
    public Long getClasseId() {
        return classeId;
    }
    public String getClasseNom() {
        return classeNom;
    }
    public Long getDisciplineId() {
        return disciplineId;
    }
    public String getDisciplineNom() {
        return disciplineNom;
    }
    public Long getRegionId() {
        return regionId;
    }
    public String getRegionNom() {
        return regionNom;
    }
    public boolean isBlocked() { return isBlocked; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setRole(String role) { this.role = role; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
    public void setEtablissementId(Long etablissementId) {
        this.etablissementId = etablissementId;
    }
    public void setEtablissementNom(String etablissementNom) {
        this.etablissementNom = etablissementNom;
    }
    public void setClasseId(Long classeId) {
        this.classeId = classeId;
    }
    public void setClasseNom(String classeNom) {
        this.classeNom = classeNom;
    }
    public void setDisciplineId(Long disciplineId) {
        this.disciplineId = disciplineId;
    }
    public void setDisciplineNom(String disciplineNom) {
        this.disciplineNom = disciplineNom;
    }
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }
    public void setRegionNom(String regionNom) {
        this.regionNom = regionNom;
    }
    public void setBlocked(boolean isBlocked) { this.isBlocked = isBlocked; }


    // getters/setters ...

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}